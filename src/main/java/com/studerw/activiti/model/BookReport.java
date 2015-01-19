package com.studerw.activiti.model;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author studerw
 */
public class BookReport extends Document {

    @NotEmpty
    private String bookAuthor;
    @NotEmpty
    private String bookTitle;
    @NotEmpty
    private String content;
    @NotEmpty
    private String summary;

    public BookReport() {};
//        this.docType = DocType.BOOK_REPORT;
//    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
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
}
