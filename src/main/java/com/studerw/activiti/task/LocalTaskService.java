package com.studerw.activiti.task;

import com.google.common.collect.Lists;
import com.studerw.activiti.model.TaskApproval;
import com.studerw.activiti.model.TaskForm;
import com.studerw.activiti.user.UserService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

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

    public List<TaskForm> getTasks(String userId) {
        log.debug("Getting tasks for user: {}", userId);
        List<Task> tasks = taskService.createTaskQuery().
                includeProcessVariables().
                taskCandidateOrAssigned(userId).
                orderByTaskCreateTime().asc().list();
        List<TaskForm> taskForms = Lists.newArrayList();
        try {
            for(Task task : tasks){
                taskForms.add(TaskForm.fromTask(task));
            }
        }
        catch(Exception e){
            throw new RuntimeException("Error converting task to Task Form", e);
        }
        log.debug("got {} tasks for user {}", taskForms.size(), userId);
        return taskForms;
    }

    public void approveTask(TaskApproval taskApproval){
        this.approveTask(taskApproval.getApproved(), taskApproval.getComment(), taskApproval.getTaskId());
    }

    public void approveTask(boolean approved, String comment, String taskId){
        log.debug("New User task completion: " + approved);
        UserDetails userDetails = userService.currentUser();
        try {
            identityService.setAuthenticatedUserId(userDetails.getUsername());
            Task task = taskService.createTaskQuery().taskId(taskId).includeProcessVariables().singleResult();
            if (task == null){
                throw new RuntimeException("Unable to find task - it's possible another user has already completed it");
            }
            Map<String, Object> vars = task.getProcessVariables();
            if (StringUtils.equalsIgnoreCase((String)vars.get("initiator"), userDetails.getUsername())){
                throw new RuntimeException("The author of a document cannot perform approvals of the same document");
            }
            taskService.setAssignee(task.getId(), userDetails.getUsername());
            taskService.addComment(task.getId(), task.getProcessInstanceId(), comment);
            Map <String, Object> taskVariables = new HashMap<String, Object>();
            taskVariables.put("approved", approved);
            taskService.setVariableLocal(task.getId(), "taskOutcome", approved ? "Approved" : "Rejected");
            taskService.complete(task.getId(), taskVariables);
        }
        finally{
            identityService.setAuthenticatedUserId(null);
        }
    }
}
