package org.eve.framework.raven.configuration;

import org.eve.framework.raven.bean.ScanBean;
import org.eve.framework.raven.definition.Configuration;
import org.eve.framework.raven.definition.Raven;
import org.eve.framework.raven.exception.RavenException;

/**
 * @author yc_xia
 * @date 2018/6/20
 */
@ScanBean
public interface ConfiguratorValidator {
    /**
     * 校验配置文件
     *
     * @param raven
     * @return
     * @throws RavenException
     */
    void validate(Raven raven) throws RavenException;

    /**
     * 校验配置文件
     *
     * @param configuration
     * @return
     * @throws RavenException
     */
    void validate(Configuration configuration) throws RavenException;
}
