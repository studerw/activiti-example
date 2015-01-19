package com.studerw.activiti.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Set;

import static org.junit.Assert.assertNotNull;

/**
 * @author studerw
 */
public class ValidatorTest {
    private static final Logger log = LogManager.getLogger(ValidatorTest.class);
    @Test
    public void testValidateBookReport(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        assertNotNull(validator);

        BookReport bookReport = new BookReport();
        Set<ConstraintViolation<BookReport>> violations = validator.validate(bookReport);
//          Document doc = new Document();
//        Set<ConstraintViolation<BookReport>> violations = validator.validate(bookReport);
        log.debug(violations.size());
        //assertEquals(violations.size(), 5);
    }
}
