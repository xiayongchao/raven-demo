package org.eve.framework.raven.constant;

import org.eve.framework.raven.util.VerifyUtils;

/**
 * 处理器类型
 *
 * @author yc_xia
 * @date 2018/6/19
 */
public enum ProcessorType {
    FORMAT, PRINT;

    public static boolean contains(String processorType) {
        if (VerifyUtils.isNull(processorType)) {
            return false;
        }
        processorType = processorType.toUpperCase();
        for (ProcessorType type : ProcessorType.values()) {
            if (processorType.equals(type.toString())) {
                return true;
            }
        }
        return false;
    }
}
