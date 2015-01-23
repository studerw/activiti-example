package com.studerw.activiti.document.dao;

import com.studerw.activiti.dao.IBaseDao;
import com.studerw.activiti.model.document.Invoice;
import com.studerw.activiti.web.PagingCriteria;

import java.util.List;

/**
 * @author William Studer
 * Date: 5/20/14
 */
public interface InvoiceDao extends IBaseDao<Invoice> {

    String create(Invoice Invoice);

    void createWithId(Invoice obj);

    Invoice read(String InvoiceId);

    List<Invoice> readAll();

    int getCount();

    void delete(String InvoiceId);

    void update(Invoice Invoice);

    List<Invoice> readPage(PagingCriteria criteria);
}
