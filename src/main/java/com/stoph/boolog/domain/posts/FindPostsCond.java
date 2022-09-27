package com.stoph.boolog.domain.posts;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class FindPostsCond {

    private String period;  //daily, weekly, monthly, yearly
    private String keyword; //검색 키워드
    private String section; //인기글, 최신글 등등 [popular, recent, search, main*]
    private String author; //특정 작성자
    private String tag; //특정 태그

//    private static FindPostsCond cond = new FindPostsCond();
//
//    public static FindPostsCond getInstance() {
//        return cond;
//    }

    public void searchCond(String keyword) {
        initCond();
        this.keyword = keyword.trim();
        this.section = "search";
    }

    public void popularCond(String period) {
        initCond();
        if (period != null) {
            this.period = period.trim();
        }
        this.section = "popular";
    }

    public void recentCond() {
        initCond();
        this.section = "recent";
    }

    public void profileCond(String author) {
        initCond();
        this.author = author.trim();
    }

    public void taggedCond(String tag) {
        initCond();
        this.tag = tag.trim();
    }

    public void userTaggedCond(String tag, String author) {
        initCond();
        this.tag = tag.trim();
        this.author = author.trim();
    }

    public void mainPopularCond() {
        initCond();
        this.section = "mainPopular";
    }

    public void mainRecentCond() {
        initCond();
        this.section = "mainRecent";
    }

    private void initCond() {
        this.period = null;
        this.keyword = null;
        this.section = null;
        this.author = null;
        this.tag = null;
    }
}
