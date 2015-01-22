package com.studerw.activiti.model;

import java.util.Arrays;
import java.util.List;

/**
 * @author studerw
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
