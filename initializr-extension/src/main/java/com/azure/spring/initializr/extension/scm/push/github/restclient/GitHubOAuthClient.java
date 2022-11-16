package com.azure.spring.initializr.extension.scm.push.github.restclient;

import com.azure.spring.initializr.extension.scm.push.common.exception.OAuthAppException;
import com.azure.spring.initializr.extension.scm.push.common.model.TokenResult;
import com.azure.spring.initializr.extension.scm.push.common.client.OAuthClient;
import com.azure.spring.initializr.metadata.scm.push.OAuthApp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

/** */
public class GitHubOAuthClient implements OAuthClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(GitHubOAuthClient.class);

    private static final String BASE_URI = "https://github.com";
    private static final String ACCESS_TOKEN_PATH = BASE_URI + "/login/oauth/access_token";

    private final OAuthApp oAuthApp;
    private final WebClient webClient;

    public GitHubOAuthClient(OAuthApp oAuthApp, WebClient webClient) {
        this.oAuthApp = oAuthApp;
        this.webClient = webClient;
    }

    @Override
    @NonNull
    public TokenResult getAccessToken(String authorizationCode) {
        Assert.notNull(authorizationCode, "Invalid authorizationCode.");
        Assert.notNull(oAuthApp.getClientId(), "Invalid clientId.");
        Assert.notNull(oAuthApp.getClientSecret(), "Invalid clientSecret.");
        Assert.notNull(oAuthApp.getRedirectUri(), "Invalid redirectUri.");

        TokenResult result;
        try {
            Map<String, String> map = new HashMap<>();
            map.put("client_id", oAuthApp.getClientId());
            map.put("client_secret", oAuthApp.getClientSecret());
            map.put("redirect_uri", oAuthApp.getRedirectUri());
            map.put("code", authorizationCode);
            result = webClient.post()
                              .uri(ACCESS_TOKEN_PATH)
                              .contentType(MediaType.APPLICATION_JSON)
                              .body(BodyInserters.fromValue(map))
                              .accept(MediaType.APPLICATION_JSON)
                              .retrieve()
                              .bodyToMono(TokenResult.class)
                              .block();
        } catch (RuntimeException ex) {
            LOGGER.error("Error while authenticating", ex);
            throw new OAuthAppException("Error while authenticating");
        }
        if (result == null || result.getAccessToken() == null) {
            throw new OAuthAppException("Error while authenticating");
        }
        return result;
    }
}