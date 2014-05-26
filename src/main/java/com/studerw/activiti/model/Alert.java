package com.studerw.activiti.model;

import com.google.common.base.Objects;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Priority might coorespond to bootstrap helper CSS classes:
 * 1 = primary
 * 2 = success
 * 3 = info
 * 4 = warning
 * 5 = danger
 * User: studerw
 * Date: 5/20/14
 */
public class Alert implements Comparable<Alert>, Serializable {
    public final static int SUCCESS = 1;
    public final static int INFO = 2;
    public final static int WARNING = 3;
    public final static int DANGER = 4;

    @NotNull
    String id;
    @NotNull
    private String createdBy;
    @NotNull
    private String userId;
    @NotNull
    private String message;
    @NotNull @Min(1) @Max(4)
    private Integer priority;
    @NotNull @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm Z")
    private Date createdDate;
    private Boolean acknowledged = Boolean.FALSE;

    public Alert() {
    }

    public Alert(String id, String createdBy, String userId, String message, Integer priority, Date createdDate, Boolean acknowledged) {
        this.id = id;
        this.createdBy = createdBy;
        this.userId = userId;
        this.message = message;
        this.priority = priority;
        this.createdDate = createdDate;
        this.acknowledged = acknowledged;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Boolean getAcknowledged() {
        return acknowledged;
    }

    public void setAcknowledged(Boolean acknowledged) {
        this.acknowledged = acknowledged;
    }

    @Override
    public int compareTo(Alert o) {
        return this.createdDate.compareTo(o.createdDate);  //To change body of implemented methods use File | Settings | File Templates.
    }


    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("createdBy", createdBy)
                .add("userId", userId)
                .add("message", message)
                .add("priority", priority)
                .add("createdDate", createdDate)
                .add("acknowledged", acknowledged)
                .toString();
    }
}

