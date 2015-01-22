package com.studerw.activiti.model;

import com.google.common.base.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * User: studerw
 * Date: 5/19/14
 */
public abstract class AbstractTask {
    @Size(max = 4000)
    private String comment;
    @NotNull
    private String taskId;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Override public String toString() {
        final StringBuilder sb = new StringBuilder("AbstractTask{");
        sb.append("comment='").append(comment).append('\'');
        sb.append(", taskId='").append(taskId).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
