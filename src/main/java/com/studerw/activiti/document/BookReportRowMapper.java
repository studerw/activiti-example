package com.studerw.activiti.document;

import com.studerw.activiti.model.document.DocState;
import com.studerw.activiti.model.document.BookReport;
import com.studerw.activiti.model.document.DocType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author William Studer
 */
public class BookReportRowMapper implements RowMapper<BookReport> {

    @Override
    public BookReport mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        String id = rs.getString("id");
        String author = StringUtils.trim(rs.getString("author"));
        String title = StringUtils.trim(rs.getString("title"));
        String bookAuthor = StringUtils.trim(rs.getString("book_author"));
        String bookTitle = StringUtils.trim(rs.getString("book_title"));
        String content = StringUtils.trim(rs.getString("content"));
        String summary = StringUtils.trim(rs.getString("summary"));
        String groupId = StringUtils.trim(rs.getString("group_Id"));
        String docState = StringUtils.trim(rs.getString("doc_state"));
        String docType = StringUtils.trim(rs.getString("doc_type"));
        Date createdDate = rs.getDate("created_date");
        BookReport BookReport = new BookReport();
        BookReport.setId(id);
        BookReport.setAuthor(author);
        BookReport.setBookAuthor(bookAuthor);
        BookReport.setTitle(title);
        BookReport.setContent(content);
        BookReport.setSummary(summary);
        BookReport.setBookTitle(bookTitle);
        BookReport.setGroupId(groupId);
        BookReport.setDocState(DocState.valueOf(docState));
        BookReport.setDocType(DocType.valueOf(docType));
        BookReport.setCreatedDate(createdDate);
        return BookReport;

    }
}

