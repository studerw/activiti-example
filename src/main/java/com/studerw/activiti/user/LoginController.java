package com.studerw.activiti.user;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
public class LoginController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserService userService;

    @RequestMapping(value = "/login.htm", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String login(Model model, HttpServletRequest request) throws Exception {
        log.debug("login()");
        model.addAttribute("error", false);
        model.addAttribute("users", userWithGroups());
        return "login";
    }


    @RequestMapping(value = "/loginfailed.htm", method = RequestMethod.GET)
    public String loginFail(ModelMap model) {
        log.debug("loginFail()");
        model.addAttribute("error", true);
        model.addAttribute("users", userWithGroups());
        return "login";
    }

    protected Map<String, String> userWithGroups() {
        Map<String, String> temp = Maps.newHashMap();
        Map<String, List<String>> userGroups = userService.userWithAssignmentGroupStr();
        for (String user : userGroups.keySet()) {
            List<String> groupIds = userGroups.get(user);
            temp.put(user, Joiner.on(", ").join(groupIds));
        }
        return temp;
    }
}
