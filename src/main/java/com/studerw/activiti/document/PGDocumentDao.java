package com.studerw.activiti.document;

import com.google.common.collect.ImmutableMap;
import com.studerw.activiti.model.Document;
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
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * User: studerw
 * Date: 5/20/14
 */
@Repository
@Component("docDao")
public class PGDocumentDao implements DocumentDao {
    private static final Logger log = LoggerFactory.getLogger(PGDocumentDao.class);
    protected DataSource ds;
    protected NamedParameterJdbcTemplate namedJdbcTemplate;

    @Autowired
    @Qualifier("dataSource")
    public void setDataSource(DataSource datasource) {
        this.ds = datasource;
        this.namedJdbcTemplate = new NamedParameterJdbcTemplate(this.ds);
    }

    public DataSource getDataSource() {
        return this.ds;
    }

    @Override
    @Transactional
    public String create(Document document) {
        log.debug("Inserting Document into SQL backend: " + document);
        checkArgument(StringUtils.isBlank(document.getId()), "Document id cannot be already set");

        String id = UUID.randomUUID().toString();
        document.setId(id);
        String sql = "INSERT INTO Document (id, author, title, content, summary, group_id, state_, created_date) " +
                "VALUES (:id, :author, :title, :content, :summary, :groupId, :state, :createdDate)";

        BeanPropertySqlParameterSource source = new BeanPropertySqlParameterSource(document);
        int results = this.namedJdbcTemplate.update(sql, source);
        log.debug("Got: " + results + " results");
        document.setId(id);
        return id;
    }

    @Override
    public void createWithId(Document obj) {
        throw new UnsupportedOperationException("not supported");
    }


    @Override
    @Transactional(readOnly = true)
    public Document read(String documentId) {
        String sql = "SELECT * FROM Document where id = :id";
        Map<String, String> params = ImmutableMap.of("id", documentId);
        Document document = this.namedJdbcTemplate.queryForObject(sql, params, new DocumentRowMapper());
        return document;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Document> readAll() {
        String sql = "SELECT * FROM Document ORDER BY created_date ASC";
        List<Document> documents = this.namedJdbcTemplate.query(sql, new DocumentRowMapper());
        log.debug("got all Documents: " + documents.size());
        return documents;
    }


    @Override
    @Transactional(readOnly = true)
    public int getCount() {
        String sql = "SELECT count(*) FROM Document";
        @SuppressWarnings("unchecked")
        int count = this.namedJdbcTemplate.queryForObject(sql, Collections.EMPTY_MAP, Integer.class);
        log.debug("Got count: " + count + " of documents");
        return count;
    }


    @Override
    @Transactional
    public void delete(String documentId) {
        String sql = "DELETE FROM Document WHERE id = :id";
        Map<String, String> params = ImmutableMap.of("id", documentId);
        int deleted = this.namedJdbcTemplate.update(sql, params);
        log.debug("Deleted: " + deleted + " Documents");
    }


    @Override
    @Transactional
    public void update(Document document) {
        checkArgument(StringUtils.isNotBlank(document.getId()), "document id cannot be blank");
        String sql = "UPDATE Document SET author=:author, title=:title, content=:content, summary=:summary," +
                " group_Id=:groupId, state_=:state, created_date=:createdDate WHERE id=:id";

        BeanPropertySqlParameterSource source = new BeanPropertySqlParameterSource(document);
        int results = this.namedJdbcTemplate.update(sql, source);
        log.debug("Updated: " + results + " Documents");
    }

    @Override
    @Transactional(readOnly = true)
    public List<Document> readPage(PagingCriteria criteria) {
        log.debug("reading page with criteria: " + criteria);
        if (criteria == null || criteria.getLimit() == null || criteria.getStart() == null) {
            log.warn("criteria invalid - reading all instead of subset");
            return readAll();
        }
        String sql = "SELECT LIMIT :start :limit * FROM Document ORDER BY created_date ASC";
        BeanPropertySqlParameterSource source = new BeanPropertySqlParameterSource(criteria);

        List<Document> documents = this.namedJdbcTemplate.query(sql, source, new DocumentRowMapper());
        log.debug(documents.size() + " Documents returned using criteria: " + criteria);

        return documents;
    }
}
