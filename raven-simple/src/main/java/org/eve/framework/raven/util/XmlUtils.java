package org.eve.framework.raven.util;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.slf4j.helpers.Util;

/**
 * @author yc_xia
 */
public class XmlUtils {
    public static Boolean getBooleanValue(Element element, String name) {
        return getBooleanValue(element, name, null);
    }

    public static Boolean getBooleanValue(Element element, String name, Boolean defaultValue) {
        Element booleanElement = element.element(name);
        if (booleanElement == null) {
            return defaultValue;
        }
        String text = booleanElement.getTextTrim();
        return "true".equalsIgnoreCase(text) ? true : ("false".equalsIgnoreCase(text) ? false : defaultValue);
    }

    public static String getStringValue(Element element, String name) {
        return getStringValue(element, name, null);
    }

    public static String getStringValue(Element element, String name, String defaultValue) {
        Element stringElement = element.element(name);
        if (stringElement == null) {
            return defaultValue;
        }
        String text = stringElement.getTextTrim();
        return text == null || text.isEmpty() ? defaultValue : text;
    }

    public static Integer getIntegerValue(Element element, String name) {
        return getIntegerValue(element, name, null);
    }

    public static Integer getIntegerValue(Element element, String name, Integer defaultValue) {
        Element intElement = element.element(name);
        if (intElement == null) {
            return defaultValue;
        }
        String text = intElement.getTextTrim();
        if (text == null || text.isEmpty()) {
            return defaultValue;
        }

        try {
            return Integer.valueOf(text);
        } catch (NumberFormatException e) {
            Util.report("get [" + name + "] value failure.return default value [" + defaultValue + "]", e);
            return defaultValue;
        }
    }

    public static Integer getIntegerText(Element intElement) {
        return getIntegerText(intElement, null);
    }

    public static Integer getIntegerText(Element intElement, Integer defaultValue) {
        String text = intElement.getTextTrim();
        if (text == null || text.isEmpty()) {
            return defaultValue;
        }

        try {
            return Integer.valueOf(text);
        } catch (NumberFormatException e) {
            Util.report("get [" + intElement.getName() + "] value failure.return default value [" + defaultValue + "]", e);
            return defaultValue;
        }
    }

    public static Long getLongValue(Element element, String name) {
        return getLongValue(element, name, null);
    }

    public static Long getLongValue(Element element, String name, Long defaultValue) {
        Element longElement = element.element(name);
        if (longElement == null) {
            return defaultValue;
        }
        String text = longElement.getTextTrim();
        if (text == null || text.isEmpty()) {
            return defaultValue;
        }

        try {
            return Long.valueOf(text);
        } catch (NumberFormatException e) {
            Util.report("get [" + name + "] value failure.return default value [" + defaultValue + "]", e);
            return defaultValue;
        }
    }

    public static Boolean getBooleanAttribute(Element element, String name) {
        return getBooleanAttribute(element, name, null);
    }

    public static Boolean getBooleanAttribute(Element element, String name, Boolean defaultValue) {
        Attribute booleanAttribute = element.attribute(name);
        if (booleanAttribute == null) {
            return defaultValue;
        }
        String text = booleanAttribute.getText();
        text = VerifyUtils.notEmpty(text) ? text.trim() : text;
        return "true".equalsIgnoreCase(text) ? true : ("false".equalsIgnoreCase(text) ? false : defaultValue);
    }

    public static String getStringAttribute(Element element, String name) {
        return getStringAttribute(element, name, null);
    }

    public static String getStringAttribute(Element element, String name, String defaultValue) {
        Attribute stringAttribute = element.attribute(name);
        if (stringAttribute == null) {
            return defaultValue;
        }
        String text = stringAttribute.getText();
        return text == null || text.isEmpty() ? defaultValue : text.trim();
    }

    public static Integer getIntegerAttribute(Element element, String name) {
        return getIntegerAttribute(element, name, null);
    }

    public static Integer getIntegerAttribute(Element element, String name, Integer defaultValue) {
        Attribute longAttribute = element.attribute(name);
        if (longAttribute == null) {
            return defaultValue;
        }
        String text = longAttribute.getText();
        if (text == null || text.isEmpty()) {
            return defaultValue;
        }

        try {
            return Integer.valueOf(text.trim());
        } catch (NumberFormatException e) {
            Util.report("get [" + element.getName() + "] attribute [" + name + "] failure.return default value [" + defaultValue + "]", e);
            return defaultValue;
        }
    }

    public static Long getLongAttribute(Element element, String name) {
        return getLongAttribute(element, name, null);
    }

    public static Long getLongAttribute(Element element, String name, Long defaultValue) {
        Attribute longAttribute = element.attribute(name);
        if (longAttribute == null) {
            return defaultValue;
        }
        String text = longAttribute.getText();
        if (text == null || text.isEmpty()) {
            return defaultValue;
        }

        try {
            return Long.valueOf(text.trim());
        } catch (NumberFormatException e) {
            Util.report("get [" + element.getName() + "] attribute [" + name + "] failure.return default value [" + defaultValue + "]", e);
            return defaultValue;
        }
    }
}
