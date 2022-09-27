package com.stoph.boolog.domain.member;

import com.stoph.boolog.web.dto.MemberResponseDto;
import com.stoph.boolog.web.dto.MemberUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Repository
public class MemberRepositoryImpl implements MemberRepository{

    private final MemberMapper memberMapper;

    @Override
    public Long save(Member member) {
        memberMapper.save(member);
        return member.getId();
    }

    @Override
    public void update(Long id, MemberUpdateRequestDto requestDto) {
        memberMapper.update(id, requestDto);
    }

    @Override
    public Optional<Member> findById(Long id) {
        return memberMapper.findById(id);
    }

    @Override
    public Optional<Member> findByName(String name) {
        return memberMapper.findByName(name);
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return memberMapper.findByEmail(email);
    }

    @Override
    public void delete(Long id) {
        memberMapper.delete(id);
    }

}
