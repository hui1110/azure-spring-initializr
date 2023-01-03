package com.azure.spring.initializr.extension.dependency.springazure.architecture;

import com.azure.spring.initializr.generator.project.ExtendProjectDescription;
import io.spring.initializr.generator.buildsystem.Build;
import io.spring.initializr.generator.buildsystem.Dependency;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.spring.build.BuildCustomizer;
import io.spring.initializr.generator.version.VersionReference;

public class TodoAppCustomizer implements BuildCustomizer<Build> {

    private final ProjectDescription projectDescription;

    public TodoAppCustomizer(ProjectDescription projectDescription) {
        this.projectDescription = projectDescription;
    }

    @Override
    public void customize(Build build) {
        if(projectDescription instanceof ExtendProjectDescription) {
            ExtendProjectDescription extendProjectDescription = (ExtendProjectDescription) projectDescription;
            if(!extendProjectDescription.getArchitecture().equals("microservice")) {
                return;
            }
            build.dependencies().add("springdoc-openapi-ui",
                Dependency.withCoordinates("org.springdoc", "springdoc-openapi-ui").version(VersionReference.ofValue("1.6.11")));
        }
    }

}