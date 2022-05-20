package com.example.demo.src.post;


import com.example.demo.config.BaseException;
import com.example.demo.src.post.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class PostService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PostDao postDao;
    private final PostProvider postProvider;
    private final JwtService jwtService;


    @Autowired
    public PostService(PostDao postDao, PostProvider postProvider, JwtService jwtService) {
        this.postDao = postDao;
        this.postProvider = postProvider;
        this.jwtService = jwtService;

    }

    public PostPostsRes createPost(int userIdx, PostPostsReq postPostsReq) throws BaseException {
        try{
            int postIdx = postDao.createPost(userIdx, postPostsReq.getContent());
            for(int i = 0; i<postPostsReq.getPostImgUrls().size(); i++){
                postDao.insertPostImgUrls(postIdx, postPostsReq.getPostImgUrls().get(i));
            }
            return new PostPostsRes(postIdx);
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyPost(int postIdx, PatchPostReq patchPostReq) throws BaseException {
        if(postProvider.checkUserExist(patchPostReq.getUserIdx())==0){
            throw new BaseException((USERS_EMPTY_USER_ID));
        }
        if(postProvider.checkPostExist(postIdx)==0){
            throw new BaseException((POSTS_EMPTY_POST_ID));
        }
        try{
            postDao.modifyPost(postIdx, patchPostReq.getContent());
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
