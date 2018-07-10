package org.eve.framework.raven.util;

import java.util.Collection;

/**
 * @author yc_xia
 * @date 2018/6/1
 */
public class AssertUtils {
    public static void is(boolean b, String message) {
        if (!b) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void not(boolean b, String message) {
        if (!b) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isNull(Object o, String message) {
        if (VerifyUtils.notNull(o)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notNull(Object o, String message) {
        if (VerifyUtils.isNull(o)) {
            throw new IllegalArgumentException(message);
        }

    }

    /*public static void hasText(CharSequence str, String message) {
        if (!VerifyUtils.hasLength(str)) {
            throw new IllegalArgumentException(message);
        }
    }*/


    public static void notEmpty(CharSequence str, String message) {
        if (VerifyUtils.isEmpty(str)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void noNullElements(Collection<?> collection, String message) {
        if (!VerifyUtils.noNullElements(collection)) {
            throw new IllegalArgumentException(message);
        }
    }


    public static void isEmpty(Collection<?> collection, String message) {
        if (VerifyUtils.notEmpty(collection)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(Collection<?> collection, String message) {
        if (VerifyUtils.isEmpty(collection)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isPort(Integer port, String message) {
        if (!VerifyUtils.isPort(port)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isIpAddress(String ipAddress, String message) {
        if (!VerifyUtils.isIpAddress(ipAddress)) {
            throw new IllegalArgumentException(message);
        }
    }

}
