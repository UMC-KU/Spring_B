package com.example.demo.src.post;


import com.example.demo.config.BaseException;
import com.example.demo.src.post.model.*;
import com.example.demo.src.user.model.PatchUserReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service
public class PostProvider {

    private final PostDao postDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public PostProvider(PostDao postDao, JwtService jwtService) {
        this.postDao = postDao;
        this.jwtService = jwtService;
    }


    public List<GetPostsRes> retrievePosts(int userIdx) throws BaseException{

        if(checkUserExist(userIdx)==0){
            throw new BaseException((USERS_EMPTY_USER_ID));
        }
        try{
            return postDao.selectPosts(userIdx);
        }
        catch (Exception exception) {
            throw new BaseException(GET_PROVIDER_FAIL_ERROR);
        }
    }

    public int checkUserExist(int userIdx) throws BaseException{
        try{
            return postDao.checkUserExist(userIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkPostExist(int postIdx) throws BaseException{
        try{
            return postDao.checkPostExist(postIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
