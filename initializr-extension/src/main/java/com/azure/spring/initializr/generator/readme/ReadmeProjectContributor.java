package com.azure.spring.initializr.generator.readme;

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

public class ReadmeProjectContributor implements ProjectContributor {

    private final PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

    private final ProjectDescription description;

    public ReadmeProjectContributor(ProjectDescription description) {
        this.description = description;
    }

    @Override
    public void contribute(Path projectRoot) throws IOException {
        Path output = projectRoot.resolve("README.md");
        if (!Files.exists(output)) {
            Files.createFile(output);
        }
        Resource resource = this.resolver.getResource("classpath:readme/README.md");
        FileCopyUtils.copy(resource.getInputStream(), Files.newOutputStream(output, StandardOpenOption.APPEND));

        String templateYaml = Files.readString(output);

        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(output))) {
            String yaml = templateYaml
                                      .replace("${repo}", description.getName());
//                                      .replace("${owner}", description.getLanguage().jvmVersion());
            writer.write(yaml);
        }
    }
}
