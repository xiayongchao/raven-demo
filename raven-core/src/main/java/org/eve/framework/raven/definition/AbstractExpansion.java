package org.eve.framework.raven.definition;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.eve.framework.raven.exception.RavenException;
import org.eve.framework.raven.util.JsonUtils;
import org.eve.framework.raven.util.VerifyUtils;
import org.slf4j.helpers.Util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 扩展信息
 *
 * @author yc_xia
 * @date 2018/6/20
 */
public abstract class AbstractExpansion implements Copyable {
    private Map<String, Object> expansionMap;

    public void put(String key, Object value) {
        if (VerifyUtils.isNull(this.expansionMap)) {
            this.expansionMap = new HashMap<>();
        }
        this.expansionMap.put(key, value);
    }

    public Object get(String key) {
        if (VerifyUtils.isNull(this.expansionMap)) {
            return null;
        }
        return this.expansionMap.get(key);
    }

    public void appendExpansionMap(AbstractExpansion... sources) throws RavenException {
        if (VerifyUtils.isEmpty(sources)) {
            return;
        }
        for (AbstractExpansion source : sources) {
            if (VerifyUtils.isNull(source) || VerifyUtils.isEmpty(source.getExpansionMap())) {
                continue;
            }
            try {
                Map<String, Object> copyExpansionMap = source.deepCopyExpansionMap();
                for (Map.Entry<String, Object> entry : copyExpansionMap.entrySet()) {
                    this.put(entry.getKey(), entry.getValue());
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RavenException("添加expansionMap扩展信息失败", e);
            }
        }
    }

    public Map<String, Object> getExpansionMap() {
        return expansionMap;
    }

    public void setExpansionMap(Map<String, Object> expansionMap) {
        this.expansionMap = expansionMap;
    }

    public Map<String, Object> deepCopyExpansionMap() throws IOException, ClassNotFoundException {
        return (Map<String, Object>) this.deepCopy(this.expansionMap);
    }

    @Override
    public String toString() {
        try {
            return JsonUtils.convertToJson(this);
        } catch (JsonProcessingException e) {
            Util.report("序列化JSON格式失败");
            return super.toString();
        }
    }
}
