package hu.roszpapad.konyvklubauth.config;

import hu.roszpapad.konyvklubauth.config.domain.Credentials;
import hu.roszpapad.konyvklubauth.config.repository.CredentialRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

public class CustomTokenEnhancer implements TokenEnhancer {

    @Autowired
    private CredentialRepositoryImpl credentialRepository;


    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        final Map<String, Object> additionalInfo = new HashMap<>();
        Credentials credential = credentialRepository.findUserByUsername(authentication.getName());
        additionalInfo.put("id", credential.getId());
        additionalInfo.put("email", credential.getEmail());
        additionalInfo.put("firstName", credential.getFirstName());
        additionalInfo.put("lastName", credential.getLastName());
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }
}
