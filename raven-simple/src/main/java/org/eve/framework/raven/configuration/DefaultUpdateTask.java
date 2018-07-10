package org.eve.framework.raven.configuration;

import org.eve.framework.raven.bean.DefaultImplementation;
import org.eve.framework.raven.definition.Configuration;
import org.eve.framework.raven.exception.RavenException;
import org.eve.framework.raven.util.VerifyUtils;
import org.slf4j.helpers.Util;

/**
 * @author yc_xia
 * @date 2018/7/2
 */
@DefaultImplementation
public class DefaultUpdateTask extends BaseUpdateTask {
    public DefaultUpdateTask(ConfiguratorListener configuratorListener, ConfiguratorValidator configuratorValidator, ConfigurationRefresher contextUpdater) {
        super(configuratorListener, configuratorValidator, contextUpdater);
    }

    @Override
    public void run() {
        try {
            Configuration configuration = super.configuratorListener.refreshConfiguration();
            if (VerifyUtils.isNull(configuration)) {
                return;
            }
            super.configuratorValidator.validate(configuration);
            super.contextUpdater.refresh(configuration);
        } catch (RavenException e) {
            Util.report("刷新配置信息失败", e);
        }
    }
}
