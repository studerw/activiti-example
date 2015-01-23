package com.studerw.activiti.model.document;

import java.util.Arrays;
import java.util.List;

/**
 * @author William Studer
 */
public enum DocType {
    BOOK_REPORT,
    INVOICE,
    RECEIPT,
    GENERAL,
    UNIT_TEST;

    public static List<DocType> asList(){
        return Arrays.asList(DocType.values());
    }
}
