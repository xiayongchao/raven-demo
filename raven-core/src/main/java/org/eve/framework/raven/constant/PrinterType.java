package org.eve.framework.raven.constant;

import org.eve.framework.raven.util.VerifyUtils;

/**
 * 打印器类型
 *
 * @author yc_xia
 * @date 2018/5/25
 */
public enum PrinterType {
    SYS_OUT, CACHED_SYS_OUT, SYS_ERR, CACHED_SYS_ERR, FILE, EXTENSION;

    public static boolean contains(String printerType) {
        if (VerifyUtils.isNull(printerType)) {
            return false;
        }
        printerType = printerType.toUpperCase();
        for (PrinterType type : PrinterType.values()) {
            if (printerType.equalsIgnoreCase(type.toString())) {
                return true;
            }
        }
        return false;
    }

    public static PrinterType get(String printerType) {
        if (VerifyUtils.isNull(printerType)) {
            return null;
        }
        return PrinterType.valueOf(printerType.toUpperCase());
    }
}
