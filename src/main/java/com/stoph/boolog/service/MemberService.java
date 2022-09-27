package com.stoph.boolog.service;

import com.stoph.boolog.domain.member.Member;
import com.stoph.boolog.domain.member.MemberRepository;
import com.stoph.boolog.web.dto.MemberResponseDto;
import com.stoph.boolog.web.dto.MemberUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository repository;
    private final HttpSession httpSession;

    @Transactional
    public Long save(Member member) {
        return repository.save(member);
    }

    @Transactional
    public void update(Long id, MemberUpdateRequestDto requestDto) {
        repository.update(id, requestDto);
    }

    public Member findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));
    }

    public Member findByName(String name) {
        return repository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다"));
    }

    public Member findByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));
    }

    public void delete(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));

        repository.delete(id);
    }
}
