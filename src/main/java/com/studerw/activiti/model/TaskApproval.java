package com.studerw.activiti.model;

import com.google.common.base.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * User: studerw
 * Date: 5/19/14
 */
public class TaskApproval extends AbstractTask{
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
