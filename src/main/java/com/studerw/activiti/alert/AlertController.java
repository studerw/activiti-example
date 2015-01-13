package com.studerw.activiti.alert;

import com.studerw.activiti.model.Response;
import com.studerw.activiti.web.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AlertController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(AlertController.class);

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @RequestMapping(value = "/alerts.htm", method = RequestMethod.GET)
    public String getAlerts(ModelMap model, HttpServletRequest request) {
        return "alerts";
    }

    @RequestMapping(value = "/alerts/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<Response> acknowledgeAlert(
                        @PathVariable("id") String alertId,
                        HttpServletRequest request) {
            log.debug("acknowledging alert {}", alertId);
            this.alertService.acknowledgeAlert(alertId, this.currentUserName());
            Response res = new Response(true, "Alert acknowledged");
            return new ResponseEntity<Response>(res, HttpStatus.OK);
        }
}
