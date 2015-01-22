package com.nathanwestlake.dartgenerator;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.project.MavenProject;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by CorayThan on 1/21/2015.
 */
public class ClasspathUtils {
	public Set<Class<?>> getModelClasses(MavenProject project, List<String> includePackages, Set<String> excludeClasses) throws DependencyResolutionRequiredException, MalformedURLException {
		Set<URL> urls = new HashSet<URL>();
		List<String> pathElements = project.getRuntimeClasspathElements();
		pathElements.addAll(project.getCompileClasspathElements());

		for (String element : pathElements) {
			urls.add(new File(element).toURI().toURL());
		}

		ClassLoader contextClassLoader = URLClassLoader.newInstance(
				urls.toArray(new URL[0]),
				Thread.currentThread().getContextClassLoader());

		Thread.currentThread().setContextClassLoader(contextClassLoader);

		FilterBuilder filterBuilder = new FilterBuilder();
		for (String pack : includePackages) {
			filterBuilder.include(FilterBuilder.prefix(pack));
		}

		Reflections reflections = new Reflections(new ConfigurationBuilder()
				.setScanners(new SubTypesScanner(false /* don't exclude Object.class */), new ResourcesScanner())
				.setUrls(ClasspathHelper.forClassLoader(contextClassLoader))
				.filterInputsBy(filterBuilder)
		);
		Set<Class<?>> toReturn = reflections.getSubTypesOf(Object.class);
		if (excludeClasses != null) {
			toReturn.removeIf(p -> excludeClasses.contains(p.getSimpleName()));
		}
		return toReturn;
	}
}
