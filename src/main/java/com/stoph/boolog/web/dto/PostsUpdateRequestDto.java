package com.stoph.boolog.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@ToString
public class PostsUpdateRequestDto {

    private String description;
    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    private String tags;

    @Builder
    public PostsUpdateRequestDto(String description, String title, String content, String tags) {
        this.description = description;
        this.title = title;
        this.content = content;
        this.tags = tags.trim().equals("") ? null : tags.trim();
    }
}
