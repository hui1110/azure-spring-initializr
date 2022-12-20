package com.azure.spring.initializr.generator.readme;

import io.spring.initializr.generator.project.contributor.SingleResourceProjectContributor;

public class ReadmeProjectContributor extends SingleResourceProjectContributor {

    public ReadmeProjectContributor() {
        this("classpath:readme/README.md");
    }

    public ReadmeProjectContributor(String resourcePattern) {
        super("README.md", resourcePattern);
    }

}
