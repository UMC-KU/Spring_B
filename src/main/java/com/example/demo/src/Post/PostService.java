package com.example.demo.src.post;


import com.example.demo.config.BaseException;
import com.example.demo.src.post.model.GetPostsRes;
import com.example.demo.src.post.model.PatchPostsReq;
import com.example.demo.src.post.model.PostPostsReq;
import com.example.demo.src.post.model.PostPostsRes;
import com.example.demo.src.user.UserDao;
import com.example.demo.src.user.UserProvider;
import com.example.demo.src.user.model.PatchUserReq;
import com.example.demo.src.user.model.PostUserReq;
import com.example.demo.src.user.model.PostUserRes;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public PostPostsRes createPost(int userIdx, PostPostsReq postPostsReq) throws BaseException{
        if(postProvider.checkUserExist(userIdx)==0)
            throw new BaseException(USERS_EMPTY_USER_ID);

        try{
            int postIdx = postDao.insertPost(userIdx, postPostsReq.getContent());
            // 게시물의 이미지는 리스트로 넣어야 하기 때문에 반복문, 다른 함수로 처리
            for(int i=0; i<postPostsReq.getPostImgUrls().size(); i++)
                postDao.insertPostImgs(postIdx, postPostsReq.getPostImgUrls().get(i));
            return new PostPostsRes(postIdx);
        }
        catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyPost(int userIdx, int postIdx, PatchPostsReq patchPostsReq) throws BaseException{
        if(postProvider.checkUserExist(userIdx)==0)
            throw new BaseException(USERS_EMPTY_USER_ID);

        if(postProvider.checkPostExist(postIdx)==0)
            throw new BaseException(POSTS_EMPTY_POST_ID);

        try{
            // update 문 실행 잘 되면 1 반환, 아니면 0 반환 -> 에러 코드 전달
            int result = postDao.updatePost(postIdx, patchPostsReq.getContent());

            // result가 0이면, DB 접근 오류 or 데이터 정상적으로 입력 X
            if(result == 0)
                throw new BaseException(MODIFY_FAIL_POST);
        }
        catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void deletePost(int postIdx) throws BaseException{
        if(postProvider.checkPostExist(postIdx)==0)
            throw new BaseException(POSTS_EMPTY_POST_ID);

        try{
            // update 문 실행 잘 되면 1 반환, 아니면 0 반환 -> 에러 코드 전달
            int result = postDao.deletePost(postIdx);

            // result가 0이면, DB 접근 오류 or 데이터 정상적으로 입력 X
            if(result == 0)
                throw new BaseException(DELETE_FAIL_POST);
        }
        catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
