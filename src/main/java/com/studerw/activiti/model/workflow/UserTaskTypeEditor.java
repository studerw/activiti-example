package com.studerw.activiti.model.workflow;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyEditorSupport;

/**
 * @author William Studer
 */
public class UserTaskTypeEditor extends PropertyEditorSupport {

    private static final Logger log = LoggerFactory.getLogger(UserTaskTypeEditor.class);

    @Override
    public String getAsText() {
        UserTaskType userTaskType = (UserTaskType) this.getValue();
        if (userTaskType == null) {
            return null;
        }
        log.trace("Converted UserTaskType: {}", userTaskType.name());
        return userTaskType.toString();
    }

    @Override
    public void setAsText(String s) {
        if (StringUtils.isBlank(s)) {
            setValue(null);
            return;
        }
        UserTaskType userTaskType = UserTaskType.valueOf(s);
        log.trace("Converted UserTaskType: {} ---> {}", s, userTaskType.name());
        setValue(userTaskType);
    }

}
