package com.studerw.activiti.document;

import com.studerw.activiti.dao.IBaseDao;
import com.studerw.activiti.model.Document;
import com.studerw.activiti.web.PagingCriteria;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * User: studerw
 * Date: 5/20/14
 */
public interface DocumentDao extends IBaseDao<Document> {
    @Override
    @Transactional
    String create(Document Document);

    @Override
    void createWithId(Document obj);

    @Override
    @Transactional(readOnly = true)
    Document read(String DocumentId);

    @Override
    @Transactional(readOnly = true)
    List<Document> readAll();

    @Override
    @Transactional(readOnly = true)
    int getCount();

    @Override
    @Transactional
    void delete(String DocumentId);

    @Override
    @Transactional
    void update(Document Document);

    @Override
    List<Document> readPage(PagingCriteria criteria);
}
