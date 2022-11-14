package com.azure.spring.initializr.generator.dependabot;

import io.spring.initializr.generator.project.contributor.SingleResourceProjectContributor;


public class MavenDependabotProjectContributor extends SingleResourceProjectContributor {

    public MavenDependabotProjectContributor() {
        this("classpath:dependabot/maven/dependabot.yml");
    }

    public MavenDependabotProjectContributor(String resourcePattern) {
        super(".github/dependabot.yml", resourcePattern);
    }

}
