package com.studerw.activiti.web;

import com.google.common.collect.Maps;
import com.studerw.activiti.model.Response;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * Annotated ControllerAdvice bean that globally configures all other controllers with exception handling, bean binding, etc.
 *
 * @author Bill Studer
 * @see <a href="http://static.springsource.org/spring/docs/3.0.x/reference/mvc.html">Spring 3.x MVC Reference</a>
 */
@ControllerAdvice
public class DefaultControllerAdvice {

    private static final Logger log = LoggerFactory.getLogger(DefaultControllerAdvice.class);

//
//    @ExceptionHandler(InvalidAccessException.class)
//    @ResponseStatus(HttpStatus.FORBIDDEN)
//    public void handlePermissionException(InvalidAccessException ex) {
//        log.warn(ex.getMessage());
//    }


    /**
     * With Ajax calls we need to send a 200 OK response with a status of success: false.
     */
    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception ex) {
        log.error("Caught Exception - returning error response: {}", ex.getMessage());
        log.error("Root cause: {}", ExceptionUtils.getRootCauseMessage(ex));
        ex.printStackTrace();
        Map<String, Object> model = Maps.newHashMap();
        Response response = new Response(false, ex.getMessage() + "    Root Cause: " + ExceptionUtils.getRootCauseMessage(ex));
        model.put("response", response);
        return new ModelAndView("error", model);
        //return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
