package com.azure.spring.initializr.generator.architecture;

import com.azure.spring.initializr.generator.project.ExtendProjectDescription;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.contributor.ProjectContributor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class MicroServiceApplicationPropertiesContributor implements ProjectContributor {

    private final PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

    private final String relativePath;

    private final String resourcePattern;

    private final ProjectDescription projectDescription;

    public MicroServiceApplicationPropertiesContributor(ProjectDescription projectDescription, String relativePath, String resourcePattern) {
        this.relativePath = relativePath;
        this.resourcePattern = resourcePattern;
        this.projectDescription = projectDescription;
    }

    @Override
    public void contribute(Path projectRoot) throws IOException {
        if(projectDescription instanceof ExtendProjectDescription) {
            ExtendProjectDescription extendProjectDescription = (ExtendProjectDescription) projectDescription;
            if (!extendProjectDescription.getArchitecture().equals("microservice")) {
                return;
            }
            Path output = projectRoot.resolve(this.relativePath);
            if (!Files.exists(output)) {
                Files.createDirectories(output.getParent());
                Files.createFile(output);
            }
            Resource resource = this.resolver.getResource(this.resourcePattern);
            FileCopyUtils.copy(resource.getInputStream(), Files.newOutputStream(output, StandardOpenOption.APPEND));
        }
    }

}
