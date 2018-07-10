package org.eve.framework.raven.configuration;

import org.eve.framework.raven.definition.Configuration;
import org.eve.framework.raven.exception.RavenException;

/**
 * @author yc_xia
 * @date 2018/6/29
 */
public interface ConfigurationRefresher {
    void refresh(Configuration configuration) throws RavenException;
}
