package com.studerw.activiti.user;

import com.google.common.base.Strings;
import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Deprecated
public class UserSessionInterceptor implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(UserSessionInterceptor.class);
    public final static String SESSION_USER_KEY = "CHROME_SESSION_USER";
    public final static String PARAMETER_USER_KEY = "currentUser";


    @Autowired
    IdentityService identityService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = (User) WebUtils.getSessionAttribute(request, SESSION_USER_KEY);
        if (user != null) {
            return true;
        }
        Object obj = WebUtils.getSessionMutex(request.getSession());
        synchronized (obj) {
            log.debug("WorkflowUser not found in session under key: " + SESSION_USER_KEY);
            String currentUser = request.getParameter(PARAMETER_USER_KEY);
            if (Strings.isNullOrEmpty(currentUser)) {
                throw new InvalidAccessException("No user found");
            }
            user = identityService.createUserQuery().userId(currentUser).singleResult();
            if (user == null) {
                throw new InvalidAccessException("WorkflowUser: " + currentUser + " is unknown");
            }
            WebUtils.setSessionAttribute(request, SESSION_USER_KEY, user);
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {
        ;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
        ;
    }

}
