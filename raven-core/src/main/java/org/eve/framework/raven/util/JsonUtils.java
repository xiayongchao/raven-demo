package org.eve.framework.raven.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * @author xiayc
 * @date 2018/7/9
 */
public class JsonUtils {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        // 允许序列化空的POJO类（否则会抛出异常）
        MAPPER.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    public static String convertToJson(Object object) throws JsonProcessingException {
        return MAPPER.writeValueAsString(object);
    }
}
