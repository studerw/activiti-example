package com.studerw.activiti.document;

import com.studerw.activiti.model.document.DocState;
import com.studerw.activiti.model.document.DocType;
import com.studerw.activiti.model.document.Invoice;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author William Studer
 */
public class InvoiceRowMapper implements RowMapper<Invoice> {

    @Override
    public Invoice mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        String id = rs.getString("id");
        String author = StringUtils.trim(rs.getString("author"));
        String title = StringUtils.trim(rs.getString("title"));
        String groupId = StringUtils.trim(rs.getString("group_Id"));
        String docState = StringUtils.trim(rs.getString("doc_state"));
        String docType = StringUtils.trim(rs.getString("doc_type"));
        String payee = StringUtils.trim(rs.getString("payee"));
        BigDecimal amount = rs.getBigDecimal("amount");
        Date createdDate = rs.getDate("created_date");
        Invoice Invoice = new Invoice();
        Invoice.setId(id);
        Invoice.setAuthor(author);
        Invoice.setGroupId(groupId);
        Invoice.setTitle(title);
        Invoice.setDocState(DocState.valueOf(docState));
        Invoice.setDocType(DocType.valueOf(docType));
        Invoice.setCreatedDate(createdDate);
        Invoice.setPayee(payee);
        Invoice.setAmount(amount);
        return Invoice;
    }
}

