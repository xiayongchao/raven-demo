package org.eve.framework.raven.bean;

import org.eve.framework.raven.exception.RavenException;

import java.util.List;

/**
 * bean工厂接口
 *
 * @author yc_xia
 * @date 2018/6/22
 */
public interface BeanFactory {
    /**
     * 根据全限定类名获取实例
     *
     * @param className
     * @param <T>
     * @return
     * @throws RavenException
     */
    <T> T getBean(String className) throws RavenException;

    /**
     * 根据全限定类名获取实例
     *
     * @param className
     * @param fromSpring
     * @param <T>
     * @return
     * @throws RavenException
     */
    <T> T getBean(String className, boolean fromSpring) throws RavenException;

    /**
     * 根据全限定类名和构造函数参数信息获取实例
     *
     * @param className
     * @param initArgClasses
     * @param initArgs
     * @param <T>
     * @return
     * @throws RavenException
     */
    <T> T getBean(String className, List<Class<?>> initArgClasses, List<Object> initArgs) throws RavenException;

    /**
     * 根据bean类型获取实例
     *
     * @param tClass
     * @param <T>
     * @return
     * @throws RavenException
     */
    <T> T getBean(Class<T> tClass) throws RavenException;

    /**
     * 根据bean类型和构造函数参数信息获取实例
     *
     * @param tClass
     * @param initArgClasses
     * @param initArgs
     * @param <T>
     * @return
     * @throws RavenException
     */
    <T> T getBean(Class<T> tClass, List<Class<?>> initArgClasses, List<Object> initArgs) throws RavenException;
}
