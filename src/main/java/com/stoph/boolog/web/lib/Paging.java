package com.stoph.boolog.web.lib;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.stoph.boolog.web.dto.PostsResponseDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public abstract class Paging {

    private static final int POSTS_NUMBER = 8;  // 한 페이지에 보여줄 게시글 개수
    private static final int PAGE_SIZE = 5;  // 사용자에게 보여질 페이지 인덱스 개수

    /**
     * PageHelper 초기화 메서드
     * @param page 현재 페이지 번호
     */
    public static void init(int page) {
        PageHelper.clearPage();
        PageHelper.startPage(page, POSTS_NUMBER);
    }

    /**
     * 게시글 목록과 현재 페이지 번호를 받아서 사용자에게 보여질 페이지 번호 리스트를 반환
     * @param post 찾아온 게시글
     * @param page 현재 페이지 번호
     * @return List (보여질 페이지 리스트)
     */
    public static List<Integer> getPageIndices(Page<PostsResponseDto> post, int page) {

        //현재 속한 페이지에서 보여줄 끝 페이지 숫자와 시작 페이지 숫자 (5개씩 보여주도록)
        int startPage = (PAGE_SIZE * ((page / PAGE_SIZE) + 1)) - (PAGE_SIZE - 1);
        int endPage = PAGE_SIZE * ((page / PAGE_SIZE) + 1);

        //실제 페이지 개수보다 위의 계산된 페이지 개수가 더 많을 경우 실제 페이지 개수로 바꿈
        if (endPage > post.getPages()) {
            endPage = post.getPages();
        }

        //보여질 페이지 인덱스 리스트
        List<Integer> pageList = new ArrayList<>();
        for(int i = startPage; i <= endPage; i++) {
            pageList.add(i);
        }

        return pageList;
    }
}
