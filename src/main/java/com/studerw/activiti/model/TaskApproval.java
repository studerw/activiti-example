package com.studerw.activiti.model;

import com.google.common.base.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * User: studerw
 * Date: 5/19/14
 */
public class TaskApproval {
    @Size(max = 4000)
    private String comment;
    @NotNull
    private Boolean approved;
    @NotNull
    private String taskId;

    public TaskApproval() {
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("taskId", taskId)
                .add("comment", comment)
                .add("approved", approved)
                .toString();
    }
}
