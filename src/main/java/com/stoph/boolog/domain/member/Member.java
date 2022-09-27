package com.stoph.boolog.domain.member;

import com.stoph.boolog.web.dto.MemberResponseDto;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class Member {

    private Long id;
    private String email;
    private String password;
    private String name;
    private String picture;
    private String introduction;
    private String provider;
    private String providerId;
    private Role role;
    private LocalDateTime regDate;

    @Builder
    public Member(String email, String password, String name, String picture,String introduction, Role role, String provider, String providerId) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.picture = picture;
        this.introduction = introduction;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
    }

    public MemberResponseDto toResponseDto() {
        return MemberResponseDto.builder()
                .email(this.email)
                .name(this.name)
                .picture(this.picture)
                .introduction(this.introduction)
                .regDate(this.regDate)
                .build();
    }
}
