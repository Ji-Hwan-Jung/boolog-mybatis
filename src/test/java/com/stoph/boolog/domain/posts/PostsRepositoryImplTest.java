package com.stoph.boolog.domain.posts;

import com.stoph.boolog.domain.member.Member;
import com.stoph.boolog.domain.member.MemberRepository;
import com.stoph.boolog.domain.member.Role;
import com.stoph.boolog.service.PostsService;
import com.stoph.boolog.web.dto.MemberResponseDto;
import com.stoph.boolog.web.dto.PostsResponseDto;
import com.stoph.boolog.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@Transactional
@SpringBootTest
class PostsRepositoryImplTest {

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    private Long memberId;

    @BeforeEach
    void setup() {
        Member member = Member.builder()
                .email("test@gmail.com")
                .password(encoder.encode("1234"))
                .name("test")
                .role(Role.USER)
                .build();

        Long save = memberRepository.save(member);
        log.info("memberId = {}", save);
        memberId = save;
    }

    /*NOTE
     * equals() 를 오버라이딩 하지 않아도 == 비교에서 왜 true가 반환되는지 찾아보기
     */
    @DisplayName("게시글 저장")
    @Test
    void save() {
        //given
//        Member member = Member.builder()
//                .email("test@gmail.com")
//                .password(encoder.encode("1234"))
//                .name("test")
//                .role(Role.USER)
//                .build();
//
//        Long memberId = memberRepository.save(member);

        Posts posts = Posts.builder()
                .memberId(memberId)
                .title("title1")
                .content("content1")
                .build();

        //when
        postsRepository.save(posts);
        PostsResponseDto savedPosts = postsRepository.findById(posts.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다"));

        //then
        log.info("posts.getClass={}", posts.getClass());
        log.info("savedPosts.getClass={}", savedPosts.getClass());
        assertThat(savedPosts.getTitle()).isEqualTo(posts.getTitle());
        assertThat(savedPosts.getContent()).isEqualTo(posts.getContent());
    }

    @DisplayName("게시글 수정")
    @Test
    void update() {
        //given
//        Member member = Member.builder()
//                .email("test@gmail.com")
//                .password(encoder.encode("1234"))
//                .name("test")
//                .role(Role.USER)
//                .build();
//
//        Long memberId = memberRepository.save(member);

        Posts posts = Posts.builder()
                .memberId(memberId)
                .title("title1")
                .content("content1")
                .build();

        postsRepository.save(posts);
        PostsResponseDto savedPosts = postsRepository.findById(posts.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다"));

        Long id = savedPosts.getId();
        PostsUpdateRequestDto updateParam = PostsUpdateRequestDto.builder()
                .title("title2")
                .content("content2")
                .build();

        //when
        postsRepository.update(id, updateParam);

        //then
        PostsResponseDto findPosts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다"));

        assertThat(findPosts.getTitle()).isEqualTo(updateParam.getTitle());
        assertThat(findPosts.getContent()).isEqualTo(updateParam.getContent());
    }

    @DisplayName("게시글 단건 조회")
    @Test
    void findById() {
        //given
//        Member member = Member.builder()
//                .email("test@gmail.com")
//                .password(encoder.encode("1234"))
//                .name("test")
//                .role(Role.USER)
//                .build();
//
//        Long memberId = memberRepository.save(member);

        Long id = postsRepository.save(Posts.builder()
                .memberId(memberId)
                .title("title")
                .content("content")
                .build());

        //when
        PostsResponseDto findPosts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다"));

        //then
        assertThat(findPosts.getTitle()).isEqualTo("title");
        assertThat(findPosts.getContent()).isEqualTo("content");
    }

    @DisplayName("게시글 전체 조회")
    @Test
    void findAll() {
        //given
//        Member member = Member.builder()
//                .email("test@gmail.com")
//                .password(encoder.encode("1234"))
//                .name("test")
//                .role(Role.USER)
//                .build();
//
//        Long memberId = memberRepository.save(member);

        postsRepository.save(Posts.builder()
                .memberId(memberId)
                .title("title1")
                .content("content1")
                .build());

        postsRepository.save(Posts.builder()
                .memberId(memberId)
                .title("title2")
                .content("content2")
                .build());

        //when
        List<PostsResponseDto> postsList = postsRepository.findAll(new FindPostsCond());

        //then
        assertThat(postsList.size()).isEqualTo(2);
    }

    @DisplayName("게시글 삭제")
    @Test
    void delete() {
//        Member member = Member.builder()
//                .email("test@gmail.com")
//                .password(encoder.encode("1234"))
//                .name("test")
//                .role(Role.USER)
//                .build();
//
//        Long memberId = memberRepository.save(member);

        Long id = postsRepository.save(Posts.builder()
                .memberId(memberId)
                .title("title")
                .content("content")
                .build());

        postsRepository.delete(id);

//        assertThatThrownBy(() -> postsRepository.findById(id)).isInstanceOf(IllegalArgumentException.class);
        postsRepository.findById(id);
    }
}