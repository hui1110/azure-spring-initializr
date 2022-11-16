package com.azure.spring.initializr.generator.gitworkflow;

import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.contributor.ProjectContributor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class BuildWorkflowProjectContributor implements ProjectContributor {

    private final GithubWorkflow githubWorkflow;

    private final ProjectDescription description;

    private final PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();


    public BuildWorkflowProjectContributor(GithubWorkflow githubWorkflow, ProjectDescription description) {
        this.githubWorkflow = githubWorkflow;
        this.description = description;
    }

    @Override
    public void contribute(Path projectRoot) throws IOException {
        Path output = projectRoot.resolve(".github/workflows/" + description.getBuildSystem().id() + ".yml");
        if (!Files.exists(output)) {
            Files.createDirectories(output.getParent());
            Files.createFile(output);
        }
        Resource resource = this.resolver.getResource("classpath:workflows/" + description.getBuildSystem().id() + "-build.yml");
        FileCopyUtils.copy(resource.getInputStream(), Files.newOutputStream(output, StandardOpenOption.APPEND));

        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(output))) {

            String templateYaml = Files.readString(resolver.
                    getResource("classpath:workflows/" + description.getBuildSystem().id() + "-build.yml").getFile().toPath());

            String yaml = templateYaml.replace("${push-branches}", "main")
                    .replace("${pull-request-branches}", "main")
                    .replace("${jdk-version}", description.getLanguage().jvmVersion());

            writer.write(yaml);
        }
    }

}
