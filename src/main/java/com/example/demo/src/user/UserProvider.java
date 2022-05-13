package com.example.demo.src.user;


import com.example.demo.config.BaseException;
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
public class UserProvider {

    private final UserDao userDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserProvider(UserDao userDao, JwtService jwtService) {
        this.userDao = userDao;
        this.jwtService = jwtService;
    }


    public GetUserFeedRes retrieveUserFeed(int userIdxByJwt, int userIdx) throws BaseException{
        // userIdxByJwt : 로그인 된 계정 index, userIdx : 볼 피드의 계정 index(path variable로 받은 계정)

        Boolean isMyFeed = true;

        // validation 처리
        if(checkUserExist(userIdx)==0){
            throw new BaseException(USERS_EMPTY_USER_ID);
        }

        try{
            if(userIdxByJwt != userIdx)
                isMyFeed = false;

            GetUserInfoRes getUserInfo = userDao.selectUserInfo(userIdx);
            List<GetUserPostsRes> getUserPosts = userDao.selectUserPosts(userIdx);
            GetUserFeedRes getUsersRes = new GetUserFeedRes(isMyFeed, getUserInfo, getUserPosts);
            return getUsersRes;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkUserExist(int userIdx) throws BaseException{
        try{
            return userDao.checkUserExist(userIdx);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // email로 유저 조회 함수
    public GetUserRes getUsersByEmail(String email) throws BaseException{
        try{
            GetUserRes getUsersRes = userDao.getUsersByEmail(email);
            return getUsersRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public GetUserRes getUserByIdx(int userIdx) throws BaseException{
        try{
            GetUserRes getUserRes = userDao.getUserByIdx(userIdx);
            return getUserRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public int checkEmail(String email) throws BaseException{
        try{
            return userDao.checkEmail(email);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public DeleteUserRes deleteUserByIdx(DeleteUserReq deleteUserReq) throws BaseException {
        try {
            DeleteUserRes deleteUserRes = userDao.deleteUserByIdx(deleteUserReq);
            return deleteUserRes;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(DELETE_USER_FAIL);
        }
    }

}
