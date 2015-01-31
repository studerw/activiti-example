package com.studerw.activiti.model.workflow;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyEditorSupport;

/**
 * @author William Studer
 */
public class DynamicUserTaskTypeEditor extends PropertyEditorSupport {

    private static final Logger LOG = LoggerFactory.getLogger(DynamicUserTaskTypeEditor.class);

    @Override
    public String getAsText() {
        DynamicUserTaskType dynamicUserTaskType = (DynamicUserTaskType) this.getValue();
        if (dynamicUserTaskType == null) {
            return null;
        }
        LOG.trace("Converted UserTaskType: {}", dynamicUserTaskType.name());
        return dynamicUserTaskType.toString();
    }

    @Override
    public void setAsText(String s) {
        if (StringUtils.isBlank(s)) {
            setValue(null);
            return;
        }
        DynamicUserTaskType dynamicUserTaskType = DynamicUserTaskType.valueOf(s);
        LOG.trace("Converted UserTaskType: {} ---> {}", s, dynamicUserTaskType.name());
        setValue(dynamicUserTaskType);
    }

}
