package com.studerw.activiti.model.document;

/**
 * Represents the acceptable document types. These are also the only acceptable {@code Category} or namespace
 * values in BPMN 2.0 XML and Models. This is because, by default, documents are associated with a workflow (and
 * possibly group) by the namespace element (or category in Activiti's query system).
 * @author William Studer
 */
public enum DocState {
    DRAFT,
    APPROVED,
    REJECTED,
    WAITING_FOR_APPROVAL,
    WAITING_FOR_COLLABORATION,
    COLLABORATED,
    PUBLISHED,
    EMAILED;
}