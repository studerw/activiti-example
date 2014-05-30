package com.studerw.activiti.model;

import com.google.common.base.Objects;
import com.studerw.activiti.util.Workflow;
import org.activiti.engine.task.Comment;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * User: studerw
 * Date: 5/22/14
 */
public class HistoricTask implements Comparable<HistoricTask> {

    String id;
    String name;
    String userId;
    List<Comment> comments;
    Map<String, Object> localVars;
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm Z")
    Date completedDate;

    //String taskOutcome;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Map<String, Object> getLocalVars() {
        return localVars;
    }

    public void setLocalVars(Map<String, Object> localVars) {
        this.localVars = localVars;
    }

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("userId", userId)
                .add("comments", comments)
                .add("localVars", localVars)
                .add("completedDate", completedDate)
                .toString();
    }

    @Override
    public int compareTo(HistoricTask o) {
        return ObjectUtils.compare(this.completedDate, o.completedDate);
    }

}
