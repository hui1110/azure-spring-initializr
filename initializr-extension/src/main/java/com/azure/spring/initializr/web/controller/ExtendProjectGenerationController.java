package com.azure.spring.initializr.web.controller;

import com.azure.spring.initializr.extension.scm.push.common.exception.OAuthAppException;
import com.azure.spring.initializr.extension.scm.push.common.service.GitService;
import com.azure.spring.initializr.extension.scm.push.common.service.GitServiceFactoryResolver;
import com.azure.spring.initializr.web.ResultCode;
import com.azure.spring.initializr.web.project.ExtendProjectRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.spring.initializr.metadata.InitializrMetadataProvider;
import io.spring.initializr.web.controller.ProjectGenerationController;
import io.spring.initializr.web.project.ProjectGenerationInvoker;
import io.spring.initializr.web.project.ProjectGenerationResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExtendProjectGenerationController extends ProjectGenerationController<ExtendProjectRequest> {

    private final ProjectGenerationInvoker<ExtendProjectRequest> projectGenerationInvoker;

    private final GitServiceFactoryResolver gitServiceFactoryResolver;

    public ExtendProjectGenerationController(InitializrMetadataProvider metadataProvider,
                                             ProjectGenerationInvoker<ExtendProjectRequest> projectGenerationInvoker,
                                             GitServiceFactoryResolver gitServiceFactoryResolver) {
        super(metadataProvider, projectGenerationInvoker);
        this.gitServiceFactoryResolver = gitServiceFactoryResolver;
        this.projectGenerationInvoker = projectGenerationInvoker;
    }

    @Override
    public ExtendProjectRequest projectRequest(Map<String, String> headers) {
        ExtendProjectRequest request = new ExtendProjectRequest();
        request.getParameters().putAll(headers);
        request.initialize(getMetadata());
        return request;
    }

    @RequestMapping(path = "/login/oauth2/code")
    public String pushToGitRepository(ExtendProjectRequest request) {
        if (StringUtils.isNotBlank(request.getGitServiceType())
                && StringUtils.isNotBlank(request.getCode())) {
            ProjectGenerationResult result = this.projectGenerationInvoker.invokeProjectStructureGeneration(request);
            try {
                GitService gitService = gitServiceFactoryResolver.resolve(request.getGitServiceType())
                                                                 .getGitService(request.getCode());
                if(request.getAction().equals("codespaces")) {
                    return "redirect:" + gitService.createCodeSpaces(request, result);
                } else if(request.getAction().equals("push")) {
                    String gitRepositoryUrl = gitService.pushProjectToGitRepository(request, result);
                    return redirectUriString(request, ResultCode.CODE_SUCCESS.getCode(), gitRepositoryUrl);
                } else {
                    throw new IllegalArgumentException(("unknown action: " + request.getAction()));
                }
            } catch (RuntimeException exception) {
                throw exception;
            } finally {
                this.projectGenerationInvoker.cleanTempFiles(result.getRootDirectory());
            }
        }
        return redirectUriString(request);
    }

    private String redirectUriString(ExtendProjectRequest request) {
        return redirectUriString(request, null, null);
    }

    private String redirectUriString(ExtendProjectRequest request, String errorCode, String msg) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.newInstance();
        if (errorCode != null && msg != null) {
            uriComponentsBuilder.queryParam("errorcode", errorCode).queryParam("msg", msg);
        }

        if (request != null) {
            uriComponentsBuilder
                    .queryParam("name", request.getName())
                    .queryParam("type", request.getType())
                    .queryParam("language", request.getLanguage())
                    .queryParam("architecture", request.getArchitecture())
                    .queryParam("packaging", request.getPackaging())
                    .queryParam("groupId", request.getGroupId())
                    .queryParam("artifactId", request.getArtifactId())
                    .queryParam("description", request.getDescription())
                    .queryParam("packageName", request.getPackageName())
                    .queryParam("platformVersion", request.getBootVersion())
                    .queryParam("jvmVersion", request.getJavaVersion())
                    .queryParam("fromSpringInitializr", request.getFromSpringInitializr());
            if (!request.getDependencies().isEmpty()) {
                uriComponentsBuilder.queryParam("dependencies", String.join(",", request.getDependencies()));
            }
        }
        return "redirect:/#!" + uriComponentsBuilder.toUriString();
    }

    @ExceptionHandler(value = {IllegalArgumentException.class, OAuthAppException.class})
    public String invalidProjectRequest(RuntimeException ex, HttpServletRequest httpServletRequest) {
        Map<String, String> map = new HashMap<>();
        Enumeration<String> parameterNames = httpServletRequest.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            map.put(name, httpServletRequest.getParameter(name));
        }
        ObjectMapper mapper = new ObjectMapper();
        String stringDependencies = String.valueOf(map.get("dependencies"));
        map.remove("dependencies");
        ExtendProjectRequest request = mapper.convertValue(map, ExtendProjectRequest.class);
        if (!"null".equals(stringDependencies)) {
            List<String> dependencies = Arrays.asList(stringDependencies.split(","));
            request.setDependencies(dependencies);
        }
        String errorCode = ResultCode.CODE_SUCCESS.getCode();
        if (ex instanceof IllegalArgumentException) {
            errorCode = ResultCode.INVALID_PARAM.getCode();
        } else if (ex instanceof OAuthAppException) {
            errorCode = ResultCode.OAUTHAPP_EXCEPTION.getCode();
        }
        return redirectUriString(request, errorCode, ex.getMessage());
    }
}
