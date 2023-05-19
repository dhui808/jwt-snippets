package com.example.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("oauth")
public class OAuthProperties {
	
    private String clientId;
    private String clientSecret;
    private String checkTokenUrl;
    private String userInfoUrl;
    
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getClientSecret() {
		return clientSecret;
	}
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	public String getCheckTokenUrl() {
		return checkTokenUrl;
	}
	public void setCheckTokenUrl(String checkTokenUrl) {
		this.checkTokenUrl = checkTokenUrl;
	}
	public String getUserInfoUrl() {
		return userInfoUrl;
	}
	public void setUserInfoUrl(String userInfoUrl) {
		this.userInfoUrl = userInfoUrl;
	}

}