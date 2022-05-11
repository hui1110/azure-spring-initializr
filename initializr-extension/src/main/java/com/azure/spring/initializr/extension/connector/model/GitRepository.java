package com.azure.spring.initializr.extension.connector.model;

import java.io.File;

public class GitRepository {
    private String ownerName;

    private String token;

    private String initialBranch;

    private String httpTransportUrl;

    private File templateFile;

    private String email;

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getInitialBranch() {
        return initialBranch;
    }

    public void setInitialBranch(String initialBranch) {
        this.initialBranch = initialBranch;
    }

    public String getHttpTransportUrl() {
        return httpTransportUrl;
    }

    public void setHttpTransportUrl(String httpTransportUrl) {
        this.httpTransportUrl = httpTransportUrl;
    }

    public File getTemplateFile() {
        return templateFile;
    }

    public void setTemplateFile(File templateFile) {
        this.templateFile = templateFile;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
