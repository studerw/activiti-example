package com.studerw.activiti.alert;

import com.studerw.activiti.model.Alert;
import com.studerw.activiti.user.InvalidAccessException;
import com.studerw.activiti.user.UserService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(AlertService.class);

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

    /**
     * Send an alert using the general <em>System User</em> as the sender
     * @param to
     * @param priority
     * @param message
     * @return
     */
    public String sendSystemAlert(String to, int priority, String message){
        log.debug("sending system alert to: {} at priority {}", to, priority);
        Alert alert = new Alert();
        alert.setCreatedBy(UserService.SYSTEM_USER);
        alert.setPriority(priority);
        alert.setCreatedDate(new Date());
        alert.setUserId(to);
        alert.setAcknowledged(Boolean.FALSE);
        alert.setMessage(message);

        return this.alertDao.create(alert);

    }

    /**
     * Send an alert using the current logged in user as the sender
     * @param to
     * @param priority
     * @param message
     * @return
     */
    public String sendAlert(String to, int priority, String message) {
        log.debug("sending alert to: {} at priority {}", to, priority);
        Alert alert = new Alert();
        UserDetails from = this.userService.currentUser();
        alert.setCreatedBy(from.getUsername());
        alert.setPriority(priority);
        alert.setCreatedDate(new Date());
        alert.setUserId(to);
        alert.setAcknowledged(Boolean.FALSE);
        alert.setMessage(message);

        return this.alertDao.create(alert);
    }

    public void acknowledgeAlert(String alertId, String userId) {
        log.debug("acknowledging alert {} for user {}", alertId, userId);
        Alert alert = this.alertDao.read(alertId);
        if (!StringUtils.equals(userId, alert.getUserId())) {
            throw new InvalidAccessException("Only the alert owner may acknowledge an alert");
        }
        alert.setAcknowledged(Boolean.TRUE);
        this.alertDao.update(alert);
    }

    /**
     *
     * @param userId
     * @return a list of alerts, sorted ascending by creation date, for a given user.
     * Only the actual user him/herself can obtain his/her own alerts
     */
    public List<Alert> readActiveAlertsByUser(String userId) {
        log.debug("reading alerts for user: {}", userId);
        UserDetails user = this.userService.currentUser();
        if (!StringUtils.equals(user.getUsername(), userId)) {
            throw new InvalidAccessException("Alerts may only be accessed by the alert recipient itself");
        }
        return this.alertDao.readActiveAlertsByUserId(userId);
    }
}
