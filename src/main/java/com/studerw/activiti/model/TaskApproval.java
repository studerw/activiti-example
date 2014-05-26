package com.studerw.activiti.model;

import com.google.common.base.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * User: studerw
 * Date: 5/19/14
 */
public class TaskApproval {
    @NotNull
    String taskId;
    @Size(max = 4000)
    String comment;
    @NotNull
    Boolean approved;

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
