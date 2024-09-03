package com.example.modoproject.login.service;

import com.example.modoproject.login.entity.User;
import com.example.modoproject.login.entity.UserInfo;
import com.example.modoproject.login.repository.UserInfoRepository;
import com.example.modoproject.login.repository.userRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class oauth2UserService extends DefaultOAuth2UserService {

    private static final Logger logger = Logger.getLogger(oauth2UserService.class.getName());

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private userRepository userRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);



        // User Info 가져오기
        Map<String, Object> attributes = oAuth2User.getAttributes();
        logger.info("userInfo value " + attributes.toString());

        // 사용자 ID 가져오기
        String externalId = null;
        if (attributes.containsKey("id")) {
            externalId = attributes.get("id").toString();
        } else if (attributes.containsKey("sub")) { // OpenID Connect 규격을 따르는 제공자 (예: Google)
            externalId = attributes.get("sub").toString();
        }

        // 닉네임 가져오기
        String nickname = null;
        if (attributes.containsKey("nickname")) {
            nickname = (String) attributes.get("nickname");
        } else if (attributes.containsKey("kakao_account")) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            if (kakaoAccount.containsKey("profile")) {
                Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
                nickname = (String) profile.get("nickname");
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
                profileImageUrl = (String) profile.get("profile_image_url");
            }
        }

        // Role 가져오기
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        String role = "ROLE_USER";

        httpSession.setAttribute("externalId", externalId);

        // 사용자 정보를 세션에 저장 및 데이터베이스에 저장
        User user = userRepository.findByExternalId(externalId);
        if (user == null) {
            user = new User();
            user.setExternalId(externalId);
            user.setNickname(nickname);
            user.setProfileImageUrl(profileImageUrl);
            user.setClientId(userRequest.getClientRegistration().getRegistrationId());
            user.setProviderName(userRequest.getClientRegistration().getProviderDetails().getTokenUri());
            user.setRole(role);
            user = userRepository.save(user);
        }

        httpSession.setAttribute("user", user);

        // 유저 정보 가져오기
        List<UserInfo> userInfoList = userInfoRepository.findByExternalId(externalId);
        if (!userInfoList.isEmpty()) {
            UserInfo userInfo = userInfoList.get(0);
            httpSession.setAttribute("userInfo", userInfo); // 세션에 저장
        }

        // Role 생성
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(role);

        return new DefaultOAuth2User(authorities, oAuth2User.getAttributes(), userNameAttributeName);
    }

    // externalId로 User를 조회하여 Role을 ROLE_OWNER로 변경하는 메소드
    @Transactional
    public void updateUserRoleToOwner(String externalId) {
        User user = userRepository.findByExternalId(externalId);
        if (user != null) {
            user.setRole("ROLE_OWNER");
            userRepository.save(user);
        }
    }
}
