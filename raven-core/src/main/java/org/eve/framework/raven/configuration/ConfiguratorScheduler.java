package org.eve.framework.raven.configuration;

import org.eve.framework.raven.bean.ScanBean;

/**
 * @author yc_xia
 * @date 2018/6/20
 */
@ScanBean
public interface ConfiguratorScheduler {
    /**
     * 开始定时器
     *
     * @param updateTask
     * @param scanPeriod
     */
    void start(BaseUpdateTask updateTask, long scanPeriod);
}
