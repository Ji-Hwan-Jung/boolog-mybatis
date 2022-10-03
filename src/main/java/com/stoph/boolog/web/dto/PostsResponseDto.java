package com.stoph.boolog.web.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

@EqualsAndHashCode
@ToString
@Getter
public class PostsResponseDto {

    private Long id;
    private String author;
    private String thumbnail;
    private String description;
    private String title;
    private String content;
    private Long liked;
    private String tags;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    @Builder
    public PostsResponseDto(Long id, String author, String thumbnail, String description, String title, String content, Long liked, String tags, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        this.id = id;
        this.author = author;
        this.thumbnail = thumbnail;
        this.description = description;
        this.title = title;
        this.content = content;
        this.liked = liked;
        this.tags = tags;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }
}
