package com.studerw.activiti.user;

/**
 * Exception used to explicitly tag failures due to invalid user permissions
 * (i.e. attempts at reading, updating objects without proper permission, etc).
 * WorkflowUser: Bill Studer
 * Date: 2/28/14
 */
public class InvalidAccessException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InvalidAccessException(String message) {
        super(message);
    }

    public InvalidAccessException(String message, Throwable t) {
        super(message, t);
    }

}
