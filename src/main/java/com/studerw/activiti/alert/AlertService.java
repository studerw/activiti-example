package com.studerw.activiti.alert;

import com.studerw.activiti.model.Alert;
import com.studerw.activiti.user.InvalidAccessException;
import com.studerw.activiti.user.UserService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * User: studerw
 * Date: 5/21/14
 */
@Service("alertService")
public class AlertService {
    protected IdentityService identityService;
    protected RuntimeService runtimeService;
    protected TaskService taskService;
    protected UserService userService;
    protected AlertDao alertDao;

    @Autowired
    public void setIdentityService(IdentityService identityService) {
        this.identityService = identityService;
    }

    @Autowired
    public void setRuntimeService(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    @Autowired
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setAlertDao(AlertDao alertDao) {
        this.alertDao = alertDao;
    }

    public void sendAlert(String to, int priority, String message){
        Alert alert = new Alert();
        UserDetails from = this.userService.currentUser();
        alert.setCreatedBy(from.getUsername());
        alert.setPriority(priority);
        alert.setCreatedDate(new Date());
        alert.setUserId(to);
        alert.setAcknowledged(Boolean.FALSE);
    }

    public List<Alert> readActiveAlertsByUser(String userId){
        UserDetails user = this.userService.currentUser();
        if (StringUtils.equals(user.getUsername(), userId)){
            throw new InvalidAccessException("Alerts may only be accessed by the alert recipient itself");
        }
        return this.alertDao.readActiveAlertsByUserId(userId);
    }
}
