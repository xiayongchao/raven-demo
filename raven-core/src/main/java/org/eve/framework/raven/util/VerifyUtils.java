package org.eve.framework.raven.util;

import java.util.Collection;
import java.util.Map;

/**
 * @author yc_xia
 */
public class VerifyUtils {
    public static boolean isNull(Object o) {
        return o == null;
    }

    public static boolean notNull(Object o) {
        return !isNull(o);
    }

    public static boolean isEmpty(CharSequence str) {
        return !notEmpty(str);
    }

    public static boolean notEmpty(CharSequence str) {
        return (notNull(str) && str.length() > 0);
    }

    public static boolean notBlank(CharSequence str) {
        if (!notEmpty(str)) {
            return false;
        }
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public static boolean isBlank(CharSequence str) {
        return !notBlank(str);
    }

    public static boolean isEmpty(Object[] objects) {
        return isNull(objects) ? true : objects.length == 0;
    }

    public static boolean notEmpty(Object[] objects) {
        return !isEmpty(objects);
    }

    public static boolean noNullElements(Collection<?> collection) {
        if (isEmpty(collection)) {
            return false;
        }
        for (Object o : collection) {
            if (o == null) {
                return false;
            }
        }
        return true;
    }

    public static boolean isEmpty(Collection<?> collection) {
        return isNull(collection) ? true : collection.isEmpty();
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return isNull(map) ? true : map.isEmpty();
    }

    public static boolean notEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    public static boolean notEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    public static boolean isPort(Integer port) {
        if (isNull(port) || port < 1 || port > 65535) {
            return false;
        }
        return true;
    }

    public static boolean isIpAddress(String ipAddress) {
        if (!notBlank(ipAddress)) {
            return false;
        }
        // 定义正则表达式
        String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                + "(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
        // 判断ip地址是否与正则表达式匹配
        return ipAddress.matches(regex);
    }
}
