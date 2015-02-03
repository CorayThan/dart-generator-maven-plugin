package com.nathanwestlake.dartgenerator;

import com.nathanwestlake.dartgenerator.generators.ClassGenerator;
import com.nathanwestlake.dartgenerator.models.DartEntity;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Mojo(name = "generate", requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME)
public class DartGeneratorMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;

    @Parameter(required = true)
    private File destination;

    @Parameter(required = true)
    private String importLine;

    private ClasspathUtils classpathUtils = new ClasspathUtils();
    private ClassGenerator classGenerator = new ClassGenerator();

    public void execute() throws MojoExecutionException {
        if (destination == null) {
            getLog().warn("No destination was configured for generated dart classes. Project directory will be used.");
            destination = new File(System.getProperty("user.dir"));
        }
        if (!destination.isDirectory()) {
            throw new MojoExecutionException("destination parameter must be a directory.");
        }
        importLine = "import 'package:" + importLine + "/";

        try {
            Set<Class<?>> modelClasses = classpathUtils.getModelClasses(project);
            Map<String, DartEntity> dartClasses = new HashMap<>();
            for (Class<?> model : modelClasses) {
                DartEntity dartClass = DartEntity.newInstance(model, importLine);

	            if (dartClasses.containsKey(dartClass.getClassName())) {
		            throw new MojoExecutionException("Classes must not have identical names. There are at least two classes " +
				            "with the name: " + dartClass.getClassName());
	            }
	            dartClasses.put(dartClass.getClassName(), dartClass);
            }
            for (DartEntity dartClass : dartClasses.values()) {
                File dartFile = new File(destination, dartClass.getLibraryName() + ".dart");
                FileUtils.writeLines(dartFile, classGenerator.generate(dartClass, dartClasses));
            }

            File dartFile = new File(destination, "jsonable.dart");
            FileUtils.writeLines(dartFile, classGenerator.generateJsonable());
        } catch (Exception e) {
            throw new MojoExecutionException("Dart model generation failed.", e);
        }
    }
}