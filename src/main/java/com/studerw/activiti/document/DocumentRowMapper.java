package com.studerw.activiti.document;

import com.studerw.activiti.model.Document;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author studerw
 */
public class DocumentRowMapper implements RowMapper<Document> {

    @Override
    public Document mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        String id = rs.getString("id");
        String author = StringUtils.trim(rs.getString("author"));
        String title = StringUtils.trim(rs.getString("title"));
        String content = StringUtils.trim(rs.getString("content"));
        String summary = StringUtils.trim(rs.getString("summary"));
        String groupId = StringUtils.trim(rs.getString("group_Id"));
        String state = StringUtils.trim(rs.getString("state_"));
        Date createdDate = rs.getDate("created_date");
        Document document = new Document();
        document.setId(id);
        document.setAuthor(author);
        document.setTitle(title);
        document.setContent(content);
        document.setSummary(summary);
        document.setGroupId(groupId);
        document.setState(state);
        document.setCreatedDate(createdDate);
        return document;

    }
}

