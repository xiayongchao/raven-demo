package org.eve.framework.raven.definition;

import org.eve.framework.raven.constant.ProcessorType;

/**
 * @author yc_xia
 * @date 2018/6/19
 */
public class ProcessorDefinition extends AbstractExpansion {
    private Integer id;
    private String typeName;
    private ProcessorType type;
    /**
     * notNull
     */
    private String className;
    private String description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public ProcessorType getType() {
        return type;
    }

    public void setType(ProcessorType type) {
        this.type = type;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

        ProcessorDefinition that = (ProcessorDefinition) o;

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
