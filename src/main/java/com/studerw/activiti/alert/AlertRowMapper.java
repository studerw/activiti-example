package com.studerw.activiti.alert;

import com.studerw.activiti.model.Alert;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author studerw
 */
public class AlertRowMapper implements RowMapper<Alert> {

    @Override
    public Alert mapRow(final ResultSet rs, final int rowNum) throws SQLException {
        String id = StringUtils.trim(rs.getString("id"));
        String createdBy = StringUtils.trim(rs.getString("created_by"));
        String message = StringUtils.trim(rs.getString("message"));
        String userId = StringUtils.trim(rs.getString("user_id"));
        Integer priority = rs.getInt("priority");
        Boolean acknowledged = rs.getBoolean("acknowledged");
        Date createdDate = rs.getDate("created_date");

        return new Alert(id, createdBy, userId, message, priority, createdDate, acknowledged);
    }
}

