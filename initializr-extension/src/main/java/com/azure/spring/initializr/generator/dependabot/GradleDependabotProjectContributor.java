package com.azure.spring.initializr.generator.dependabot;

import io.spring.initializr.generator.project.contributor.SingleResourceProjectContributor;


public class GradleDependabotProjectContributor extends SingleResourceProjectContributor {

    public GradleDependabotProjectContributor() {
        this("classpath:dependabot/gradle/dependabot.yml");
    }

    public GradleDependabotProjectContributor(String resourcePattern) {
        super(".github/dependabot.yml", resourcePattern);
    }

}
