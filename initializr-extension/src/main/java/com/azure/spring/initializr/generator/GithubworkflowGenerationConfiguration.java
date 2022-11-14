package com.azure.spring.initializr.generator;

import com.azure.spring.initializr.generator.gitworkflow.BuildWorkflowProjectContributor;
import com.azure.spring.initializr.generator.gitworkflow.GithubWorkflow;
import io.spring.initializr.generator.buildsystem.gradle.GradleBuildSystem;
import io.spring.initializr.generator.buildsystem.maven.MavenBuildSystem;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@ProjectGenerationConfiguration
public class GithubworkflowGenerationConfiguration {

    @Bean
    public GithubWorkflow buildWorkflow(ProjectDescription description) {
        GithubWorkflow workflow = new GithubWorkflow();

        workflow.setName("Java CI with " + StringUtils.capitalize(description.getBuildSystem().id()));

        GithubWorkflow.Trigger trigger = new GithubWorkflow.Trigger();
        GithubWorkflow.Trigger.Push push = new GithubWorkflow.Trigger.Push();
        push.getBranches().add("main");
        trigger.setPush(push);

        GithubWorkflow.Trigger.PullRequest pullRequest = new GithubWorkflow.Trigger.PullRequest();
        pullRequest.getBranches().add("main");
        trigger.setPullRequest(pullRequest);
        workflow.setTo(trigger);

        GithubWorkflow.Job buildJob = new GithubWorkflow.Job();
        buildJob.setRunsOn("ubuntu-latest");

        buildJob.setSteps(buildSteps4BuildWorkflow(description));

        workflow.getJobs().put("build", buildJob);

        return workflow;
    }

    private List<GithubWorkflow.Job.Step> buildSteps4BuildWorkflow(ProjectDescription description) {
        List<GithubWorkflow.Job.Step> stepList = new ArrayList<>();

        GithubWorkflow.Job.Step checkOutStep = new GithubWorkflow.Job.Step();
        checkOutStep.setUses("actions/checkout@v3");
        stepList.add(checkOutStep);
        GithubWorkflow.Job.Step setupJava = new GithubWorkflow.Job.Step();
        setupJava.setName("Set up JDK " + description.getLanguage().jvmVersion());
        setupJava.setUses("actions/setup-java@v3");
        setupJava.buildWith().put("java-version", description.getLanguage().jvmVersion());
        setupJava.buildWith().put("distribution", "microsoft");
        setupJava.buildWith().put("cache", description.getBuildSystem().id());
        stepList.add(setupJava);

        if(description.getBuildSystem().id().equals(GradleBuildSystem.ID)) {
            GithubWorkflow.Job.Step gradleValidateStep = new GithubWorkflow.Job.Step();
            gradleValidateStep.setName("Validate Gradle wrapper");
            gradleValidateStep.setUses("gradle/wrapper-validation-action@e6e38bacfdf1a337459f332974bb2327a31aaf4b");
            stepList.add(gradleValidateStep);
        }

        GithubWorkflow.Job.Step buildStep = new GithubWorkflow.Job.Step();
        buildStep.setName("Build with " + StringUtils.capitalize(description.getBuildSystem().id()));
        if(description.getBuildSystem().id().equals(MavenBuildSystem.ID)) {
            buildStep.setUses("mvn -B clean package --file pom.xml -P full");
        } else { // gradle
            buildStep.setUses("gradle/gradle-build-action@67421db6bd0bf253fb4bd25b31ebb98943c375e1");
            buildStep.buildWith().put("arguments", "build");
        }
        stepList.add(buildStep);

        return stepList;
    }


    @Bean
    public BuildWorkflowProjectContributor buildWorkflowProjectContributor(GithubWorkflow buildWorkflow, ProjectDescription description) {
        return new BuildWorkflowProjectContributor(buildWorkflow, description);
    }

}
