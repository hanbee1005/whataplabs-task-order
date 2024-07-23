package com.whataplabs.task.order.whataplabstaskorder.infrastructure.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class JsonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.registerModule(new JavaTimeModule());
    }

    public static String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("[JsonUtil.toJson] {}", e.getMessage());
            return StringUtils.EMPTY;
        }
    }

    public static <T> T fromJson(String json, Class<T> valueType) {
        try {
            return objectMapper.readValue(json, valueType);
        } catch (JsonProcessingException e) {
            log.error("[JsonUtil.fromJson] {}", e.getMessage());
            return null;
        }
    }
}
