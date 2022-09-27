package com.stoph.boolog.domain.posts;

import com.github.pagehelper.Page;
import com.stoph.boolog.web.dto.PostsResponseDto;
import com.stoph.boolog.web.dto.PostsUpdateRequestDto;
import com.stoph.boolog.web.dto.TagResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostsRepositoryImpl implements PostsRepository{

    private final PostsMapper postsMapper;

    @Override
    public Long save(Posts posts) {
        postsMapper.save(posts);
        return posts.getId();
    }

    @Override
    public void update(Long id, PostsUpdateRequestDto updateParam) {
        postsMapper.update(id, updateParam);
    }

    @Override
    public Optional<PostsResponseDto> findById(Long id) {
        return postsMapper.findById(id);
    }

    @Override
    public Page<PostsResponseDto> findAll(FindPostsCond cond) {
        return postsMapper.findAll(cond);
    }

    @Override
    public void delete(Long id) {
        postsMapper.delete(id);
    }

    @Override
    public void saveLikedPost(Long memberId, Long postId) {
        postsMapper.saveLikedPost(memberId, postId);
    }

    @Override
    public void deleteLikedPost(Long memberId, Long postId) {
        postsMapper.deleteLikedPost(memberId, postId);
    }

    @Override
    public Long getLiked(Long postId) {
        return postsMapper.getLiked(postId);
    }

    @Override
    public Long isLiked(Long memberId, Long postId) {
        return postsMapper.isLiked(memberId, postId);
    }

    @Override
    public Page<PostsResponseDto> findAllLikedPost(Long memberId) {
        return postsMapper.findAllLikedPost(memberId);
    }

    @Override
    public void tagging(Long postId, String tagName) {
        postsMapper.tagging(postId, tagName);
    }

    @Override
    public void deleteTags(Long postId) {
        postsMapper.deleteTags(postId);
    }

    @Override
    public List<String> findTags(Long postId) {
        return postsMapper.findTags(postId);
    }

    @Override
    public List<TagResponseDto> findTagsAndCount(Long memberId) {
        return postsMapper.findTagsAndCount(memberId);
    }
}
