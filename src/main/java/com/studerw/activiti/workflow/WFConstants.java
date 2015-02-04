package com.studerw.activiti.workflow;

import com.studerw.activiti.model.document.DocType;
import org.springframework.util.Assert;

/**
 * @author William Studer
 * Date: 5/24/14
 */
public class WFConstants {
    public static final String NAMESPACE_CATEGORY = "studerw.com";
    public static final String PROCESS_GROUP_DIVIDER = "___";
    public static final String WORKFLOW_GROUP_NONE = "NONE";
    public static final String SUBPROCESS_ID_DYNAMIC = "DYNAMIC_SUB_PROCESS";
    public static final String SUBPROCESS_NAME_DYNAMIC = "Dynamic Document Subprocess";
    public static final String TASK_ID_DOC_APPROVAL = "APPROVE_REJECT_DOC_USER_TASK";
    public static final String TASK_ID_DOC_COLLABORATE = "COLLABORATE_DOC_USER_TASK";
    public static final String REJECTED_BOUNDARY_EVENT_ID = "REJECTED_ERROR_BOUNDARY_EVENT";
    public static final String ERROR_DOC_REJECTED = "ERROR_DOC_REJECTED";

    public static final String TASK_VAR_OUTCOME = "taskOutcome";
    public static final String PROCESS_VAR_APPROVED_OR_REJECTED = "approved";

    public static final String PROCESS_ID_USER_APPROVAL = "NEW_USER";
    public static final String PROCESS_NAME_USER_APPROVAL = "Approve New User";
    public static final String TASK_ID_USER_APPROVAL = "approveUserTask";
    public static final String TASK_NAME_USER_APPROVAL = "Approve New User Account";

    /**
     * return the process id of a of a Doc and Group
     * @param docType
     * @param group
     * @return
     */
    public static String createProcId(DocType docType, String group){
        Assert.notNull(docType);
        Assert.hasText(group);
        return String.format("%s%s%s", docType.name(), PROCESS_GROUP_DIVIDER, group);
    }

}
