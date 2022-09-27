package com.stoph.boolog.web;

import com.github.pagehelper.Page;
import com.stoph.boolog.config.LoginMember;
import com.stoph.boolog.config.dto.SessionMember;
import com.stoph.boolog.domain.posts.FindPostsCond;
import com.stoph.boolog.service.MemberService;
import com.stoph.boolog.service.PostsService;
import com.stoph.boolog.web.dto.MemberResponseDto;
import com.stoph.boolog.web.dto.MemberUpdateRequestDto;
import com.stoph.boolog.web.dto.PostsResponseDto;
import com.stoph.boolog.web.dto.TagResponseDto;
import com.stoph.boolog.web.lib.Paging;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@CrossOrigin(origins = "http://chiye1890.dothome.co.kr", methods = RequestMethod.GET)
@RequiredArgsConstructor
@Controller
public class MemberController {

    private final PostsService postsService;
    private final MemberService memberService;
    private final HttpSession httpSession;

    private FindPostsCond condition = new FindPostsCond();

    @GetMapping("/@{name}")
    public String profile(Model model,
                          @PathVariable("name") String name,
                          @RequestParam(value = "page", defaultValue = "1") int page,
                          @RequestParam(value = "tag", required = false) String tag) {

        condition.profileCond(name);
        int totalNum = postsService.findAll(condition).getResult().size();

        if (tag != null) {
            condition.userTaggedCond(tag, name);
        }

        MemberResponseDto member = memberService.findByName(name).toResponseDto();
        List<TagResponseDto> tagsAndCount = postsService.findTagsAndCount(memberService.findByName(name).getId());

        Paging.init(page);
        Page<PostsResponseDto> postList = postsService.findAll(condition);
        List<Integer> pageList = Paging.getPageIndices(postList, page);

        model.addAttribute("totalNum", totalNum);
        model.addAttribute("tagParam", tag);
        model.addAttribute("member", member);
        model.addAttribute("currentPage", page);
        model.addAttribute("postList", postList);
        model.addAttribute("pageList", pageList);
        model.addAttribute("tagsAndCount", tagsAndCount);

        return "profile";
    }

    @GetMapping("/setting")
    public String setting(Model model) {
        return "setting";
    }

    @ResponseBody
    @PatchMapping("/setting/update")
    public String profileUpdate(@RequestBody MemberUpdateRequestDto updateParam, @SessionAttribute(name = "member") SessionMember sessionMember) {

        Long memberId = memberService.findByEmail(sessionMember.getEmail()).getId();

        memberService.update(memberId, updateParam);

        sessionMember.updateSession(memberService.findById(memberId));

        return "success";
    }

    @ResponseBody
    @DeleteMapping("/setting/withdrawal")
    public String withdrawal(@LoginMember SessionMember sessionMember) {

        Long memberId = memberService.findByEmail(sessionMember.getEmail()).getId();

        memberService.delete(memberId);

        httpSession.invalidate();

        return "ok";
    }
}
