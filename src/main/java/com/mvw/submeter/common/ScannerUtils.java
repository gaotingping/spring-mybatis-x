package com.mvw.submeter.common;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.ClassUtils;

/**
 * 包扫描
 * 
 * @author gaotingping@cyberzone.cn
 */
public class ScannerUtils{
	
	private static final String RESOURCE_PATTERN = "/**/*.class";

	private static ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

	public static List<Class<?>> scanner(List<String> packages,Class<? extends Annotation> annotationType) throws IOException, ClassNotFoundException {

		List<Class<?>> classSet = new ArrayList<>();
		
		TypeFilter typeFilter = null;
		if(annotationType != null){
			typeFilter = new AnnotationTypeFilter(annotationType, false);
		}

		if (packages!=null && !packages.isEmpty()) {
			for (String pkg : packages) {
				String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX+ ClassUtils.convertClassNameToResourcePath(pkg) + RESOURCE_PATTERN;
				Resource[] resources = resourcePatternResolver.getResources(pattern);
				MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
				for (Resource resource : resources) {
					if (resource.isReadable()) {
						MetadataReader reader = readerFactory.getMetadataReader(resource);
						String className = reader.getClassMetadata().getClassName();
						if(typeFilter == null){
							classSet.add(Class.forName(className));
						}else if (matchesEntityTypeFilter(reader, readerFactory,typeFilter)) {
							classSet.add(Class.forName(className));
						}
					}
				}
			}
		}

		return classSet;
	}

	private static boolean matchesEntityTypeFilter(MetadataReader reader, 
			MetadataReaderFactory readerFactory,
			TypeFilter typeFilter)
			throws IOException {
		return typeFilter.match(reader, readerFactory);
	}
}