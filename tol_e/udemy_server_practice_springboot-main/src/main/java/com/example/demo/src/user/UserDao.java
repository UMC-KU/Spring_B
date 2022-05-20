package com.example.demo.src.user;


import com.example.demo.config.BaseException;
import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public GetUserInfoRes getUserInfo(int userIdx) throws BaseException {
            String getUserInfoQuery = "SELECT u.userIdx as userIdx, \n" +
                    "       u.nickName as nickName, \n" +
                    "       u.name as name, \n" +
                    "       u.profileImgUrl as profileImgUrl, \n" +
                    "       u.website as website, \n" +
                    "       u.introduction as introduction, \n" +
                    "       if(followedCount is null, 0, followedCount) as followedCount, \n" +
                    "       if(followingCount is null, 0, followingCount) as followingCount, \n" +
                    "       if(postCount is null, 0, postCount) as postCount \n" +
                    "FROM User as u \n" +
                    "       left join (select followedIdx, COUNT(followIdx) as followedCount FROM Follow where status = 'ACTIVE' group by followedIdx) f1 on f1.followedIdx = u.userIdx \n" +
                    "       left join (select followingIdx, COUNT(followIdx) as followingCount FROM Follow where status = 'ACTIVE' group by followingIdx) f2 on f2.followingIdx = u.userIdx \n" +
                    "       left join (select userIdx, COUNT(postIdx) as postCount FROM Post where status = 'ACTIVE' group by userIdx) p on p.userIdx = u.userIdx \n" +
                    "where u.userIdx = ? and u.status = 'ACTIVE'";
            int getUserInfoParams = userIdx;
            return this.jdbcTemplate.queryForObject(getUserInfoQuery,
                    (rs, rowNum) -> new GetUserInfoRes(
                            rs.getString("nickName"),
                            rs.getString("name"),
                            rs.getString("profileImgUrl"),
                            rs.getString("website"),
                            rs.getString("introduction"),
                            rs.getInt("followedCount"),
                            rs.getInt("followingCount"),
                            rs.getInt("postCount")),
                    getUserInfoParams);

    }


    public List<GetUserPostRes> getUserPost(int userIdx) throws BaseException {
        try{
        String getUserPostQuery = "" +
                "select p.postIdx as postIdx, \n" +
                "       pi.imgUrl as postImgUrl \n" +
                "from User as u \n" +
                "    join Post as p on p.userIdx = u.userIdx and p.status = 'ACTIVE' \n" +
                "    join PostImgUrl as pi on pi.postIdx = p.postIdx and pi.status = 'ACTIVE' \n" +
                "where u.userIdx = ? and u.status = 'ACTIVE' \n" +
                "group by p.postIdx";
        int getUserPostParams = userIdx;
        return this.jdbcTemplate.query(getUserPostQuery,
                (rs,rowNum) -> new GetUserPostRes(
                        rs.getInt("postIdx"),
                        rs.getString("postImgUrl")),
                getUserPostParams);
        }
        catch (Exception exception) {
            throw new BaseException(GET_DAO_POST_FAIL_ERROR);
        }
    }

    public GetUserRes getUsersByEmail(String email){
        String getUsersByEmailQuery = "select userIdx,name,nickName,email from User where email=?";
        String getUsersByEmailParams = email;
        return this.jdbcTemplate.queryForObject(getUsersByEmailQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("name"),
                        rs.getString("nickName"),
                        rs.getString("email")),
                getUsersByEmailParams);
    }


    public GetUserRes getUsersByIdx(int userIdx){
        String getUsersByIdxQuery = "select userIdx,name,nickName,email from User where userIdx=?";
        int getUsersByIdxParams = userIdx;
        return this.jdbcTemplate.queryForObject(getUsersByIdxQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("name"),
                        rs.getString("nickName"),
                        rs.getString("email")),
                getUsersByIdxParams);
    }

    public int createUser(PostUserReq postUserReq){
        String createUserQuery = "insert into User (name, nickName, phone, email, password) VALUES (?,?,?,?,?)";
        Object[] createUserParams = new Object[]{postUserReq.getName(), postUserReq.getNickName(),postUserReq.getPhone(), postUserReq.getEmail(), postUserReq.getPassword()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);
        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }

    public int checkEmail(String email){
        String checkEmailQuery = "select exists(select email from User where email = ?)";
        String checkEmailParams = email;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);

    }

    public int checkUserExist(int userIdx){
        String checkUserExistQuery = "select exists(select userIdx from User where userIdx = ?)";
        int checkUserExistParams = userIdx;
        return this.jdbcTemplate.queryForObject(checkUserExistQuery,
                int.class,
                checkUserExistParams);
    }
    public int modifyUserName(PatchUserReq patchUserReq){
        String modifyUserNameQuery = "update User set nickName = ? where userIdx = ? ";
        Object[] modifyUserNameParams = new Object[]{patchUserReq.getNickName(), patchUserReq.getUserIdx()};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }

    public int checkStatus(int userIdx){
        String checkStatusQuery = "select status from User where userIdx = ?)";
        String checkStatusParams = "ACTIVE";
        return this.jdbcTemplate.queryForObject(checkStatusQuery,
                int.class,
                checkStatusParams);
    }

    public DeleteUserRes deleteUserByIdx(int userIdx){
        String deleteUserByIdxQuery = "select name,nickName,phone,email,password from User where userIdx=?";
        int deleteUserByIdxParams = userIdx;
        DeleteUserRes deleteUserRes = this.jdbcTemplate.queryForObject(deleteUserByIdxQuery,
                (rs, rowNum) -> new DeleteUserRes(
                        rs.getString("name"),
                        rs.getString("nickName"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("password")),
                deleteUserByIdxParams);
        String deleteQuery = "delete from User where userIdx=?";
        this.jdbcTemplate.update(deleteQuery,deleteUserByIdxParams);
        return deleteUserRes;
    }
}
