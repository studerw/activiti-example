package com.studerw.activiti.model;

/**
 * @author studerw
 */
public enum DocState {
    DRAFT,
    APPROVED,
    REJECTED,
    WAITING_FOR_APPROVAL,
    WAITING_FOR_COLLABORATION,
    COLLABORATED,
    PUBLISHED;
}