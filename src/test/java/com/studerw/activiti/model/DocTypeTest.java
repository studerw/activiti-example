package com.studerw.activiti.model;

import com.studerw.activiti.model.document.DocType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.springframework.util.Assert;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * @author William Studer
 */
public class DocTypeTest {
    private static final Logger LOG = LogManager.getLogger(DocTypeTest.class);

    @Test(expected = IllegalArgumentException.class)
    public void testValueOf(){
        DocType type = DocType.BOOK_REPORT;
        LOG.debug("name: {}", type.name());
        LOG.debug("ordinal: {}", type.ordinal());
        DocType docType = DocType.valueOf("BOOK_REPORT");
        LOG.debug("docType: {}", docType.toString());

        type = DocType.valueOf("FOO");
        fail("Should have exception on illegal DocType: FOO");

    }

    @Test
    public void testParseDocTypeAndGroup() {
        String procId = "BOOK_REPORT_NONE";
        int index = procId.lastIndexOf("_");
        if (index < 0) {
            throw new IllegalArgumentException("Invalid process id: " + procId);
        }
        String docType = procId.substring(0, index);
        String group = procId.substring(index + 1, procId.length());
        LOG.debug("{} - {}", docType, group);
        Assert.hasText(docType);
        Assert.hasText(group);
    }
}
