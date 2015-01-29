package com.studerw.activiti.model.task;

import com.google.common.base.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Abstract base type of web forms used for completing user tasks
 * @author William Studer
 * Date: 5/19/14
 */
public abstract class AbstractTaskForm {
    @NotNull
    @Size(max = 10000, min = 2)
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
