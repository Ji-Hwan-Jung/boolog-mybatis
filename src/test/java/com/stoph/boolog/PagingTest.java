package com.stoph.boolog;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.stoph.boolog.domain.posts.FindPostsCond;
import com.stoph.boolog.service.PostsService;
import com.stoph.boolog.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@SpringBootTest
public class PagingTest {

    @Autowired
    private PostsService postsService;

    @Test
    void paging() {
        PageHelper.startPage(1, 2);
        Page<PostsResponseDto> page = postsService.findAll(new FindPostsCond());
        log.info("result = {}", page.getResult());
        log.info("pages = {}", page.getResult().get(0));
    }
}
