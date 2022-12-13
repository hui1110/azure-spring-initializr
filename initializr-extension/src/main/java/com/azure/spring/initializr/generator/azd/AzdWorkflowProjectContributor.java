package com.azure.spring.initializr.generator.azd;

import io.spring.initializr.generator.project.contributor.SingleResourceProjectContributor;

public class AzdWorkflowProjectContributor extends SingleResourceProjectContributor {

    public AzdWorkflowProjectContributor() {
        this("classpath:azd/workflow/azure-dev.yml");
    }

    public AzdWorkflowProjectContributor(String resourcePattern) {
        super(".github/workflows/azure-dev.yml", resourcePattern);
    }

}
