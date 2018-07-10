package org.eve.framework.raven.bean;

import org.eve.framework.raven.exception.RavenException;
import org.eve.framework.raven.util.VerifyUtils;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * bean工厂
 *
 * @author yc_xia
 * @date 2018/6/21
 */
public abstract class DefaultBeanFactory extends AbstractBeanFactory implements BeanFactory {
    /**
     * ${@link ScanBean}的class名称和class类型的对应Map
     */
    private final Map<String, Class<?>> scanBeanMap = new HashMap<>();
    /**
     * ${@link ScanBean}的class名称和实现类/子类的class类型列表的对应Map
     */
    private final Map<String, List<Class<?>>> implementationMap = new HashMap<>();
    /**
     * ${@link ScanBean}的class名称和${@link ImplBeans}的对应Map
     */
    private final Map<String, ImplBeans> ifName2ImplBeansMap = new HashMap<>();
    /**
     * ${@link ScanBean}的实现类/子类的class名称和class类型的对应Map
     */
    private final Map<String, Class<?>> implBeanName2ClassMap = new HashMap<>();
    /**
     * raven组件的class名称和被spring IOC容器管理后的beanName之间的对应关系
     */
    protected final Map<String, String> className2BeanNameMap = new HashMap<>();

    /**
     * bean类型
     */
    private enum BeanType {
        /**
         * 默认实现${@link DefaultImplementation}
         */
        DEFAULT,
        /**
         * 覆盖实现${@link CoverImplementation}
         */
        COVER,
        /**
         * 其它
         */
        OTHER;
    }

    /**
     * 实现的bean信息
     */
    private class ImplBeans {
        private final String scanBeanName;
        private final List<Class<?>> defaults = new ArrayList<>();
        private final List<Class<?>> covers = new ArrayList<>();
        private final List<Class<?>> others = new ArrayList<>();

        public ImplBeans(String scanBeanName) {
            this.scanBeanName = scanBeanName;
        }

        public Class<?> get() throws RavenException {
            if (this.defaults.size() > 1) {
                throw new RavenException(String.format("%s接口的默认实现不能超过一个", this.scanBeanName));
            }
            if (this.covers.size() > 1) {
                throw new RavenException(String.format("%s接口的覆盖实现不能超过一个", this.scanBeanName));
            }
            if (this.covers.size() == 1) {
                return this.covers.get(0);
            }
            if (this.defaults.size() == 1) {
                return this.defaults.get(0);
            }
            return null;
        }

        public void put(BeanType beanType, Class<?> beanClass) {
            if (VerifyUtils.isNull(beanClass) || VerifyUtils.isNull(beanClass)) {
                return;
            }
            switch (beanType) {
                case DEFAULT:
                    this.defaults.add(beanClass);
                    break;
                case COVER:
                    this.covers.add(beanClass);
                    break;
                case OTHER:
                    this.others.add(beanClass);
                    break;
            }
        }
    }

    /**
     * 根据bean类型获取实例
     *
     * @param tClass
     * @param <T>
     * @return
     * @throws RavenException
     */
    @Override
    public <T> T getBean(Class<T> tClass) throws RavenException {
        Object springBean = this.getSpringBean(tClass);
        if (VerifyUtils.notNull(springBean)) {
            return (T) springBean;
        }
        ImplBeans implBeans = this.ifName2ImplBeansMap.get(tClass.getName());
        if (VerifyUtils.isNull(implBeans)) {
            return null;
        }
        Class<?> aClass = implBeans.get();
        if (VerifyUtils.isNull(aClass)) {
            return null;
        }
        T t = null;
        try {
            t = (T) aClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RavenException(String.format("实例化%s失败", aClass.getName()), e);
        }
        return t;
    }

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
    @Override
    public <T> T getBean(Class<T> tClass, List<Class<?>> initArgClasses, List<Object> initArgs) throws RavenException {
        Object springBean = this.getSpringBean(tClass, initArgs);
        if (VerifyUtils.notNull(springBean)) {
            return (T) springBean;
        }
        ImplBeans implBeans = this.ifName2ImplBeansMap.get(tClass.getName());
        if (VerifyUtils.isNull(implBeans)) {
            return null;
        }
        Class<T> aClass = (Class<T>) implBeans.get();
        if (VerifyUtils.isNull(aClass)) {
            return null;
        }
        return this.newInstance(aClass, initArgClasses, initArgs);
    }

    /**
     * 从spring容器中获取bean
     *
     * @param beanName
     * @return
     */
    protected abstract Object getSpringBean(String beanName);

    /**
     * 从spring容器中获取bean
     *
     * @param beanName
     * @param args
     * @return
     */
    protected abstract Object getSpringBean(String beanName, Object... args);

    /**
     * 从spring容器中获取bean
     *
     * @param beanType
     * @param <T>
     * @return
     */
    protected abstract <T> T getSpringBean(Class<T> beanType);

    /**
     * 从spring容器中获取bean
     *
     * @param beanType
     * @param args
     * @param <T>
     * @return
     */
    protected abstract <T> T getSpringBean(Class<T> beanType, Object... args);

    /**
     * 根据全限定类名获取实例
     *
     * @param className
     * @param <T>
     * @return
     * @throws RavenException
     */
    @Override
    public <T> T getBean(String className) throws RavenException {
        return this.getBean(className, true);
    }

    /**
     * 根据全限定类名获取实例
     *
     * @param className
     * @param fromSpring
     * @param <T>
     * @return
     * @throws RavenException
     */
    @Override
    public <T> T getBean(String className, boolean fromSpring) throws RavenException {
        if (fromSpring) {
            Object springBean = this.getSpringBean(this.className2BeanNameMap.get(className));
            if (VerifyUtils.notNull(springBean)) {
                return (T) springBean;
            }
        }
        Class<?> aClass = this.implBeanName2ClassMap.get(className);
        if (VerifyUtils.isNull(aClass)) {
            return null;
        }
        T t = null;
        try {
            t = (T) aClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RavenException(String.format("实例化%s失败", aClass.getName()), e);
        }
        return t;
    }

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
    @Override
    public <T> T getBean(String className, List<Class<?>> initArgClasses, List<Object> initArgs) throws RavenException {
        Object springBean = this.getSpringBean(this.className2BeanNameMap.get(className), initArgs);
        if (VerifyUtils.notNull(springBean)) {
            return (T) springBean;
        }
        Class<T> aClass = (Class<T>) this.implBeanName2ClassMap.get(className);
        if (VerifyUtils.isNull(aClass)) {
            return null;
        }
        return this.newInstance(aClass, initArgClasses, initArgs);
    }

    /**
     * 实例化对象
     *
     * @param aClass
     * @param initArgClasses
     * @param initArgs
     * @param <T>
     * @return
     * @throws RavenException
     */
    private <T> T newInstance(Class<T> aClass, List<Class<?>> initArgClasses, List<Object> initArgs) throws RavenException {
        T t = null;
        try {
            if (VerifyUtils.isEmpty(initArgClasses) || VerifyUtils.isEmpty(initArgs)) {
                t = aClass.newInstance();
            } else {
                Constructor<?> constructor = aClass.getConstructor((Class<?>[]) initArgClasses.toArray());
                t = (T) constructor.newInstance(initArgs.toArray());
            }
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RavenException(String.format("实例化%s失败", aClass.getName()), e);
        }
        return t;
    }

    /**
     * 初始化bean工厂
     *
     * @throws RavenException
     */
    protected void initBeanFactory() throws RavenException {
        //加载 @ScanBean
        try {
            this.loadScanBean();
        } catch (IOException | ClassNotFoundException e) {
            throw new RavenException("加载@ScanBean接口/类失败", e);
        }
        if (VerifyUtils.isEmpty(this.scanBeanMap)) {
            throw new RavenException("没有发现@ScanBean接口/类");
        }
        //根据@ScanBean去加载实现类
        try {
            this.registerScanBean();
        } catch (IOException | ClassNotFoundException e) {
            throw new RavenException("注册@ScanBean接口/类的实现类/子类失败", e);
        }
        //进一步封装
        ImplBeans implBeans;
        for (Map.Entry<String, List<Class<?>>> entry : this.implementationMap.entrySet()) {
            if (VerifyUtils.isEmpty(entry.getValue())) {
                continue;
            }
            implBeans = new ImplBeans(entry.getKey());
            for (Class<?> aClass : entry.getValue()) {
                this.implBeanName2ClassMap.put(aClass.getName(), aClass);
                if (aClass.isAnnotationPresent(DefaultImplementation.class)) {
                    implBeans.put(BeanType.DEFAULT, aClass);
                } else if (aClass.isAnnotationPresent(CoverImplementation.class)) {
                    implBeans.put(BeanType.COVER, aClass);
                } else {
                    implBeans.put(BeanType.OTHER, aClass);
                }
            }
            this.ifName2ImplBeansMap.put(entry.getKey(), implBeans);
        }
    }

    /**
     * 加载注解了${@link ScanBean}的类或者接口
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void loadScanBean() throws IOException, ClassNotFoundException {
        List<Class<?>> classes = this.findByAnnotation(ScanBean.class);
        if (VerifyUtils.isEmpty(classes)) {
            return;
        }
        for (Class<?> aClass : classes) {
            this.scanBeanMap.put(aClass.getName(), aClass);
        }
    }

    /**
     * 注册@ScanBean接口/类的实现类/子类
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void registerScanBean() throws IOException, ClassNotFoundException {
        Class<?> cls;
        for (Map.Entry<String, Class<?>> classEntry : this.scanBeanMap.entrySet()) {
            cls = classEntry.getValue();
            if (VerifyUtils.isNull(cls)) {
                continue;
            }
            if (cls.isInterface()) {
                this.implementationMap.put(classEntry.getKey(), this.findByInterface(classEntry.getValue()));
            } else {
                this.implementationMap.put(classEntry.getKey(), this.findByExtend(classEntry.getValue()));
            }
        }
    }
}
