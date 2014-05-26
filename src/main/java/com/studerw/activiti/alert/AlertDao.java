package com.studerw.activiti.alert;

import com.studerw.activiti.dao.IBaseDao;
import com.studerw.activiti.model.Alert;

import java.util.List;

/**
 * User: studerw
 * Date: 5/21/14
 */
public interface AlertDao extends IBaseDao<Alert> {

    /**
     * @param userId
     * @return sorted list (by createdDate) of alerts for the given user
     *         that have yet to be acknowledged
     */
    List<Alert> readActiveAlertsByUserId(String userId);

    /**
     * Sets the acknowledge flag to boolean TRUE
     * @param alertId
     */
    void acknowledgeAlert(String alertId);
}
