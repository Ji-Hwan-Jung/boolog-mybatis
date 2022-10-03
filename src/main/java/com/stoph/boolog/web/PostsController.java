package com.stoph.boolog.web;

import com.github.pagehelper.Page;
import com.stoph.boolog.config.LoginMember;
import com.stoph.boolog.config.dto.SessionMember;
import com.stoph.boolog.domain.member.Member;
import com.stoph.boolog.domain.posts.FindPostsCond;
import com.stoph.boolog.domain.posts.Posts;
import com.stoph.boolog.service.MemberService;
import com.stoph.boolog.service.PostsService;
import com.stoph.boolog.web.dto.PostsResponseDto;
import com.stoph.boolog.web.dto.PostsSaveRequestDto;
import com.stoph.boolog.web.dto.PostsUpdateRequestDto;
import com.stoph.boolog.web.lib.Paging;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
@CrossOrigin(origins = "http://chiye1890.dothome.co.kr", methods = RequestMethod.GET)
@Controller
public class PostsController {

    private final PostsService postsService;
    private final MemberService memberService;

    private FindPostsCond condition = new FindPostsCond();

    //URL 리다이렉트
    @GetMapping("/posts")
    public String mainRedirect() {
        return "redirect:/";
    }

    //인기 포스트
    @GetMapping("/posts/popular")
    public String popular(@RequestParam(value = "period", required = false) String period,
                          @RequestParam(value = "page", defaultValue = "1") int page,
                          Model model) {

        //기간별 조회 드롭박스에 표시될 텍스트를 바꾸기 위해 사용
        Map<String, String> periodMap = new HashMap<>();
        periodMap.put("daily", "일간");
        periodMap.put("weekly", "주간");
        periodMap.put("monthly", "월간");
        periodMap.put("yearly", "연간");

        condition.popularCond(period);

        /*NOTE
         * 페이징 처리 로직
         */
        Paging.init(page);  //한 페이지에 몇개의 글을 보여줄지 초기화하는 부분 (현재 페이지 번호를 파라미터로 전달)
        Page<PostsResponseDto> post = postsService.findAll(condition);
        List<Integer> pageList = Paging.getPageIndices(post, page);  // 선택된 페이지에 따른 사용자에게 보여질 페이지 인덱스 목록

        model.addAttribute("pageList", pageList);

        model.addAttribute("popularList", post);

        model.addAttribute("currentPage", page);

        // 페이지 요청마다 쿼리파라미터 값을 기억하기 위해 모델로 전달
        model.addAttribute("pageNum", page);
        model.addAttribute("periodKey", period);
        model.addAttribute("periodValue", periodMap.get(period));

        return "popular";
    }

    //최신 포스트
    @GetMapping("/posts/recent")
    public String recent(@RequestParam(value = "page", defaultValue = "1") int page,
                         Model model) {

        condition.recentCond();

        Paging.init(page);
        Page<PostsResponseDto> post = postsService.findAll(condition);
        List<Integer> pageList = Paging.getPageIndices(post, page);

        model.addAttribute("pageList", pageList);
        model.addAttribute("currentPage", page);
        model.addAttribute("recentList", post);

        return "recent";
    }

    //포스트 세부내용
    @GetMapping("/posts/{id}")
    public String post(@PathVariable("id") Long postId, Model model, @LoginMember SessionMember member) {

        PostsResponseDto post = postsService.findById(postId);
        List<String> tags = postsService.findTags(postId);

        model.addAttribute("post", post);
        model.addAttribute("tags", tags);

        if (member == null) {
            model.addAttribute("hasAuthority", false);
            model.addAttribute("isLiked", 0L);  //세션이 없기 때문에 false로 설정
        } else {
            Long memberId = memberService.findByEmail(member.getEmail()).getId();
            Long isLiked = postsService.isLiked(memberId, postId);

            model.addAttribute("hasAuthority", member.getName().equals(post.getAuthor()));
            model.addAttribute("isLiked", isLiked);  //0이면 좋아요 안 한 게시글, 1이면 좋아요한 게시글
        }

        return "post";
    }

    //검색결과 리스트
    @GetMapping("/posts/search")
    public String search(@RequestParam("q") String keyword,
                         @RequestParam(value = "page", defaultValue = "1") int page,
                         Model model) {

        condition.searchCond(keyword);

        Paging.init(page);
        Page<PostsResponseDto> searchResult = postsService.findAll(condition);
        List<Integer> pageList = Paging.getPageIndices(searchResult, page);

        model.addAttribute("pageList", pageList);
        model.addAttribute("result", searchResult);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);

        return "search";
    }

    @GetMapping("/posts/write")
    public String write(Model model, @LoginMember SessionMember member) {

        Long memberId = memberService.findByEmail(member.getEmail()).getId();

        model.addAttribute("memberId", memberId);

        return "write";
    }

    @ResponseBody
    @PostMapping("/posts/write")
    public String addPost(@ModelAttribute PostsSaveRequestDto post){

        Long postId = postsService.save(post.toEntity());

        if (post.getTags() != null) {
            List<String> tags = Arrays.stream(post.getTags().split(","))
                    .map(tag -> tag.trim())
                    .collect(Collectors.toList());

            tags.forEach(tag -> postsService.tagging(postId, tag));
        }

        return postId.toString();
    }

    @GetMapping("/posts/{postId}/edit")
    public String edit(@PathVariable Long postId, Model model, @LoginMember SessionMember member){

        //세션에 있는 유저의 이름과 게시글을 작성한 유저의 이름을 비교하기 위해 조회
        PostsResponseDto post = postsService.findById(postId);

        if (member != null && (member.getName().equals(post.getAuthor()))) {
            model.addAttribute("post", post);
            return "edit";
        } else {
            return "";
        }
    }

    @ResponseBody
    @PutMapping("/posts/{postId}/edit")
    public String edit(@ModelAttribute PostsUpdateRequestDto post,
                           @PathVariable Long postId,
                           @LoginMember SessionMember member) {

        //세션에 있는 유저의 이름과 게시글을 작성한 유저의 이름을 비교하기 위해 조회
        String author = postsService.findById(postId).getAuthor();

        if (member != null && (member.getName().equals(author))) {
            postsService.update(postId, post);

            //기존 태그들 삭제 후 새로운 문자열을 토대로 태그 삽입
            postsService.deleteTags(postId);

            //태그 문자열을 받아서 쉼표를 구분자로하여 리스트로 변환
            if (post.getTags() != null) {
                List<String> tags = Arrays.stream(post.getTags().split(","))
                        .map(tag -> tag.trim())
                        .collect(Collectors.toList());

                //리스트의 각 태그들 DB에 삽입
                tags.forEach(tag -> postsService.tagging(postId, tag));
            }
        }

        return postId.toString();
    }

    @ResponseBody
    @DeleteMapping("/posts/{postId}/delete")
    public String delete(@PathVariable Long postId, @LoginMember SessionMember member) {

        String author = postsService.findById(postId).getAuthor();

        if (member != null && (member.getName().equals(author))) {
            postsService.delete(postId);
        }

        return "success";
    }

    @GetMapping("/posts/liked")
    public String liked(Model model,
                        @RequestParam(value = "page", defaultValue = "1") int page,
                        @LoginMember SessionMember member) {

        Long memberId = memberService.findByEmail(member.getEmail()).getId();

        Paging.init(page);
        Page<PostsResponseDto> allLikedPost = postsService.findAllLikedPost(memberId);
        List<Integer> pageList = Paging.getPageIndices(allLikedPost, page);

        model.addAttribute("pageList", pageList);
        model.addAttribute("currentPage", page);
        model.addAttribute("likedList", allLikedPost);

        return "liked";
    }

    @ResponseBody
    @PostMapping("/posts/{postId}/thumb-up")
    public String thumbUp(@PathVariable Long postId,
                          @LoginMember SessionMember member) {

        if (member != null) {
            Long memberId = memberService.findByEmail(member.getEmail()).getId();
            postsService.saveLikedPost(memberId, postId);
            String likedCnt = postsService.getLiked(postId).toString();
//            log.info("처리 상태 = {}, 반환 값 = {}", "정상", likedCnt);

            return likedCnt;

        } else {
//            log.info("처리 상태 = {}", "비정상");
            return "error";  //클라이언트에서 error 처리를 하도록 (차후 바꿀 예정)
        }
    }

    @ResponseBody
    @DeleteMapping("/posts/{postId}/thumb-up-cancel")
    public String thumbUpCancel(@PathVariable Long postId,
                                @LoginMember SessionMember member) {

        if (member != null) {
            Long memberId = memberService.findByEmail(member.getEmail()).getId();
            postsService.deleteLikedPost(memberId, postId);
            String likedCnt = postsService.getLiked(postId).toString();
//            log.info("처리 상태 = {}, 반환 값 = {}", "정상", likedCnt);

            return likedCnt;
        } else {
//            log.info("처리 상태 = {}", "비정상");
            return "error";  //클라이언트에서 error 처리를 하도록 (차후 바꿀 예정)
        }
    }

    @GetMapping("/tags/{tag}")
    public String tags(@PathVariable("tag") String tag,
                       @RequestParam(value = "page", defaultValue = "1") int page,
                       Model model) {

        condition.taggedCond(tag);

        Paging.init(page);
        Page<PostsResponseDto> postList = postsService.findAll(condition);
        List<Integer> pageList = Paging.getPageIndices(postList, page);

        model.addAttribute("tag", tag);
        model.addAttribute("currentPage", page);
        model.addAttribute("postList", postList);
        model.addAttribute("pageList", pageList);

        return "tags";
    }
}
