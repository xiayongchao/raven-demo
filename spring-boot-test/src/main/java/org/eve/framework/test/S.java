package org.eve.framework.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.eve.framework.raven.util.JsonUtils;

/**
 * @author xiayc
 * @date 2018/7/10
 */
public class S extends P {
    private String b;

    public String getB() {
        return b;
    }

     void setB(String b) {
        this.b = b;
    }

    public static void main(String[] args) throws JsonProcessingException {
        System.out.println(JsonUtils.convertToJson(new S()));
    }
}
