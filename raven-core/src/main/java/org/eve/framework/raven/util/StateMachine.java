package org.eve.framework.raven.util;

/**
 * 状态机
 *
 * @author yc_xia
 * @date 2018/6/28
 */
public class StateMachine {
    /**
     * 中断
     */
    private static final int INTERRUPTED = -2;
    /**
     * 未初始化
     */
    private static final int UNINITIALIZED = -1;
    /**
     * 初始化中
     */
    private static final int INITIALIZING = 0;

    /**
     * 已经初始化
     */
    private static final int INITIALIZED = 1;
    /**
     * 已经停止
     */
    private static final int STOPPED = 2;
    /**
     * 重新启动中
     */
    private static final int RESTARTING = 3;

    public static StateMachine getInstance() {
        /**
         * 默认状态:未初始化
         */
        return new StateMachine(UNINITIALIZED);
    }

    /**
     * 当前状态
     */
    private int state;

    private StateMachine(int state) {
        this.state = state;
    }

    public boolean isUninitialized() {
        return this.state == UNINITIALIZED;
    }

    public boolean isInitializing() {
        return this.state == INITIALIZING;
    }

    public void initializing() {
        this.state = INITIALIZING;
    }

    public boolean isInterrupted() {
        return this.state == INTERRUPTED;
    }

    public void interrupted() {
        this.state = INTERRUPTED;
    }

    public boolean isInitialized() {
        return this.state == INITIALIZED;
    }

    public void initialized() {
        this.state = INITIALIZED;
    }

    public boolean initializedAble() {
        return this.state == UNINITIALIZED;
    }

    public boolean isRestarting() {
        return this.state == RESTARTING;
    }

    public void restarting() {
        this.state = RESTARTING;
    }
}
