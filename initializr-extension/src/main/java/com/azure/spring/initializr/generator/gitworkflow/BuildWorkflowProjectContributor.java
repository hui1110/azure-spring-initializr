package com.azure.spring.initializr.generator.gitworkflow;

import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.contributor.ProjectContributor;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public class BuildWorkflowProjectContributor implements ProjectContributor {

    private final GithubWorkflow githubWorkflow;

    private final ProjectDescription description;


    public BuildWorkflowProjectContributor(GithubWorkflow githubWorkflow, ProjectDescription description) {
        this.githubWorkflow = githubWorkflow;
        this.description = description;
    }

    @Override
    public void contribute(Path projectRoot) throws IOException {
        if(!Files.isDirectory(projectRoot.resolve(".github/workflows"))) {
            Files.createDirectories(projectRoot.resolve(".github/workflows"));
        }
        Files.exists(projectRoot.resolve(".github/workflows/"));
        Path file = Files.createFile(projectRoot.resolve(".github/workflows/" + description.getBuildSystem().id() + ".yml"));
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(file))) {
            this.githubWorkflow.write(writer);
        }
    }

}
