package com.azure.spring.initializr.generator.azd;

import io.spring.initializr.generator.project.contributor.MultipleResourcesProjectContributor;

public class AzdInfraProjectContributor extends MultipleResourcesProjectContributor {

    public AzdInfraProjectContributor() {
        super("classpath:azd-infra-aca-bicep");
    }

}
