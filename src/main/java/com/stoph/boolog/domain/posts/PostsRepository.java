package com.stoph.boolog.domain.posts;

import com.github.pagehelper.Page;
import com.stoph.boolog.web.dto.PostsResponseDto;
import com.stoph.boolog.web.dto.PostsUpdateRequestDto;
import com.stoph.boolog.web.dto.TagResponseDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

public interface PostsRepository {

    Long save(Posts posts);

    void update(Long id, PostsUpdateRequestDto dto);

    Optional<PostsResponseDto> findById(Long id);

    Page<PostsResponseDto> findAll(FindPostsCond cond);

    void delete(Long id);

    void saveLikedPost(Long memberId, Long postId);

    void deleteLikedPost(Long memberId, Long postId);

    Long getLiked(Long postId);

    Long isLiked(Long memberId, Long postId);

    Page<PostsResponseDto> findAllLikedPost(Long memberId);

    void tagging(Long postId, String tagName);

    void deleteTags(Long postId);

    List<String> findTags(Long postId);

    List<TagResponseDto> findTagsAndCount(Long memberId);
}
