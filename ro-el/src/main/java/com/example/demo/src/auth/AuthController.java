package com.example.demo.src.auth;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.auth.model.PostLoginReq;
import com.example.demo.src.auth.model.PostLoginRes;
import com.example.demo.src.post.model.GetPostsRes;
import com.example.demo.src.post.model.PatchPostsReq;
import com.example.demo.src.post.model.PostPostsReq;
import com.example.demo.src.post.model.PostPostsRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

@RestController
@RequestMapping("/auth") // controller 에 있는 모든 api 의 uri 앞에 기본적으로 들어감
public class AuthController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final AuthProvider authProvider;
    @Autowired
    private final AuthService authService;
    @Autowired
    private final JwtService jwtService;


    public AuthController(AuthProvider authProvider, AuthService authService, JwtService jwtService){
        this.authProvider = authProvider;
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @ResponseBody
    @PostMapping("/login")
    public BaseResponse<PostLoginRes> logIn(@RequestBody PostLoginReq postLoginReq) {
        try{
            // validation
            // email - 입력하지 않았을 때, 형식 검증
            // 1. email 입력하지 않았을 때
            if(postLoginReq.getEmail() == null)
                return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
            // 2. email 형식에 맞지 않을 때 - 정규식 검증
            if(!isRegexEmail(postLoginReq.getEmail()))
                return new BaseResponse<>(POST_USERS_INVALID_EMAIL);

            // pwd - 입력하지 않았을 때 검증 (+ 형식 검증 - 사이트마다 다름)
            if(postLoginReq.getPassword() == null)
                return new BaseResponse<>(POST_USERS_EMPTY_PASSWORD);

            PostLoginRes postLoginRes = authService.logIn(postLoginReq);

            return new BaseResponse<>(postLoginRes);
        } catch(BaseException exception){
            exception.printStackTrace();
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
