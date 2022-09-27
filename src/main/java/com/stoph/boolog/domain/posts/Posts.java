package com.stoph.boolog.domain.posts;

import com.stoph.boolog.web.dto.PostsResponseDto;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class Posts {

    private Long id;
    private Long memberId;
    private String thumbnail;
    private String title;
    private String content;
    private Integer liked;
    private String tags;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    @Builder
    public Posts(Long memberId, String thumbnail, String title, String content, String tags) {
        this.memberId = memberId;
        this.thumbnail = thumbnail;
        this.title = title;
        this.content = content;
        this.tags = tags;
    }
}
