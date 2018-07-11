package org.eve.framework.raven.definition;

import org.eve.framework.raven.constant.PrinterType;

import java.util.List;

/**
 * @author yc_xia
 * @date 2018/6/19
 */
public class PrinterDefinition extends AbstractExpansion {
    private String id;
    private String typeName;
    private PrinterType type;
    private String className;
    private String path;
    private String description;
    private List<String> processorRef;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public PrinterType getType() {
        return type;
    }

    public void setType(PrinterType type) {
        this.type = type;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getProcessorRef() {
        return processorRef;
    }

    public void setProcessorRef(List<String> processorRef) {
        this.processorRef = processorRef;
    }

    /**
     * 只要id或者className中一项相等则整个对象相等
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PrinterDefinition that = (PrinterDefinition) o;

        if (id != null && id.equals(that.id)) {
            return true;
        }
        if (id == null && that.id == null) {
            return true;
        }
        if (className != null && className.equals(that.className)) {
            return true;
        }
        if (className == null && that.className == null) {
            return true;
        }

        return false;
    }

    /**
     * hashCode返回值一致，将比较是否相等交给equals方法进行判断
     *
     * @return
     */
    @Override
    public int hashCode() {
        return 0;
    }
}
