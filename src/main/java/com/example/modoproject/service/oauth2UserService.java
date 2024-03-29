package com.example.modoproject.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static com.mysql.cj.conf.PropertyKey.logger;

@Service
public class oauth2UserService extends DefaultOAuth2UserService {

    private static final Logger logger = Logger.getLogger(oauth2UserService.class.getName());

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        //User Info 로거
        Map<String, Object> attributes = oAuth2User.getAttributes();
        for (Map.Entry<String, Object> entry : attributes.entrySet()) {
            logger.info("userInfo key " + entry.getKey());
            logger.info("userInfo value " + entry.getValue().toString());

        }
        // Role generate
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_USER");

        // nameAttributeKey
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        // DB에 사용자 정보 로직 추가 가능

        return new DefaultOAuth2User(authorities, oAuth2User.getAttributes(), userNameAttributeName);
    }

}
