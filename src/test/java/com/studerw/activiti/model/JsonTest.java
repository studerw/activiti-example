package com.studerw.activiti.model;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * @author William Studer
 */
public class JsonTest {
    private static final Logger log = LogManager.getLogger(JsonTest.class);
    public final static ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testJsonToMap() throws IOException {
        String json = "{\"name\":\"foo\", \"age\":\"29\"}";
        Map<String, String> map = objectMapper.readValue(json,
                new TypeReference<HashMap<String,String>>(){});

        assertTrue(map.size() == 2);
        assertTrue(map.keySet().containsAll(Arrays.asList("name", "age")));
        log.debug(map);
    }
}
