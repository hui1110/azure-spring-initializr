package com.azure.spring.initializr.generator;

import com.azure.spring.initializr.generator.architecture.MicroServiceApplicationDatabasePropertiesContributor;
import com.azure.spring.initializr.generator.architecture.MicroServiceApplicationPropertiesContributor;
import com.azure.spring.initializr.generator.architecture.MicroServiceProjectContributor;
import com.azure.spring.initializr.generator.architecture.MicroServiceTestProjectContributor;
import com.azure.spring.initializr.generator.azd.AzdInfraProjectContributor;
import com.azure.spring.initializr.generator.azd.AzdMetadataProjectContributor;
import com.azure.spring.initializr.generator.azd.AzdWorkflowProjectContributor;
import com.azure.spring.initializr.generator.codespaces.CodespacesProjectContributor;
import com.azure.spring.initializr.generator.dependabot.GradleDependabotProjectContributor;
import com.azure.spring.initializr.generator.dependabot.MavenDependabotProjectContributor;
import com.azure.spring.initializr.generator.readme.ReadmeProjectContributor;
import io.spring.initializr.generator.buildsystem.gradle.GradleBuildSystem;
import io.spring.initializr.generator.buildsystem.maven.MavenBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnBuildSystem;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ProjectGenerationConfiguration
public class AzureGenerationConfiguration {

    @Bean
    public CodespacesProjectContributor codespacesProjectContributor() {
        return new CodespacesProjectContributor();
    }

    @Bean
    public AzdWorkflowProjectContributor azdWorkflowProjectContributor() {
        return new AzdWorkflowProjectContributor();
    }

    @Bean
    public AzdMetadataProjectContributor azdMetadataProjectContributor() {
        return new AzdMetadataProjectContributor();
    }

    @Bean
    public AzdInfraProjectContributor azdInfraProjectContributor() {
        return new AzdInfraProjectContributor();
    }

    @Bean
    public ReadmeProjectContributor readmeProjectContributor(ProjectDescription projectDescription) {
        return new ReadmeProjectContributor(projectDescription);
    }

    @Bean
    public MicroServiceProjectContributor microServiceProjectContributor(ProjectDescription projectDescription) {
        return new MicroServiceProjectContributor(projectDescription, "classpath:architecture/microservice/code");
    }

    @Bean
    public MicroServiceApplicationPropertiesContributor microServiceApplicationPropertiesContributor(ProjectDescription projectDescription) {
        return new MicroServiceApplicationPropertiesContributor(projectDescription, "src/main/resources/application.properties", "classpath:architecture/microservice/configuration/application.properties");
    }

    @Bean
    public MicroServiceApplicationDatabasePropertiesContributor microServiceApplicationDatabasePropertiesContributor(ProjectDescription projectDescription) {
        return new MicroServiceApplicationDatabasePropertiesContributor(projectDescription, "src/main/resources/schema-all.sql", "classpath:architecture/microservice/database/schema-all.sql");
    }

    @Configuration
    public static class DependabotConfiguration {

        @Bean
        @ConditionalOnBuildSystem(MavenBuildSystem.ID)
        public MavenDependabotProjectContributor mavenDependabotProjectContributor() {
            return new MavenDependabotProjectContributor();
        }

        @Bean
        @ConditionalOnBuildSystem(GradleBuildSystem.ID)
        public GradleDependabotProjectContributor gradleDependabotProjectContributor() {
            return new GradleDependabotProjectContributor();
        }

    }

}
