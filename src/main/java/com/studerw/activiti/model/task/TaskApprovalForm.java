package com.studerw.activiti.model.task;

import com.studerw.activiti.model.task.AbstractTaskForm;

import javax.validation.constraints.NotNull;

/**
 * @author William Studer
 * Date: 5/19/14
 */
public class TaskApprovalForm extends AbstractTaskForm {
    @NotNull
    private Boolean approved;

    public Boolean getApproved() {
        return approved;
    }
    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    @Override public String toString() {
        final StringBuilder sb = new StringBuilder("TaskApproval{");
        sb.append(super.toString());
        sb.append("approved=").append(approved);
        sb.append('}');
        return sb.toString();
    }
}
