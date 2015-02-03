package com.nathanwestlake.dartgenerator;

import com.nathanwestlake.dartgenerator.annotations.DartInclude;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.project.MavenProject;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClasspathUtils {
	public Set<Class<?>> getModelClasses(MavenProject project) throws DependencyResolutionRequiredException, MalformedURLException {
		Set<URL> urls = new HashSet<>();
		List<String> pathElements = project.getRuntimeClasspathElements();
		pathElements.addAll(project.getCompileClasspathElements());

		for (String element : pathElements) {
			urls.add(new File(element).toURI().toURL());
		}

		ClassLoader contextClassLoader = URLClassLoader.newInstance(
				urls.toArray(new URL[urls.size()]),
				Thread.currentThread().getContextClassLoader());

		Thread.currentThread().setContextClassLoader(contextClassLoader);

		Reflections reflections = new Reflections(new ConfigurationBuilder()
				.setScanners(new SubTypesScanner(false /* don't exclude Object.class */), new TypeAnnotationsScanner(), new ResourcesScanner())
				.setUrls(ClasspathHelper.forClassLoader(contextClassLoader))
		);
		return reflections.getTypesAnnotatedWith(DartInclude.class);
	}
}
