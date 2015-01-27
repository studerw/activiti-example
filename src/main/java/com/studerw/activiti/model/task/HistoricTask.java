package com.studerw.activiti.model.task;

import com.google.common.base.Objects;
import org.activiti.engine.task.Comment;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author William Studer
 *         Date: 5/22/14
 */
public class HistoricTask implements Comparable<HistoricTask> {

    private String id;
    private String name;
    private String userId;
    private List<Comment> comments;
    private Map<String, Object> localVars;
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm Z")
    private Date completedDate;

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
    public int compareTo(HistoricTask that) {
        if (this.completedDate == null) {
            if (that.completedDate == null) {
                return 0; //equal
            } else {
                return 1; // null is after other dates
            }
        } else // this.member != null
            if (that.completedDate == null) {
                return -1;  // all other dates are before null
            } else {
                return ObjectUtils.compare(this.completedDate, that.completedDate);
            }
    }
}
