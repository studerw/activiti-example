package com.studerw.activiti.model;

import com.google.common.base.Objects;

import java.util.Date;

/**
 * User: studerw
 * Date: 5/20/14
 */
public class Document {
    String id;
    String author;
    String title;
    String content;
    String summary;
    String group;
    Date created;

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

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("author", author)
                .add("title", title)
                .add("content", content)
                .add("summary", summary)
                .add("group", group)
                .add("created", created)
                .toString();
    }
}

