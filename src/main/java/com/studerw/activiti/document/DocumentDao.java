package com.studerw.activiti.document;

import com.studerw.activiti.dao.IBaseDao;
import com.studerw.activiti.model.Document;
import com.studerw.activiti.web.PagingCriteria;

import java.util.List;

/**
 * User: studerw
 * Date: 5/20/14
 */
public interface DocumentDao extends IBaseDao<Document> {

    String create(Document Document);

    void createWithId(Document obj);

    Document read(String DocumentId);

    List<Document> readAll();

    int getCount();

    void delete(String DocumentId);

    void update(Document Document);

    List<Document> readPage(PagingCriteria criteria);
}
