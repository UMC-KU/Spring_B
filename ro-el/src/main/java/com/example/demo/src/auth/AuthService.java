package com.example.demo.src.auth;


import com.example.demo.utils.SHA256;
import com.example.demo.config.BaseException;
import com.example.demo.src.auth.model.PostLoginReq;
import com.example.demo.src.auth.model.PostLoginRes;
import com.example.demo.src.auth.model.User;
import com.example.demo.src.post.model.PatchPostsReq;
import com.example.demo.src.post.model.PostPostsReq;
import com.example.demo.src.post.model.PostPostsRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class AuthService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AuthDao authDao;
    private final AuthProvider authProvider;
    private final JwtService jwtService;


    @Autowired
    public AuthService(AuthDao authDao, AuthProvider authProvider, JwtService jwtService) {
        this.authDao = authDao;
        this.authProvider = authProvider;
        this.jwtService = jwtService;
    }

    public PostLoginRes logIn(PostLoginReq postLoginReq) throws BaseException{
        // login 요청 객체를 userDao에 전달해서 해당하는 user 정보 받아옴
        // 해당 유저의 pwd를 받아와서, 새로 받은 pwd 와 비교
        User user = authDao.getPwd(postLoginReq);
        String encryptPwd; // 새로 받은 pwd 암호화 -> 암호화 알고리즘은 utils/SHA256.java

        try{
            encryptPwd = new SHA256().encrypt(postLoginReq.getPassword());
        }
        catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }

        if(user.getPassword().equals(encryptPwd)){
            int userIdx = user.getUserIdx();
            String jwt = jwtService.createJwt(userIdx);

            return new PostLoginRes(userIdx, jwt);
        }
        else
            throw new BaseException(FAILED_TO_LOGIN);
    }
}
