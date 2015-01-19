package com.studerw.activiti.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

/**
 * @author studerw
 */
public class DocTypeTest {
    private static final Logger log = LogManager.getLogger(DocTypeTest.class);
    @Test
    public void testValueOf(){
        DocType type = DocType.BOOK_REPORT;
        log.debug("name: {}", type.name());
        log.debug("ordinal: {}", type.ordinal());
        DocType docType = DocType.valueOf("BOOK_REPORT");
        log.debug("docType: {}", docType.toString());
    }
}
