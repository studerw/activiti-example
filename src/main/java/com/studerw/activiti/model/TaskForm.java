package com.studerw.activiti.model;

import com.google.common.base.Objects;
import org.activiti.engine.task.Task;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Map;

/**
 * User: studerw
 * Date: 5/18/14
 */
public class TaskForm {
    String id;
    String owner;

    public String getTaskDefinitionKey() {
        return taskDefinitionKey;
    }

    public void setTaskDefinitionKey(String taskDefinitionKey) {
        this.taskDefinitionKey = taskDefinitionKey;
    }

    String delegate;
    String category;
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm Z")
    Date createTime;
    String description;
    String executionId;
    String name;
    String assignee;
    int priority;
    String processDefinitionId;
    String processInstanceId;
    Map<String, Object> processVariables;
    boolean suspended;
    String taskDefinitionKey;


    public TaskForm() {}

    public static TaskForm fromTask(Task task) throws InvocationTargetException, IllegalAccessException {
        TaskForm taskForm = new TaskForm();
        BeanUtils.copyProperties(taskForm, task);
        return taskForm;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getDelegate() {
        return delegate;
    }

    public void setDelegate(String delegate) {
        this.delegate = delegate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public Map<String, Object> getProcessVariables() {
        return processVariables;
    }

    public void setProcessVariables(Map<String, Object> processVariables) {
        this.processVariables = processVariables;
    }

    public boolean isSuspended() {
        return suspended;
    }

    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("owner", owner)
                .add("delegate", delegate)
                .add("category", category)
                .add("createTime", createTime)
                .add("description", description)
                .add("executionId", executionId)
                .add("name", name)
                .add("assignee", assignee)
                .add("priority", priority)
                .add("processDefinitionId", processDefinitionId)
                .add("processInstanceId", processInstanceId)
                .add("processVariables", processVariables)
                .add("suspended", suspended)
                .add("taskDefinitionKey", taskDefinitionKey)
                .toString();
    }
}
