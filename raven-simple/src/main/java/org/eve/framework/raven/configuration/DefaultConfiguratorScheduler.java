package org.eve.framework.raven.configuration;

import org.eve.framework.raven.bean.DefaultImplementation;
import org.eve.framework.raven.util.ExecutorServiceUtils;
import org.eve.framework.raven.util.VerifyUtils;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author yc_xia
 * @date 2018/6/19
 */
@DefaultImplementation
public class DefaultConfiguratorScheduler implements ConfiguratorScheduler {
    private ScheduledExecutorService scheduler;
    private ScheduledFuture<?> scheduledFuture;

    /**
     * 开始定时器
     *
     * @param updateTask
     * @param scanPeriod
     */
    @Override
    public synchronized void start(BaseUpdateTask updateTask, long scanPeriod) {
        if (VerifyUtils.isNull(this.scheduler)) {
            this.scheduler = ExecutorServiceUtils.newScheduledExecutorService();
        }
        this.scheduledFuture = this.scheduler.scheduleWithFixedDelay(updateTask, scanPeriod, scanPeriod, TimeUnit.MILLISECONDS);
    }

    /**
     * 停止定时器
     */
    private synchronized void stop() {
        if (VerifyUtils.notNull(this.scheduledFuture) && !this.scheduledFuture.isDone() && !this.scheduledFuture.isCancelled()) {
            this.scheduledFuture.cancel(false);
            this.scheduledFuture = null;
        }
    }

    /**
     * 重启定时器
     */
    private void restart(ConfiguratorListener configuratorListener, long scanPeriod) {
        this.stop();
//        this.start(configuratorListener, scanPeriod);
    }
}
