package com.example.demo.src.Post;


import com.example.demo.config.BaseException;
import com.example.demo.src.Post.model.GetPostsRes;
import com.example.demo.src.user.UserDao;
import com.example.demo.src.user.model.*;
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
            throw new BaseException(USERS_EMPTY_USER_ID);
        }

        try{
            List<GetPostsRes> getPosts = postDao.selectPosts(userIdx);
            return getPosts;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkUserExist(int userIdx) throws BaseException{
        try{
            return postDao.checkUserExist(userIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
