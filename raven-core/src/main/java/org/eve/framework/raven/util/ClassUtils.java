package org.eve.framework.raven.util;

/**
 * @author xiayc
 * @date 2018/7/10
 */
public class ClassUtils {
    public static final boolean isPresent(String className) {
        try {
            return null != Class.forName(className);
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static final Object newInstance(String className) {
        try {
            Class<?> aClass = Class.forName(className);
            return aClass.newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            return null;
        }
    }
}
