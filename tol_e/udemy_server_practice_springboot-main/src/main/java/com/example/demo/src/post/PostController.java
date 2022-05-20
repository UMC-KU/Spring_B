package com.example.demo.src.post;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.post.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/posts")
public class PostController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final PostProvider postProvider;
    @Autowired
    private final PostService postService;
    @Autowired
    private final JwtService jwtService;




    public PostController(PostProvider postProvider, PostService postService, JwtService jwtService){
        this.postProvider = postProvider;
        this.postService = postService;
        this.jwtService = jwtService;
    }




    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetPostsRes>> getPosts(@RequestParam int userIdx) {
        try{
            List<GetPostsRes> getPostsRes = postProvider.retrievePosts(userIdx);
            return new BaseResponse<>(getPostsRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PostMapping("") // (POST) 127.0.0.1:9000/users
    public BaseResponse<PostPostsRes> createPost(@RequestBody PostPostsReq postPostsReq) {
        try{
            if(postPostsReq.getContent().length()>450)
            {
                return new BaseResponse<>(POST_POSTS_INVALID_CONTENT);
            }
            if(postPostsReq.getPostImgUrls().size()<1)
            {
                return new BaseResponse<>(POST_POSTS_EMPTY_IMGURLS);
            }
            PostPostsRes postPostsRes = postService.createPost(postPostsReq.getUserIdx(), postPostsReq);
            return new BaseResponse<>(postPostsRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PatchMapping("/{postIdx}") // (POST) 127.0.0.1:9000/users
    public BaseResponse<String> modifyPost(@PathVariable("postIdx") int postIdx, @RequestBody PatchPostReq patchPostReq) {
        try{
            if(patchPostReq.getContent().length()>450) {
                return new BaseResponse<>(POST_POSTS_INVALID_CONTENT);
            }
            postService.modifyPost(postIdx, patchPostReq);
            String res = "게시물 수정을 완료했습니다.";
            return new BaseResponse<>(res);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
