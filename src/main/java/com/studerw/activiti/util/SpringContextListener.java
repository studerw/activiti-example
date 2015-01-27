package com.studerw.activiti.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Helper Bean to display the loaded Spring Contexts. Debug pages
 * can call it to obtain a list of beans per context. The hope
 * is that this will help developers to avoid loading duplicates.
 * Date: 8/7/14
 *
 * @author William Studer
 */
@Component
public class SpringContextListener implements ApplicationListener<ApplicationEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(SpringContextListener.class);
    Map<String, List<String>> appContexts = new ConcurrentHashMap<String, List<String>>();

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            doRefresh(event);
        } else if (event instanceof ContextStartedEvent) {
            ApplicationContext appContext = ((ContextStartedEvent) event).getApplicationContext();
            LOG.debug("**********************STARTED Context [{}] ***********************************", appContext.getDisplayName());

        } else if (event instanceof ContextStoppedEvent) {
            ApplicationContext appContext = ((ContextStoppedEvent) event).getApplicationContext();
            LOG.debug("**********************STOPPED Context [{}] ***********************************", appContext.getDisplayName());

        }
    }

    protected void doRefresh(ApplicationEvent event) {
        ApplicationContext appContext = ((ContextRefreshedEvent) event).getApplicationContext();
        ApplicationContext parent = appContext.getParent();
        String parentName = parent == null ? "[none]" : parent.getApplicationName();
        LOG.debug("**********************REFRESHED Context [{}] of parent [{}] ***********************************", appContext.getDisplayName(), parent);
        LOG.info("AppContext {}: beans({})", appContext.getId(), appContext.getBeanDefinitionCount());
        List beans = new ArrayList<String>();
        for (String bean : appContext.getBeanDefinitionNames()) {
            try {
                Object obj = appContext.getBean(bean);
//                if (!obj.getClass().getName().startsWith("org.springframework")) {
                    LOG.trace("bean: {} ", bean);
                    beans.add(bean);
//                }
            } catch (Exception e) {
                // TODO log and continue for now
                LOG.error("Error getting bean " + bean, e);
            }
        }
        appContexts.put(appContext.getDisplayName(), beans);
        LOG.debug("******");
    }

    public Map<String, List<String>> getAppContexts() {
        return Collections.unmodifiableMap(appContexts);
    }
}
