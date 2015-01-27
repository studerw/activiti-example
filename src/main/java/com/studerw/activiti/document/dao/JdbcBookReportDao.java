package com.studerw.activiti.document.dao;

import com.google.common.collect.ImmutableMap;
import com.studerw.activiti.document.BookReportRowMapper;
import com.studerw.activiti.model.document.BookReport;
import com.studerw.activiti.web.PagingCriteria;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Implementation of {@link BookReportDao} using Spring / JDBC
 *
 * @author William Studer
 */
@Repository
@Component("bookReportDao")
public class JdbcBookReportDao implements BookReportDao {
    private static final Logger LOG = LoggerFactory.getLogger(JdbcBookReportDao.class);
    protected DataSource ds;
    protected NamedParameterJdbcTemplate namedJdbcTemplate;

    public DataSource getDataSource() {
        return this.ds;
    }

    @Autowired
    @Qualifier("dataSource")
    public void setDataSource(DataSource datasource) {
        this.ds = datasource;
        this.namedJdbcTemplate = new NamedParameterJdbcTemplate(this.ds);
    }

    @Override
    public int getCount() {
        String sql = "SELECT count(*) FROM BOOK_REPORT";
        @SuppressWarnings("unchecked")
        int count = this.namedJdbcTemplate.queryForObject(sql, Collections.EMPTY_MAP, Integer.class);
        LOG.debug("Got count: {} of book reports", count);
        return count;
    }

    @Override public List<BookReport> readAll() {
        String sql = "SELECT * FROM BOOK_REPORT ORDER BY created_date ASC";
        List<BookReport> reports = this.namedJdbcTemplate.query(sql, new BookReportRowMapper());
        LOG.debug("got all book reports: {}", reports.size());
        return reports;
    }


    @Override public List<BookReport> readPage(PagingCriteria criteria) {
        LOG.debug("reading page with criteria: {}", criteria);
        if (criteria == null || criteria.getLimit() == null || criteria.getStart() == null) {
            LOG.warn("criteria invalid - reading all instead of subset");
            return readAll();
        }
        String sql = "SELECT LIMIT :start :limit * FROM BOOK_REPORT ORDER BY created_date ASC";
        BeanPropertySqlParameterSource source = new BeanPropertySqlParameterSource(criteria);

        List<BookReport> documents = this.namedJdbcTemplate.query(sql, source, new BookReportRowMapper());
        LOG.debug("{} reports returned using criteria: {}", documents.size(), criteria);

        return documents;

    }

    @Override public String create(BookReport obj) {
        LOG.debug("Inserting Document into SQL backend: {}",obj);
        checkArgument(StringUtils.isBlank(obj.getId()), "Document id cannot be already set");

        String id = UUID.randomUUID().toString();
        obj.setId(id);
        String sql = "INSERT INTO BOOK_REPORT (id, author, title, content, summary, group_id, doc_type, doc_state, created_date, book_author, book_title) " +
                "VALUES (:id, :author, :title, :content, :summary, :groupId, :docType, :docState, :createdDate, :bookAuthor, :bookTitle)";

        BeanPropertySqlParameterSource source = new BeanPropertySqlParameterSource(obj);
        source.registerSqlType("docState", Types.VARCHAR);
        source.registerSqlType("docType", Types.VARCHAR);
        int results = this.namedJdbcTemplate.update(sql, source);
        LOG.debug("Got: {} results", results);
        obj.setId(id);
        return id;
    }

    @Override public void createWithId(BookReport obj) {
        throw new UnsupportedOperationException("not supported");
    }

    @Override public void update(BookReport obj) {
        checkArgument(StringUtils.isNotBlank(obj.getId()), "book report id cannot be blank");
        String sql = "UPDATE BOOK_REPORT SET author=:author, title=:title, content=:content, summary=:summary, doc_state=:docState, " +
                "book_author=:bookAuthor, book_title=:bookTitle WHERE id=:id";

        BeanPropertySqlParameterSource source = new BeanPropertySqlParameterSource(obj);
        source.registerSqlType("docState", Types.VARCHAR);
        int results = this.namedJdbcTemplate.update(sql, source);
        LOG.debug("Updated: {} Book Reports", results);
    }

    @Override public void delete(String id) {
        String sql = "DELETE FROM BOOK_REPORT WHERE id = :id";
        Map<String, String> params = ImmutableMap.of("id", id);
        int deleted = this.namedJdbcTemplate.update(sql, params);
        LOG.debug("Deleted: {} BookReports", deleted);
    }

    @Override public BookReport read(String id) {
        String sql = "SELECT * FROM Book_REPORT where id = :id";
        Map<String, String> params = ImmutableMap.of("id", id);
        BookReport bookReport = this.namedJdbcTemplate.queryForObject(sql, params, new BookReportRowMapper());
        return bookReport;
    }
}
