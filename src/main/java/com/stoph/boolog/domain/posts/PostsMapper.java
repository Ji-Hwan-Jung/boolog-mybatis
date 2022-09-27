package com.stoph.boolog.domain.posts;

import com.github.pagehelper.Page;
import com.stoph.boolog.web.dto.PostsResponseDto;
import com.stoph.boolog.web.dto.PostsUpdateRequestDto;
import com.stoph.boolog.web.dto.TagResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Optional;

@Mapper
public interface PostsMapper {

    void save(Posts posts);

    void update(@Param("id") Long id, @Param("updateParam") PostsUpdateRequestDto dto);

    Optional<PostsResponseDto> findById(Long id);

    Page<PostsResponseDto> findAll(FindPostsCond cond);

    void delete(Long id);

    void saveLikedPost(@Param("memberId") Long memberId, @Param("postId") Long postId);

    void deleteLikedPost(@Param("memberId") Long memberId, @Param("postId") Long postId);

    Long getLiked(@Param("id") Long postId);

    Long isLiked(@Param("memberId") Long memberId, @Param("postId") Long postId);

    Page<PostsResponseDto> findAllLikedPost(@Param("id") Long memberId);

    void tagging(@Param("postId") Long postId, @Param("name") String tagName);

    void deleteTags(@Param("id") Long postId);

    List<String> findTags(@Param("id") Long postId);

    List<TagResponseDto> findTagsAndCount(@Param("id") Long memberId);
}
