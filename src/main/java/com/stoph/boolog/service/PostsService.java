package com.stoph.boolog.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.stoph.boolog.domain.posts.FindPostsCond;
import com.stoph.boolog.domain.posts.Posts;
import com.stoph.boolog.domain.posts.PostsRepository;
import com.stoph.boolog.web.dto.PostsResponseDto;
import com.stoph.boolog.web.dto.PostsUpdateRequestDto;
import com.stoph.boolog.web.dto.TagResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository repository;

    @Transactional
    public Long save(Posts posts) {
        return repository.save(posts);
    }

    @Transactional
    public void update(Long postId, PostsUpdateRequestDto updateParam) {
        repository.update(postId, updateParam);
    }

    public PostsResponseDto findById(Long postId) {
        return repository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
    }

    public Page<PostsResponseDto> findAll(FindPostsCond cond) {
        return repository.findAll(cond);
    }

    @Transactional
    public void delete(Long postId) {
        repository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
        repository.delete(postId);
    }

    @Transactional
    public void saveLikedPost(Long memberId, Long postId) {
        repository.saveLikedPost(memberId, postId);
    }

    @Transactional
    public void deleteLikedPost(Long memberId, Long postId) {
        repository.deleteLikedPost(memberId, postId);
    }

    public Long getLiked(Long postId) {
        return repository.getLiked(postId);
    }

    public Long isLiked(Long memberId, Long postId) {
        return repository.isLiked(memberId, postId);
    }

    public Page<PostsResponseDto> findAllLikedPost(Long memberId) {
        return repository.findAllLikedPost(memberId);
    }

    @Transactional
    public void tagging(Long postId, String tagName) {
        repository.tagging(postId, tagName);
    }

    @Transactional
    public void deleteTags(Long postId) {
        repository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
        repository.deleteTags(postId);
    }

    public List<String> findTags(Long postId) {
        return repository.findTags(postId);
    }

    public List<TagResponseDto> findTagsAndCount(Long memberId) {
        return repository.findTagsAndCount(memberId);
    }
}
