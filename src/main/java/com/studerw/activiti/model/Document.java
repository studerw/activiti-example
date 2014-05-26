package com.studerw.activiti.model;

import com.google.common.base.Objects;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * User: studerw
 * Date: 5/20/14
 */
public class Document implements Comparable<Document>, Serializable {
    public final static String STATE_DRAFT = "DRAFT";
    public final static String STATE_APPROVED = "APPROVED";
    public final static String STATE_REJECTED = "REJECTED";
    public final static String STATE_WAITING_FOR_APPROVAL = "WAITING FOR APPROVAL";
    public final static String STATE_PUBLISHED = "PUBLISHED";

    @NotNull
    String id = "TEMP";
    @NotNull
    private String author;
    @NotNull
    private String title;
    @NotNull
    private String content;
    @NotNull
    private String summary;
    @NotNull
    private String groupId;
    @NotNull @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm Z")
    private Date createdDate;
    @NotNull
    private String state = STATE_DRAFT;

    public Document() {}


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("author", author)
                .add("title", title)
                .add("content", content)
                .add("summary", summary)
                .add("groupId", groupId)
                .add("createdDate", createdDate)
                .add("state", state)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Document)) return false;

        Document document = (Document) o;

        if (!id.equals(document.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public int compareTo(Document o) {
        return ObjectUtils.compare(this.createdDate, o.createdDate);
    }

    public boolean isEditable() {
        return STATE_DRAFT.equals(this.state) || STATE_REJECTED.equals(this.state);
    }
}

