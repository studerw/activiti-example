package com.studerw.activiti.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studerw.activiti.model.workflow.DynamicUserTaskType;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author William Studer
 */
public class JsonTest {
    private static final Logger LOG = LoggerFactory.getLogger(JsonTest.class);
    public final static ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testJsonToMap() throws IOException {
        String json = "{\"name\":\"foo\", \"age\":\"29\", \"group\": \"some weired group_blah\"}";
        Map<String, String> map = objectMapper.readValue(json,
                new TypeReference<HashMap<String, String>>() {
                });

        assertTrue(map.size() == 3);
        assertTrue(map.keySet().containsAll(Arrays.asList("name", "age", "group")));
        LOG.debug("{}",map);
    }

    @Test
    public void testTaskTypestoJson() throws JsonProcessingException {
        List<DynamicUserTaskType> taskTypeList = DynamicUserTaskType.asList();
        String json = objectMapper.writeValueAsString(taskTypeList);
        LOG.debug(json);
    }

    @Test(expected = UnsupportedCharsetException.class)
    public void testUtf() {
        Charset charset = Charset.forName("UTF-8");
        LOG.debug(charset.name());

        charset = Charset.forName("utf-8");
        LOG.debug(charset.name());

        charset = Charset.forName("utf8");
        LOG.debug(charset.name());

        charset = Charset.forName("foo");
        fail("should have thrown exception here");


    }
}
