package com.studerw.activiti.model;

/**
 * @author studerw
 */
public enum DocType {
    BOOK_REPORT("Book Report"),
    INVOICE("Invoice")
    ;

    private final String text;

    /**
     * @param text
     */
    private DocType(final String text) {
        this.text = text;
    }

    /** (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return text;
    }

}
