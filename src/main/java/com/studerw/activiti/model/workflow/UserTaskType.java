package com.studerw.activiti.model.workflow;

import java.util.Arrays;
import java.util.List;

/**
 * @author William Studer
 */
public enum UserTaskType {
    COLLABORATION,
    APPROVE_REJECT;

    public static List<UserTaskType> asList(){
        return Arrays.asList(UserTaskType.values());
    }
}
