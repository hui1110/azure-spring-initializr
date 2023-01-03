package com.azure.spring.initializr.generator.architecture;

import com.azure.spring.initializr.generator.project.ExtendProjectDescription;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.contributor.ProjectContributor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Predicate;

public class MicroServiceProjectContributor implements ProjectContributor {

    private final PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

    private final String rootResource;

    private final Predicate<String> executable;

    private final ProjectDescription projectDescription;

    public MicroServiceProjectContributor(ProjectDescription projectDescription, String rootResource) {
        this(projectDescription, rootResource, (filename) -> false);
    }

    public MicroServiceProjectContributor(ProjectDescription projectDescription, String rootResource, Predicate<String> executable) {
        this.rootResource = StringUtils.trimTrailingCharacter(rootResource, '/');
        this.executable = executable;
        this.projectDescription = projectDescription;
    }

    @Override
    public void contribute(Path projectRoot) throws IOException {
        if(projectDescription instanceof ExtendProjectDescription) {
            ExtendProjectDescription extendProjectDescription = (ExtendProjectDescription) projectDescription;
            if(!extendProjectDescription.getArchitecture().equals("microservice")) {
                return;
            }
            Resource root = this.resolver.getResource(this.rootResource);
            Resource[] resources = this.resolver.getResources(this.rootResource + "/**");
            for (Resource resource : resources) {
                if (resource.isReadable()) {
                    String filename = extractFileName(root.getURI(), resource.getURI());
                    Path output = projectRoot.resolve(filename);
                    Files.createDirectories(output.getParent());
                    Files.createFile(output);
                    FileCopyUtils.copy(resource.getInputStream(), Files.newOutputStream(output));
                    // TODO Set executable using NIO
                    output.toFile().setExecutable(this.executable.test(filename));
                }
            }
        }
    }

    private String extractFileName(URI root, URI resource) {
        String candidate = resource.toString().substring(root.toString().length());
        return StringUtils.trimLeadingCharacter(candidate, '/');
    }

}
