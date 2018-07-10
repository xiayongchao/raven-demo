package org.eve.framework.raven.configuration;

import org.eve.framework.raven.bean.ScanBean;
import org.eve.framework.raven.definition.Configuration;
import org.eve.framework.raven.definition.Raven;
import org.eve.framework.raven.exception.RavenException;

/**
 * @author yc_xia
 * @date 2018/6/19
 */
@ScanBean
public interface ConfiguratorListener {
    /**
     * 初始配置
     *
     * @return
     * @throws RavenException
     */
    Raven initialConfiguration() throws RavenException;

    /**
     * 更新配置
     *
     * @return
     * @throws RavenException
     */
    Configuration refreshConfiguration() throws RavenException;
}
