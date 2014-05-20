//package com.itc.activiti.user;
//
//import com.google.common.collect.Lists;
//import com.studerw.activiti.model.WorkflowUser;
//import gov.dia.chrome.common.service.GroupManagementService;
//import gov.dia.chrome.common.service.UserManagementService;
//import org.activiti.engine.IdentityService;
//import org.activiti.engine.identity.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service("defaultIdentityService")
//public class DefaultIdentityService implements IdentityService {
//
//    private List<WorkflowUser> users;
//
//    @Autowired
//    GroupManagementService groupManagementService;
//    @Autowired
//    UserManagementService userManagementService;
//    public DefaultIdentityService(){
//    }
//
//    @Override
//    public org.activiti.engine.identity.User newUser(String s) {
//        return null;  //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    @Override
//    public void saveUser(org.activiti.engine.identity.User user) {
//        //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    @Override
//    public UserQuery createUserQuery() {
//        return null;  //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    @Override
//    public NativeUserQuery createNativeUserQuery() {
//        return null;  //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    @Override
//    public void deleteUser(String s) {
//        //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    @Override
//    public Group newGroup(String s) {
//        return null;  //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    @Override
//    public GroupQuery createGroupQuery() {
//        return null;  //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    @Override
//    public NativeGroupQuery createNativeGroupQuery() {
//        return null;  //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    @Override
//    public void saveGroup(Group group) {
//        //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    @Override
//    public void deleteGroup(String s) {
//        //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    @Override
//    public void createMembership(String s, String s2) {
//        //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    @Override
//    public void deleteMembership(String s, String s2) {
//        //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    @Override
//    public boolean checkPassword(String s, String s2) {
//        return false;  //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    @Override
//    public void setAuthenticatedUserId(String s) {
//        //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    @Override
//    public void setUserPicture(String s, Picture picture) {
//        //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    @Override
//    public Picture getUserPicture(String s) {
//        return null;  //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    @Override
//    public void setUserInfo(String s, String s2, String s3) {
//        //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    @Override
//    public String getUserInfo(String s, String s2) {
//        return null;  //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    @Override
//    public List<String> getUserInfoKeys(String s) {
//        return null;  //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    @Override
//    public void deleteUserInfo(String s, String s2) {
//        //To change body of implemented methods use File | Settings | File Templates.
//    }
//}
