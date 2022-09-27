package com.stoph.boolog.web;

import com.stoph.boolog.service.MemberService;
import com.stoph.boolog.service.PostsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class PostsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostsService postsService;

    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("메인 페이지 리다이렉션")
    void mainRedirect() throws Exception {
        mockMvc.perform(get("/posts"))
                .andExpect(status().is3xxRedirection())
                .andDo(print());
    }

    @Test
    @DisplayName("인기글 페이지 요청")
    void popular() throws Exception {



        mockMvc.perform(get("/posts/popular")
                .param("period", "daily")
                .param("page", "2"))
                .andExpect(status().isOk())
                .andExpect(view().name("popular"))
                .andExpect(model().attribute("pageNum",2))
                .andExpect(model().attribute("periodKey", "daily"))
                .andDo(print());
    }

    @Test
    @DisplayName("최신글 페이지 요청")
    void recent() throws Exception {
        mockMvc.perform(get("/posts/recent")
                .param("page", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("recent"))
                .andExpect(model().attribute("currentPage", 1))
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 세부내용 요청")
    void post() {

    }

    @Test
    void search() {
    }

    @Test
    void write() {
    }

    @Test
    void addPost() {
    }

    @Test
    void edit() {
    }

    @Test
    void editPost() {
    }

    @Test
    void delete() {
    }
}