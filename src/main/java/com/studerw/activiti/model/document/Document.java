package com.studerw.activiti.model.document;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author William Studer
 * Date: 5/20/14
 */
public abstract class Document implements Serializable, Comparable<Document> {
    protected String id;
    @NotNull
    protected String author;
    @NotNull
    protected String title;
    @NotNull
    protected String groupId;
    @NotNull @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm Z")
    protected Date createdDate;
    @NotNull
    protected DocState docState = DocState.DRAFT;
    @NotNull
    protected DocType docType;

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

    public DocState getDocState() {
        return docState;
    }

    public void setDocState(DocState docState) {
        this.docState = docState;
    }

    public DocType getDocType() {
        return docType;
    }

    public void setDocType(DocType docType) {
        this.docType = docType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Document document = (Document) o;

        if (id != null ? !id.equals(document.id) : document.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public int compareTo(Document o) {
        return ObjectUtils.compare(this.createdDate, o.createdDate);
    }

    public boolean isEditable(String author, String currentUser) {
        if (StringUtils.equals(author, currentUser)) {
            return DocState.DRAFT.equals(this.docState) || DocState.REJECTED.equals(this.docState);
        } else return DocState.WAITING_FOR_COLLABORATION.equals(this.docState);
    }

    @Override public String toString() {
        final StringBuilder sb = new StringBuilder("Document{");
        sb.append("id='").append(id).append('\'');
        sb.append(", author='").append(author).append('\'');
        sb.append(", title='").append(title).append('\'');
        sb.append(", groupId='").append(groupId).append('\'');
        sb.append(", createdDate=").append(createdDate);
        sb.append(", docState=").append(docState);
        sb.append(", docType=").append(docType);
        sb.append('}');
        return sb.toString();
    }
}

