package com.azure.spring.initializr.generator.azd;

import io.spring.initializr.generator.project.contributor.SingleResourceProjectContributor;

public class AzdMetadataProjectContributor extends SingleResourceProjectContributor {

    public AzdMetadataProjectContributor() {
        this("classpath:azd/metadata/azure.yaml");
    }

    public AzdMetadataProjectContributor(String resourcePattern) {
        super("azure.yaml", resourcePattern);
    }

}
