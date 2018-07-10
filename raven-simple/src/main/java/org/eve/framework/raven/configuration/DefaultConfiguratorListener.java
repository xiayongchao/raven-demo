package org.eve.framework.raven.configuration;

import org.eve.framework.raven.bean.DefaultImplementation;
import org.eve.framework.raven.definition.Configuration;
import org.eve.framework.raven.definition.Raven;
import org.eve.framework.raven.exception.RavenException;
import org.eve.framework.raven.util.VerifyUtils;

/**
 * @author yc_xia
 * @date 2018/6/19
 */
@DefaultImplementation
public class DefaultConfiguratorListener implements ConfiguratorListener {
    private static final String CONFIGURATION_FILE = "raven.xml";
    private final RavenParser ravenParser = new RavenParser(CONFIGURATION_FILE);

    @Override
    public Raven initialConfiguration() throws RavenException {
        return this.ravenParser.parse();
    }

    @Override
    public Configuration refreshConfiguration() throws RavenException {
        Raven raven = this.ravenParser.parse();
        return VerifyUtils.isNull(raven) ? null : raven.getConfiguration();
    }
}
