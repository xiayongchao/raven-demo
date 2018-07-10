package org.eve.framework.raven.support;

import org.eve.framework.raven.annotation.RavenBean;
import org.eve.framework.raven.definition.RavenComponentDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author xyc
 * @date 2018/2/11 0011
 */
public class RavenComponentScanParser implements ResourceLoaderAware, EnvironmentAware {
    private Environment environment;
    private MetadataReaderFactory metadataReaderFactory;
    private ResourcePatternResolver resourcePatternResolver;

    public Set<RavenComponentDefinition> parse(String[] basePackages, String resourcePattern) throws IOException {
        Assert.notEmpty(basePackages, "basePackages不能为空");
        Assert.hasText(resourcePattern, "resourcePattern不能为空");
        TypeFilter typeFilter = new AnnotationTypeFilter(RavenBean.class);
        MetadataReader metadataReader = null;
        AnnotationMetadata annotationMetadata = null;
        Set<RavenComponentDefinition> ravenComponentDefinitions = new HashSet<>();
        for (String basePackage : basePackages) {
            Assert.hasText(basePackage, "basePackage不能为空");
            String classPattern = "classpath*:" + ClassUtils.convertClassNameToResourcePath(this.environment.resolveRequiredPlaceholders(basePackage)) + "/" + resourcePattern;
            Resource[] resources = this.resourcePatternResolver.getResources(classPattern);
            if (resources == null || resources.length == 0) {
                continue;
            }
            for (int j = 0; j < resources.length; j++) {
                Resource resource = resources[j];
                if (resource.isReadable()) {
                    metadataReader = this.metadataReaderFactory.getMetadataReader(resource);
                    annotationMetadata = metadataReader.getAnnotationMetadata();
                    if (typeFilter.match(metadataReader, this.metadataReaderFactory)) {
                        ravenComponentDefinitions.add(new RavenComponentDefinition(this.generateBeanName(annotationMetadata), annotationMetadata.getClassName()));
                    }
                }
            }
        }
        return ravenComponentDefinitions;
    }

    private String generateBeanName(AnnotationMetadata annotationMetadata) {
        Set<String> annotationTypes = annotationMetadata.getAnnotationTypes();
        if (annotationTypes != null && !annotationTypes.isEmpty()) {
            for (String annotationType : annotationTypes) {
                Map<String, Object> map = annotationMetadata.getAnnotationAttributes(annotationType, false);
                String value = map.get("value").toString();
                if (value == null || value.isEmpty()) {
                    String[] classNameArr = annotationMetadata.getClassName().split("\\.");
                    char[] cs = classNameArr[classNameArr.length - 1].toCharArray();
                    //首字母小写
                    cs[0] = (char) (cs[0] + 32);
                    value = String.valueOf(cs);
                }
                return value;
            }
        }
        return null;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
        this.metadataReaderFactory = new CachingMetadataReaderFactory(this.resourcePatternResolver);
    }
}
