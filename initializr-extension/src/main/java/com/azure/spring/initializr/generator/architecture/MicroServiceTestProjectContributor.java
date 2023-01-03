package com.azure.spring.initializr.generator.architecture;

import com.azure.spring.initializr.generator.project.ExtendProjectDescription;
import io.spring.initializr.generator.project.ProjectDescription;
import io.spring.initializr.generator.project.contributor.MultipleResourcesProjectContributor;
import io.spring.initializr.generator.project.contributor.ProjectContributor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Predicate;

public class MicroServiceTestProjectContributor extends MultipleResourcesProjectContributor {

    public MicroServiceTestProjectContributor() {
        super("classpath:architecture.microservice");
    }

}
