package com.example.modoproject.service;

import com.example.modoproject.entity.User;
import com.example.modoproject.repository.userRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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

@Service
public class oauth2UserService extends DefaultOAuth2UserService {

    private static final Logger logger = Logger.getLogger(oauth2UserService.class.getName());

@Autowired
private userRepository userRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // User Info 로깅
        Map<String, Object> attributes = oAuth2User.getAttributes();
        logger.info("userInfo value " + attributes.toString());

        // 닉네임 가져오기
        String nickname = null;
        if (attributes.containsKey("nickname")) {
            nickname = (String) attributes.get("nickname");
        } else if (attributes.containsKey("kakao_account")) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            if (kakaoAccount.containsKey("profile")) {
                Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
                if (profile.containsKey("nickname")) {
                    nickname = (String) profile.get("nickname");
                }
            }
        }

        // 프로필 이미지 URL 가져오기
        String profileImageUrl = null;
        if (attributes.containsKey("profile_image_url")) {
            profileImageUrl = (String) attributes.get("profile_image_url");
        } else if (attributes.containsKey("kakao_account")) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            if (kakaoAccount.containsKey("profile")) {
                Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
                if (profile.containsKey("profile_image_url")) {
                    profileImageUrl = (String) profile.get("profile_image_url");
                }
            }
        }

        // 역할 정보 가져오기 (권한 목록을 가져와서 문자열로 변환하는 작업이 필요할 수 있음)
        String role = "ROLE_USER"; // 기본 역할 설정

        logger.info("닉네임: " + nickname);
        logger.info("프로필 이미지 URL: " + profileImageUrl);
        logger.info("역할: " + role);

        // DB에 저장
        User user = new User();
        user.setNickname(nickname);
        user.setProfileImage(profileImageUrl);
        user.setRole(role);

        user.setClientId(userRequest.getClientRegistration().getRegistrationId());
        user.setProviderName(userRequest.getClientRegistration().getProviderDetails().getTokenUri());
        userRepository.save(user);

        // nameAttributeKey
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        // Role 생성
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(role);

        return new DefaultOAuth2User(authorities, oAuth2User.getAttributes(), userNameAttributeName);
    }
}