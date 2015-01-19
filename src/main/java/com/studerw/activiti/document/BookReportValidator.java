package com.studerw.activiti.document;

import com.studerw.activiti.model.BookReport;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * @author studerw
 */
public class BookReportValidator implements Validator {
    private static final Logger log = LoggerFactory.getLogger(BookReportValidator.class);

    @Override public boolean supports(Class<?> clazz) {
        return BookReport.class.isAssignableFrom(clazz);
    }

    @Override public void validate(Object target, Errors errors) {
        BookReport bookReport = (BookReport)target;
        log.debug("Validating book report: {}", bookReport);

        ValidationUtils.rejectIfEmpty(errors, "author", "required");
        ValidationUtils.rejectIfEmpty(errors, "groupId", "required");
        ValidationUtils.rejectIfEmpty(errors, "createdDate", "required");
        ValidationUtils.rejectIfEmpty(errors, "docState", "required");
        ValidationUtils.rejectIfEmpty(errors, "docType", "required");
        ValidationUtils.rejectIfEmpty(errors, "title", "required");
        ValidationUtils.rejectIfEmpty(errors, "bookTitle", "bookTitle");
        ValidationUtils.rejectIfEmpty(errors, "summary", "required");
        ValidationUtils.rejectIfEmpty(errors, "content", "required");
        ValidationUtils.rejectIfEmpty(errors, "bookAuthor", "required");

    }
}
