package org.eve.framework.raven;

import org.eve.framework.raven.configuration.ConfigurationRefresher;
import org.eve.framework.raven.configuration.ConfiguratorManager;
import org.eve.framework.raven.definition.Configuration;
import org.eve.framework.raven.exception.RavenException;
import org.eve.framework.raven.logger.RavenLogger;
import org.eve.framework.raven.util.StateMachine;
import org.eve.framework.raven.util.VerifyUtils;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.helpers.Util;

/**
 * Raven Logger 工厂
 *
 * @author yc_xia
 * @date 2018/6/19
 */
public class RavenLoggerFactory extends AbstractLoggerFactory implements ILoggerFactory, ConfigurationRefresher {
    /**
     * 状态机
     */
    private final StateMachine stateMachine = StateMachine.getInstance();
    private ConfiguratorManager configuratorManager;

    public RavenLoggerFactory() {
        super.updateLock.writeLock().lock();
        try {
            //如果不是 未初始化 状态则返回
            if (!this.stateMachine.initializedAble()) {
                return;
            }
            //设置状态为 初始化中
            this.stateMachine.initializing();
            //进行初始化
            if (this.init()) {
                //初始化成功 设置状态为 已经初始化
                this.stateMachine.initialized();
            } else {
                //初始化失败 设置状态为 中断
                this.stateMachine.interrupted();
            }
        } finally {
            super.updateLock.writeLock().unlock();
        }
    }

    /**
     * 初始化
     */
    private boolean init() {
        //初始化bean工厂
        try {
            this.initBeanFactory();
        } catch (RavenException e) {
            Util.report("初始化日志Bean工厂失败", e);
            return false;
        }
        try {
            this.configuratorManager = ConfiguratorManager.getInstance(this);
        } catch (RavenException e) {
            Util.report("创建配置管理器失败", e);
            return false;
        }
        //初始化配置管理器
        try {
            super.raven = this.configuratorManager.init();
        } catch (RavenException e) {
            Util.report("初始化配置管理器失败", e);
            return false;
        }
        //初始化Logger工厂
        try {
            super.initLoggerFactory(super.raven);
        } catch (RavenException e) {
            Util.report("初始化Logger工厂失败", e);
            return false;
        }
        //开启配置信息心跳检测
        try {
            if (VerifyUtils.notNull(this.raven.getConfigurator()) && this.raven.getConfigurator().getScan()) {
                this.configuratorManager.startScheduler(this.raven.getConfigurator());
            }
        } catch (RavenException e) {
            Util.report("开启配置信息更新定时器失败", e);
            return false;
        }
        return true;
    }

    /**
     * 刷新logger工厂
     *
     * @param configuration
     * @throws RavenException
     */
    @Override
    public void refresh(Configuration configuration) throws RavenException {
        super.updateLock.writeLock().lock();
        try {
            if (super.raven.getConfiguration().toString().equals(configuration.toString())) {
                Util.report("配置信息没有变化...");
                return;
            }
            Util.report("发现配置信息变更,进行更新...");
            super.refresh(configuration);
        } finally {
            super.updateLock.writeLock().unlock();
        }
    }

    /**
     * 获取Logger实例对象
     *
     * @param logName
     * @return
     */
    @Override
    public Logger getLogger(String logName) {
        if (!this.stateMachine.isInitialized()) {
            return null;
        }
        super.updateLock.readLock().lock();
        try {
            RavenLogger logger = super.getRavenLogger(logName);
            return logger;
        } catch (RavenException e) {
            Util.report("获取Logger失败,logName:" + logName, e);
            return null;
        } finally {
            super.updateLock.readLock().unlock();
        }
    }

    /**
     * 从spring容器中获取bean
     *
     * @param beanName
     * @return
     */
    @Override
    protected Object getSpringBean(String beanName) {
        return null;
    }

    /**
     * 从spring容器中获取bean
     *
     * @param beanName
     * @param args
     * @return
     */
    @Override
    protected Object getSpringBean(String beanName, Object... args) {
        return null;
    }

    /**
     * 从spring容器中获取bean
     *
     * @param beanType
     * @return
     */
    @Override
    protected <T> T getSpringBean(Class<T> beanType) {
        return null;
    }

    /**
     * 从spring容器中获取bean
     *
     * @param beanType
     * @param args
     * @return
     */
    @Override
    protected <T> T getSpringBean(Class<T> beanType, Object... args) {
        return null;
    }

}
