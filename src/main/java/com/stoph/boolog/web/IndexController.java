package com.stoph.boolog.web;

import com.stoph.boolog.config.LoginMember;
import com.stoph.boolog.domain.posts.FindPostsCond;
import com.stoph.boolog.service.PostsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Slf4j
@CrossOrigin(origins = "http://chiye1890.dothome.co.kr", methods = RequestMethod.GET)
@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;

    private FindPostsCond condition = new FindPostsCond();

    //메인화면
    @GetMapping("/")
    public String main(Model model) {

        condition.mainPopularCond();
        model.addAttribute("mainPopular", postsService.findAll(condition));

        condition.mainRecentCond();
        model.addAttribute("mainRecent", postsService.findAll(condition));
        return "main";
    }

    @GetMapping("/signin")
    public String signin() {
        return "sign_in";
    }
}
