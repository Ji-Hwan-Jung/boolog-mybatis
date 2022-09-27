package com.stoph.boolog.web.dto;

import com.stoph.boolog.domain.posts.Posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
public class PostsSaveRequestDto {

    private Long memberId;

    private String thumbnail;

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotEmpty(message = "내용을 입력해주세요.")
    private String content;

    private String tags;

    @Builder
    public PostsSaveRequestDto(Long memberId, String thumbnail, String title, String content, String tags) {
        this.memberId = memberId;
        this.thumbnail = thumbnail;
        this.title = title.trim();
        this.content = content.trim();
        this.tags = tags.trim();
        if (tags.equals("")) {
            this.tags = null;
        }
    }

    public Posts toEntity() {
        return Posts.builder()
                .memberId(this.memberId)
                .thumbnail(this.thumbnail)
                .title(this.title)
                .content(this.content)
                .tags(this.tags)
                .build();
    }
}
