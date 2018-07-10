package org.eve.framework.raven.util;

import org.eve.framework.raven.constant.Constants;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yc_xia
 * @date 2018/6/29
 */
public class ExecutorServiceUtils {
    private static final ThreadFactory THREAD_FACTORY = new ThreadFactory() {

        private final ThreadFactory defaultFactory = Executors.defaultThreadFactory();
        private final AtomicInteger threadNumber = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = defaultFactory.newThread(r);
            if (!thread.isDaemon()) {
                thread.setDaemon(true);
            }
            thread.setName("raven-" + threadNumber.getAndIncrement());
            return thread;
        }
    };

    static public ScheduledExecutorService newScheduledExecutorService() {
        return new ScheduledThreadPoolExecutor(Constants.SCHEDULED_EXECUTOR_POOL_SIZE, THREAD_FACTORY);
    }


    /**
     * Creates an executor service suitable for use by logback components.
     *
     * @return executor service
     */
    static public ExecutorService newExecutorService() {
        return new ThreadPoolExecutor(Constants.CORE_POOL_SIZE, Constants.MAX_POOL_SIZE, 0L, TimeUnit.MILLISECONDS, new SynchronousQueue<Runnable>(),
                THREAD_FACTORY);
    }

    /**
     * Shuts down an executor service.
     * <p>
     *
     * @param executorService the executor service to shut down
     */
    static public void shutdown(ExecutorService executorService) {
        executorService.shutdownNow();
    }
}
