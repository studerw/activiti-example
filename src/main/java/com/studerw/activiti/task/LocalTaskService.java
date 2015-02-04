package com.studerw.activiti.task;

import com.google.common.collect.Lists;
import com.studerw.activiti.model.task.CandidateTask;
import com.studerw.activiti.model.task.HistoricTask;
import com.studerw.activiti.model.task.TaskApprovalForm;
import com.studerw.activiti.user.UserService;
import com.studerw.activiti.workflow.WFConstants;
import com.studerw.activiti.workflow.WorkflowService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author William Studer
 *         Date: 5/18/14
 */
@Service("localTaskService")
public class LocalTaskService {
    private static final Logger LOG = LoggerFactory.getLogger(LocalTaskService.class);

    @Autowired protected TaskService taskService;
    @Autowired protected IdentityService identityService;
    @Autowired protected RuntimeService runtimeService;
    @Autowired protected UserService userService;
    @Autowired protected HistoryService historyService;
    @Autowired protected WorkflowService workflowService;

    /**
     * @param userId
     * @return a list of all tasks either assigned or as possible candidate based on user groups.
     * Removes all cases of 'ApproveDoc' tasks where the user is the author of the document, as we
     * assume the document author cannot also be an approver. This may change in the future.
     */
    public List<CandidateTask> findCandidateTasks(String userId) {
        LOG.debug("Getting tasks for user: {}", userId);
        List<Task> tasks = taskService.createTaskQuery().
                includeProcessVariables().
                taskCandidateOrAssigned(userId).
                orderByTaskCreateTime().asc().list();
        List<CandidateTask> candidateTasks = Lists.newArrayList();
        try {
            for (Task task : tasks) {
                if (!isDocAuthor(task, userId)) {
                    candidateTasks.add(CandidateTask.fromTask(task));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error converting task to Task Form", e);
        }
        LOG.debug("got {} tasks for user {}", candidateTasks.size(), userId);
        return candidateTasks;
    }

    /**
     * Complete an approval task
     *
     * @param taskApprovalForm
     */
    public void approveOrRejectDoc(TaskApprovalForm taskApprovalForm) {
        this.approveOrRejectDoc(taskApprovalForm.getApproved(), taskApprovalForm.getComment(), taskApprovalForm.getTaskId());
    }

    /**
     * Complete an approval task
     *
     * @param approved
     * @param comment
     * @param taskId
     */
    public void approveOrRejectDoc(boolean approved, String comment, String taskId) {
        LOG.debug("Approve Task completion?: " + approved);
        UserDetails userDetails = userService.currentUser();
        try {
            identityService.setAuthenticatedUserId(userDetails.getUsername());
            Task task = taskService.createTaskQuery().taskId(taskId).includeProcessVariables().singleResult();
            if (task == null) {
                throw new RuntimeException("Unable to find task - it's possible another user has already completed it");
            }
            Map<String, Object> vars = task.getProcessVariables();
            if (StringUtils.equalsIgnoreCase((String) vars.get("initiator"), userDetails.getUsername())) {
                throw new RuntimeException("The author of a document cannot perform approvals of the same document");
            }
            runtimeService.setVariable(task.getExecutionId(), WFConstants.PROCESS_VAR_APPROVED_OR_REJECTED, approved);
            taskService.setAssignee(task.getId(), userDetails.getUsername());
            taskService.addComment(task.getId(), task.getProcessInstanceId(), comment);
            //String outcome = String.format("%s (%s)", approved ? "Approved" : "Rejected",  userDetails.getUsername());
            taskService.setVariableLocal(task.getId(), WFConstants.TASK_VAR_OUTCOME, approved ? "Approved" : "Rejected");
            taskService.complete(task.getId());
        } finally {
            identityService.setAuthenticatedUserId(null);
        }
    }

    /**
     * Complete a collaboration task
     *
     * @param taskId
     * @param comment
     */
    public void collaborateTask(String taskId, String comment) {
        LOG.debug("Collaboration Task completion");
        UserDetails userDetails = userService.currentUser();
        try {
            identityService.setAuthenticatedUserId(userDetails.getUsername());
            Task task = taskService.createTaskQuery().taskId(taskId).includeProcessVariables().singleResult();
            if (task == null) {
                throw new RuntimeException("Unable to find task - it's possible another user has already completed it");
            }
            taskService.setAssignee(task.getId(), userDetails.getUsername());
            taskService.addComment(task.getId(), task.getProcessInstanceId(), comment);
            //String outcome = String.format("Collaborated (%s)", userDetails.getUsername());
            taskService.setVariableLocal(task.getId(), WFConstants.TASK_VAR_OUTCOME, "Collaborated");
            taskService.complete(task.getId());
        } finally {
            identityService.setAuthenticatedUserId(null);
        }
    }

    /**
     * Returns a list of <strong>completed</strong> Doc Approval Tasks.
     *
     * @param businessKey
     * @return
     */
    public List<HistoricTask> getTaskHistory(String businessKey) {
        LOG.debug("getting historic tasks for doc: {}", businessKey);

        HistoricProcessInstance historicPI = historyService.createHistoricProcessInstanceQuery().
                /*includeProcessVariables()*/processInstanceBusinessKey(businessKey).singleResult();

        if (historicPI == null) {
            return Collections.emptyList();
        }
        LOG.debug("Duration time in millis: {}", historicPI.getDurationInMillis());
        //historyService.createProcessInstanceHistoryLogQuery(hTasks.get(0).getProcessInstanceId()).includeActivities().includeComments().includeTasks().singleResult()
        List<HistoricTaskInstance> hTasks = historyService.createHistoricTaskInstanceQuery().includeTaskLocalVariables().
                processInstanceBusinessKey(businessKey).list();
        List<HistoricTask> historicTasks = Lists.newArrayList();
        for (HistoricTaskInstance hti : hTasks) {
            historicTasks.add(fromActiviti(hti));
        }
        Collections.sort(historicTasks);
        return historicTasks;
    }

    protected HistoricTask fromActiviti(HistoricTaskInstance historicTI) {
        HistoricTask ht = new HistoricTask();
        ht.setId(historicTI.getId());
        ht.setName(historicTI.getName());
        ht.setUserId(historicTI.getAssignee());
        ht.setComments(taskService.getTaskComments(historicTI.getId()));
        Map<String, Object> vars = historicTI.getTaskLocalVariables();
        ht.setLocalVars(vars);
        ht.setCompletedDate(historicTI.getEndTime());
        return ht;
    }

    boolean isDocAuthor(Task task, String userId) {
        String author = (String) task.getProcessVariables().get("initiator");
        return Objects.equals(author, userId);
    }
}
