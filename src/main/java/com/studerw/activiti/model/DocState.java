package com.studerw.activiti.model;

/**
 * @author studerw
 */
public enum DocState {
    DRAFT("Draft"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    WAITING_FOR_APPROVAL("Waiting for Approval"),
    WAITING_FOR_COLLABORATION("Waiting for Collaboration"),
    PUBLISHED("Published");

    private final String text;

    /**
     * @param text
     */
    private DocState(final String text) {
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
