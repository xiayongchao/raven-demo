package org.eve.framework.raven.bean;

import org.eve.framework.raven.constant.Constants;
import org.eve.framework.raven.util.VerifyUtils;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * bean工厂抽象类
 *
 * @author yc_xia
 * @date 2018/6/8
 */
public abstract class AbstractBeanFactory {
    /**
     * 查询类型
     */
    private enum FindType {
        //继承
        EXTENDS,
        //实现
        IMPLEMENTS;
    }

    /**
     * 通过接口类型查询实现Class列表
     *
     * @param ifClass
     * @param exclusions
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    protected List<Class<?>> findByInterface(Class<?> ifClass, Class<?>... exclusions) throws IOException, ClassNotFoundException {
        return find(Constants.CLASS_PATH, ifClass, FindType.IMPLEMENTS, exclusions);
    }

    /**
     * 通过父类类型查询实现Class列表
     *
     * @param pClass
     * @param exclusions
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    protected List<Class<?>> findByExtend(Class<?> pClass, Class<?>... exclusions) throws IOException, ClassNotFoundException {
        return find(Constants.CLASS_PATH, pClass, FindType.EXTENDS, exclusions);
    }

    /**
     * 通过注解查询实现Class列表
     *
     * @param annotation
     * @param exclusions
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    protected List<Class<?>> findByAnnotation(Class<? extends Annotation> annotation, Class<?>... exclusions) throws IOException, ClassNotFoundException {
        if (VerifyUtils.isNull(annotation)) {
            return null;
        }
        String packageName = Constants.CLASS_PATH;
        List<String> classNames = findClassNames(packageName);
        List<Class<?>> classes = new ArrayList<>();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Class<?> loadClass;
        for (String className : classNames) {
            loadClass = classLoader.loadClass(className);
            if (isExclusion(loadClass, exclusions)) {
                continue;
            }
            if (loadClass.isAnnotationPresent(annotation)) {
                classes.add(loadClass);
            }
        }
        return classes;
    }

    /**
     * 查询实现或子类Class列表
     *
     * @param packageName
     * @param cls
     * @param findType
     * @param exclusions
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private List<Class<?>> find(String packageName, Class<?> cls, FindType findType, Class<?>... exclusions) throws IOException, ClassNotFoundException {
        if (VerifyUtils.isEmpty(packageName)) {
            packageName = Constants.CLASS_PATH;
        }
        packageName = packageName.replaceAll(Constants.POINT_MARK, File.separator);

        List<String> classNames = findClassNames(packageName);
        List<Class<?>> classes = new ArrayList<>();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Class<?> loadClass;
        for (String className : classNames) {
            loadClass = classLoader.loadClass(className);
            if (isExclusion(loadClass, exclusions)) {
                continue;
            }
            if (VerifyUtils.isNull(cls)) {
                if (Constants.CLASS_PATH.equals(packageName) || loadClass.getName().startsWith(packageName)) {
                    classes.add(loadClass);
                }
                continue;
            }
            Class<?> superclass;
            if (FindType.EXTENDS.equals(findType)) {
                if (!Modifier.isAbstract(loadClass.getModifiers())) {
                    superclass = loadClass.getSuperclass();
                    while (VerifyUtils.notNull(superclass) && !superclass.equals(cls)) {
                        superclass = superclass.getSuperclass();
                    }
                    if (VerifyUtils.notNull(superclass) && superclass.equals(cls)) {
                        classes.add(loadClass);
                    }
                }
            } else if (FindType.IMPLEMENTS.equals(findType)) {
                superclass = loadClass;
                Class<?>[] interfaces;
                outer:
                while (VerifyUtils.notNull(superclass)) {
                    interfaces = superclass.getInterfaces();
                    superclass = superclass.getSuperclass();
                    for (Class<?> anInterface : interfaces) {
                        if (anInterface.equals(cls)) {
                            classes.add(loadClass);
                            break outer;
                        }
                    }
                }
            }
        }
        return classes;
    }

    /**
     * 查询包下的所有Class全限定类名
     *
     * @param packageName
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private List<String> findClassNames(String packageName) throws IOException, ClassNotFoundException {
        if (VerifyUtils.isEmpty(packageName)) {
            packageName = Constants.CLASS_PATH;
        }
        packageName = packageName.replaceAll(Constants.POINT_MARK, File.separator);
        ClassLoader classLoader = this.getClass().getClassLoader();
        Enumeration<URL> enumeration;
        if (VerifyUtils.notNull(classLoader)) {
            enumeration = classLoader.getResources(packageName);
        } else {
            enumeration = ClassLoader.getSystemResources(packageName);
        }
        List<String> classNames = new ArrayList<>();
        URL url;
        File file;
        List<String> classPaths = null;
        String prefix = null;
        while (enumeration.hasMoreElements()) {
            url = enumeration.nextElement();
            switch (url.getProtocol()) {
                case Constants.FILE:
                    // 获取包的物理路径
                    file = new File(URLDecoder.decode(url.getFile(), Constants.UTF_8));
                    prefix = file.getPath() + File.separator;
                    classPaths = findClassPathsByFile(file);
                    break;
                case Constants.JAR:
                    //TODO  jar包方式的还没有实现
                    break;
                default:
                    break;
            }
            if (VerifyUtils.isEmpty(classPaths)) {
                continue;
            }
            for (String classPath : classPaths) {
                if (VerifyUtils.notBlank(prefix)) {
                    classPath = classPath.replace(prefix, Constants.EMPTY_STRING);
                    classPath = classPath.substring(0, classPath.length() - 6);
                    classPath = classPath.replace(File.separator, Constants.POINT_MARK);
                    classNames.add(classPath);
                }
            }
        }
        return classNames;
    }

    /**
     * 判断是否排除
     *
     * @param cls
     * @param exclusions
     * @return
     */
    private boolean isExclusion(Class<?> cls, Class<?>... exclusions) {
        if (exclusions != null && exclusions.length > 0) {
            for (Class<?> exclusion : exclusions) {
                if (exclusion.equals(cls)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 根据文件查询所有Class的文件路径
     *
     * @param file
     * @return
     * @throws ClassNotFoundException
     */
    private List<String> findClassPathsByFile(File file) throws ClassNotFoundException {
        List<String> classNames = new ArrayList<>();
        if (!file.exists()) {
            return classNames;
        }
        if (!file.isDirectory() && file.getPath().endsWith(Constants.CLASS_SUFFIX)) {
            classNames.add(file.getPath());
        }
        File[] listFiles = file.listFiles();
        if (VerifyUtils.notEmpty(listFiles)) {
            for (File listFile : listFiles) {
                classNames.addAll(findClassPathsByFile(listFile));
            }
        }
        return classNames;
    }
}
