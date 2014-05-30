package com.studerw.activiti.task;

import com.google.common.collect.Lists;
import com.studerw.activiti.model.HistoricTask;
import com.studerw.activiti.model.TaskApproval;
import com.studerw.activiti.model.TaskForm;
import com.studerw.activiti.user.UserService;
import com.studerw.activiti.util.Workflow;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: studerw
 * Date: 5/18/14
 */
@Service("localTaskService")
public class LocalTaskService {
    private static final Logger log = LoggerFactory.getLogger(LocalTaskService.class);

    @Autowired
    TaskService taskService;
    @Autowired
    IdentityService identityService;
    @Autowired
    RuntimeService runtimeService;
    @Autowired
    UserService userService;
    @Autowired
    HistoryService historyService;

    /**
     * @param userId
     * @return a list of all tasks either assigned or as possible candidate based on user groups.
     *         Removes all cases of 'ApproveDoc' tasks where the user is the author of the document, as we
     *         assume the document author cannot also be an approver. This may change in the future.
     */
    public List<TaskForm> getTasks(String userId) {
        log.debug("Getting tasks for user: {}", userId);
        List<Task> tasks = taskService.createTaskQuery().
                includeProcessVariables().
                taskCandidateOrAssigned(userId).
                orderByTaskCreateTime().asc().list();
        List<TaskForm> taskForms = Lists.newArrayList();
        try {
            for (Task task : tasks) {
                if (!isDocAuthor(task, userId)) {
                    taskForms.add(TaskForm.fromTask(task));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error converting task to Task Form", e);
        }
        log.debug("got {} tasks for user {}", taskForms.size(), userId);
        return taskForms;
    }

    public void approveTask(TaskApproval taskApproval) {
        this.approveTask(taskApproval.getApproved(), taskApproval.getComment(), taskApproval.getTaskId());
    }

    public void approveTask(boolean approved, String comment, String taskId) {
        log.debug("New User task completion: " + approved);
        UserDetails userDetails = userService.currentUser();
        try {
            identityService.setAuthenticatedUserId(userDetails.getUsername());
            Task task = taskService.createTaskQuery().taskId(taskId).includeProcessVariables().singleResult();
            if (task == null) {
                throw new RuntimeException("Unable to find task - it's possible another user has already completed it");
            }
            Map<String, Object> vars = task.getProcessVariables();
            runtimeService.setVariable(task.getExecutionId(), "approved", approved);
            vars.put("approved", approved);
            if (StringUtils.equalsIgnoreCase((String) vars.get("initiator"), userDetails.getUsername())) {
                throw new RuntimeException("The author of a document cannot perform approvals of the same document");
            }
            taskService.setAssignee(task.getId(), userDetails.getUsername());
            taskService.addComment(task.getId(), task.getProcessInstanceId(), comment);
            Map<String, Object> taskVariables = new HashMap<String, Object>();
            taskService.setVariableLocal(task.getId(), "taskOutcome", approved ? "Approved" : "Rejected");
            taskService.complete(task.getId(), taskVariables);
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
    public List<HistoricTask> getDocApprovalHistory(String businessKey) {
        log.debug("getting historic tasks for doc: " + businessKey);
        HistoricProcessInstance pi = historyService.createHistoricProcessInstanceQuery().
                includeProcessVariables().processInstanceBusinessKey(businessKey).singleResult();

        if (pi == null) {
            return Collections.emptyList();
        }
        log.debug("Duration time in millis: " + pi.getDurationInMillis());
        List<HistoricTaskInstance> hTasks;
        hTasks = historyService.createHistoricTaskInstanceQuery().includeTaskLocalVariables().processInstanceBusinessKey(businessKey).list();
        List<HistoricTask> historicTasks = Lists.newArrayList();
        for (HistoricTaskInstance hti : hTasks) {
            if (StringUtils.startsWith(hti.getProcessDefinitionId(), Workflow.PROCESS_ID_DOC_APPROVAL)
                    && hti.getEndTime() != null) {
                historicTasks.add(fromActiviti(hti));
            }
        }
        Collections.sort(historicTasks);
        return historicTasks;
    }

    protected HistoricTask fromActiviti(HistoricTaskInstance hti) {
        HistoricTask ht = new HistoricTask();
        ht.setId(hti.getId());
        ht.setName(hti.getName());
        ht.setUserId(hti.getAssignee());
        ht.setComments(taskService.getTaskComments(hti.getId()));
        Map<String, Object> vars = hti.getTaskLocalVariables();
        ht.setLocalVars(vars);
        ht.setCompletedDate(hti.getEndTime());

        return ht;
    }

    boolean isDocAuthor(Task task, String userId) {
        log.debug("********** " + task.getTaskDefinitionKey() + " ********");
        //is not docApprove Task
        if (!StringUtils.startsWithIgnoreCase(task.getTaskDefinitionKey(), Workflow.TASK_ID_DOC_APPROVAL)) {
            return false;
        }
        String author = (String) task.getProcessVariables().get("initiator");
        return ObjectUtils.equals(author, userId);
    }
}
