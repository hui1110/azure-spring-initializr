package com.azure.spring.initializr.generator;

import com.azure.spring.initializr.generator.codespaces.CodespacesProjectContributor;
import com.azure.spring.initializr.generator.dependabot.GradleDependabotProjectContributor;
import com.azure.spring.initializr.generator.dependabot.MavenDependabotProjectContributor;
import io.spring.initializr.generator.buildsystem.gradle.GradleBuildSystem;
import io.spring.initializr.generator.buildsystem.maven.MavenBuildSystem;
import io.spring.initializr.generator.condition.ConditionalOnBuildSystem;
import io.spring.initializr.generator.project.ProjectGenerationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ProjectGenerationConfiguration
public class AzureGenerationConfiguration {

    @Bean
    public CodespacesProjectContributor codespacesProjectContributor() {
        return new CodespacesProjectContributor();
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
