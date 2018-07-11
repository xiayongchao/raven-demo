package org.eve.framework.raven.util;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.slf4j.helpers.Util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * xml解析工具类，基于dom4j
 *
 * @author yc_xia
 */
public class XmlUtils {
    public static List<String> getStringValues(Element element, String name) {
        return getStringValues(element, name, null);
    }

    public static List<String> getStringValues(Element element, String name, String defaultValue) {
        Iterator<Element> idIterator = element.elements(name).iterator();
        List<String> list = new ArrayList<>();
        while (idIterator.hasNext()) {
            list.add(XmlUtils.getStringContent(idIterator.next(), defaultValue));
        }
        return list;
    }

    public static String getStringContent(Element element) {
        return getStringContent(element, null);
    }

    public static String getStringContent(Element element, String defaultValue) {
        String text = element.getTextTrim();
        if (VerifyUtils.isBlank(text)) {
            return defaultValue;
        }
        return text;
    }

    public static String getStringValue(Element element, String name) {
        return getStringValue(element, name, null);
    }

    public static String getStringValue(Element element, String name, String defaultValue) {
        Element stringElement = element.element(name);
        if (VerifyUtils.isNull(stringElement)) {
            return defaultValue;
        }
        return getStringContent(stringElement, defaultValue);
    }

    public static String getStringAttribute(Element element, String name) {
        return getStringAttribute(element, name, null);
    }

    public static String getStringAttribute(Element element, String name, String defaultValue) {
        Attribute stringAttribute = element.attribute(name);
        if (VerifyUtils.isNull(stringAttribute)) {
            return defaultValue;
        }
        String text = stringAttribute.getText();
        return VerifyUtils.isBlank(text) ? defaultValue : text.trim();
    }

    public static Integer getIntegerContent(Element element) {
        return getIntegerContent(element, (Integer) null);
    }

    public static Integer getIntegerContent(Element element, Integer defaultValue) {
        String text = element.getTextTrim();
        if (VerifyUtils.isBlank(text)) {
            return defaultValue;
        }

        try {
            return Integer.valueOf(text);
        } catch (NumberFormatException e) {
            Util.report("get [" + element.getName() + "] value failure.return default value [" + defaultValue + "]", e);
            return defaultValue;
        }
    }

    public static Integer getIntegerValue(Element element, String name) {
        return getIntegerValue(element, name, null);
    }

    public static Integer getIntegerValue(Element element, String name, Integer defaultValue) {
        Element intElement = element.element(name);
        if (VerifyUtils.isNull(intElement)) {
            return defaultValue;
        }
        return getIntegerContent(intElement, defaultValue);
    }

    public static Long getLongValue(Element element, String name) {
        return getLongValue(element, name, null);
    }

    public static Long getLongValue(Element element, String name, Long defaultValue) {
        Element longElement = element.element(name);
        if (VerifyUtils.isNull(longElement)) {
            return defaultValue;
        }
        String text = longElement.getTextTrim();
        if (VerifyUtils.isBlank(text)) {
            return defaultValue;
        }

        try {
            return Long.valueOf(text);
        } catch (NumberFormatException e) {
            Util.report("get [" + name + "] value failure.return default value [" + defaultValue + "]", e);
            return defaultValue;
        }
    }

    public static Boolean getBooleanValue(Element element, String name) {
        return getBooleanValue(element, name, null);
    }

    public static Boolean getBooleanValue(Element element, String name, Boolean defaultValue) {
        Element booleanElement = element.element(name);
        if (VerifyUtils.isNull(booleanElement)) {
            return defaultValue;
        }
        String text = booleanElement.getTextTrim();
        if (VerifyUtils.isBlank(text)) {
            return defaultValue;
        }
        return "true".equalsIgnoreCase(text) ? true : ("false".equalsIgnoreCase(text) ? false : defaultValue);
    }

    public static Boolean getBooleanAttribute(Element element, String name) {
        return getBooleanAttribute(element, name, null);
    }

    public static Boolean getBooleanAttribute(Element element, String name, Boolean defaultValue) {
        Attribute booleanAttribute = element.attribute(name);
        if (VerifyUtils.isNull(booleanAttribute)) {
            return defaultValue;
        }
        String text = booleanAttribute.getText();
        if (VerifyUtils.isBlank(text)) {
            return defaultValue;
        }
        text = text.trim();
        return "true".equalsIgnoreCase(text) ? true : ("false".equalsIgnoreCase(text) ? false : defaultValue);
    }

    public static Integer getIntegerAttribute(Element element, String name) {
        return getIntegerAttribute(element, name, null);
    }

    public static Integer getIntegerAttribute(Element element, String name, Integer defaultValue) {
        Attribute intAttribute = element.attribute(name);
        if (VerifyUtils.isNull(intAttribute)) {
            return defaultValue;
        }
        String text = intAttribute.getText();
        if (VerifyUtils.isBlank(text)) {
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
        if (VerifyUtils.isNull(longAttribute)) {
            return defaultValue;
        }
        String text = longAttribute.getText();
        if (VerifyUtils.isBlank(text)) {
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
