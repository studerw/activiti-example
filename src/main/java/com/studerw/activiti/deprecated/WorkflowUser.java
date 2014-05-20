//package com.itc.activiti.model;
//
//
//import com.google.common.collect.Lists;
//import gov.dia.chrome.common.model.ChromeUser;
//import org.codehaus.jackson.annotate.JsonIgnore;
//import org.codehaus.jackson.annotate.JsonIgnoreProperties;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//@JsonIgnoreProperties(ignoreUnknown=true)
//public class WorkflowUser extends ChromeUser implements org.activiti.engine.identity.User {
//    private static final Logger log = LoggerFactory.getLogger(WorkflowUser.class);
//
//    public WorkflowUser(){}
//
//    @Override
//    @JsonIgnore
//    public void setEmail(String s) {
//        log.warn("Ignoring email: " + s);
//    }
//
//    @Override
//    @JsonIgnore
//    public String getEmail() {
//        log.warn("shouldn't be calling getEmail()");
//        return null;
//    }
//
//    @Override
//    @JsonIgnore
//    public String getPassword() {
//        log.warn("shouldn't be calling getPassword()");
//        return null;
//    }
//
//    @Override
//    @JsonIgnore
//    public void setPassword(String s) {
//        log.warn("Ignoring setting password");
//    }
//}
//
