package com.studerw.activiti.model;

import com.studerw.activiti.model.document.BookReport;
import com.studerw.activiti.model.document.Invoice;
import com.studerw.activiti.model.task.TaskApprovalForm;
import com.studerw.activiti.model.task.TaskCollaborationForm;


import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author William Studer
 */
public class ValidatorTest {
    private static final Logger LOG = LoggerFactory.getLogger(ValidatorTest.class);
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = null;
    @Before
    public void before(){
        this.validator = factory.getValidator();
        assertNotNull(validator);
    }

    @Test
    public void testValidateBookReport(){
        BookReport bookReport = new BookReport();
        Set<ConstraintViolation<BookReport>> violations = validator.validate(bookReport);
        LOG.debug("Empty book report violations: {}", violations.size());
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testValidateInvoice(){
        Invoice invloice = new Invoice();
        Set<ConstraintViolation<Invoice>> violations = validator.validate(invloice);
        LOG.debug("Empty invoice violations: {}", violations.size());
        assertFalse(violations.isEmpty());
    }

    @Test
    public void testValidateCollabTask(){
        TaskCollaborationForm form = new TaskCollaborationForm();
        Set<ConstraintViolation<TaskCollaborationForm>> violations = validator.validate(form);
        LOG.debug("violations: {}", violations.size());
        assertTrue(violations.size() == 2);

        form.setComment("foo");
        violations = validator.validate(form);
        LOG.debug("violations: {}", violations.size());
        assertTrue(violations.size() == 1);

        form.setTaskId("abc");
        violations = validator.validate(form);
        LOG.debug("violations: {}", violations.size());
        assertTrue(violations.size() == 0);

    }

    @Test
    public void testValidateApprovalTask(){
        TaskApprovalForm form = new TaskApprovalForm();
        Set<ConstraintViolation<TaskApprovalForm>> violations = validator.validate(form);
        LOG.debug("violations: {}", violations.size());
        assertTrue(violations.size() == 3);

        form.setComment("foo");
        form.setTaskId("abc");
        violations = validator.validate(form);
        LOG.debug("violations: {}", violations.size());
        assertTrue(violations.size() == 1);

        form.setApproved(Boolean.FALSE);
        violations = validator.validate(form);
        LOG.debug("violations: {}", violations.size());
        assertTrue(violations.size() == 0);


    }

}
