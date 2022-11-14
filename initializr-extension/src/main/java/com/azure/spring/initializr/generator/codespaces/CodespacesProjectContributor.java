package com.azure.spring.initializr.generator.codespaces;

import io.spring.initializr.generator.project.contributor.MultipleResourcesProjectContributor;

public class CodespacesProjectContributor extends MultipleResourcesProjectContributor {

    public CodespacesProjectContributor() {
        super("classpath:codespaces");
    }
}
