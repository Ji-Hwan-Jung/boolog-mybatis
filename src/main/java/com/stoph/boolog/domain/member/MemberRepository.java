package com.stoph.boolog.domain.member;

import com.stoph.boolog.web.dto.MemberResponseDto;
import com.stoph.boolog.web.dto.MemberUpdateRequestDto;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

public interface MemberRepository {

    Long save(Member member);

    void update(Long id, MemberUpdateRequestDto requestDto);

    Optional<Member> findById(Long id);

    Optional<Member> findByName(String name);

    Optional<Member> findByEmail(String email);

    void delete(Long id);
}
