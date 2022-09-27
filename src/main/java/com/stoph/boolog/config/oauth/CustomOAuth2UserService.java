package com.stoph.boolog.config.oauth;

import com.stoph.boolog.config.dto.OAuthAttributes;
import com.stoph.boolog.config.dto.SessionMember;
import com.stoph.boolog.domain.member.Member;
import com.stoph.boolog.domain.member.MemberRepository;
import com.stoph.boolog.domain.member.Role;
import com.stoph.boolog.web.dto.MemberUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;
    private final HttpSession httpSession;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        //provider name
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes
                .of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        Member member = saveOrUpdate(attributes);
        httpSession.setAttribute("member", new SessionMember(member));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(member.getRole().getKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );
    }

    private Member saveOrUpdate(OAuthAttributes attributes) {

        Member member = Member.builder()
                .email(attributes.getEmail())
                .password(passwordEncoder.encode("boolog"))
                .name(attributes.getName())
                .picture(attributes.getPicture())
                .provider(attributes.getProvider())
                .providerId(attributes.getProviderId())
                .role(Role.USER)
                .build();

        Member findMember = memberRepository.findByEmail(member.getEmail()).orElse(null);

        if (findMember != null) {
            MemberUpdateRequestDto updateParam = MemberUpdateRequestDto.builder()
                    .password(member.getPassword())
                    .picture(member.getPicture())
                    .build();

            memberRepository.update(member.getId(), updateParam);

            return memberRepository.findByEmail(member.getEmail()).get();
        }

        return memberRepository.findById(memberRepository.save(member)).get();
    }
}
