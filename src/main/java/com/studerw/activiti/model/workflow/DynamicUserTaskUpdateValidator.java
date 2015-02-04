package com.studerw.activiti.model.workflow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * @author William Studer
 */
public class DynamicUserTaskUpdateValidator implements Validator {
    private static final Logger LOG = LoggerFactory.getLogger(DynamicUserTaskUpdateValidator.class);

    @Override public boolean supports(Class<?> clazz) {
        return DynamicUserTask.class.isAssignableFrom(clazz);
    }

    @Override public void validate(Object target, Errors errors) {
        DynamicUserTask dynamicUserTask= (DynamicUserTask)target;
        LOG.debug("Validating update dynamicUserTask: {}", dynamicUserTask);

        if (dynamicUserTask.getCandidateGroups().isEmpty() && dynamicUserTask.getCandidateUsers().isEmpty()){
            errors.reject("dynamicUserTask.noCandidates");
        }

    }
}
