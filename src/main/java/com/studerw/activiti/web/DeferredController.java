package com.studerw.activiti.web;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class DeferredController extends BaseController {
    private static final Logger LOG = LoggerFactory.getLogger(DeferredController.class);


    @RequestMapping(value = "/deferred/capitalize", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Map<String, String> capitalize(@RequestParam("arg") String arg, HttpServletRequest request) throws InterruptedException {
        LOG.debug("got arg of: {}", arg);
        Thread.sleep(RandomUtils.nextInt(10, 5000));
        Map result = Maps.newHashMap();
        result.put("result", StringUtils.upperCase(arg));
        return result;
    }


    @RequestMapping(value = "/deferred.htm", method = RequestMethod.GET)
    public String deferred(ModelMap model, HttpServletRequest request) {
        return "deferred";
    }

}

