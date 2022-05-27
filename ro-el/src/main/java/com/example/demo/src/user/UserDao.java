package com.example.demo.src.user;


import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetUserRes> getUsers(){
        String getUsersQuery = "select userIdx,name,nickName,email from User";
        return this.jdbcTemplate.query(getUsersQuery,
                (rs,rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("name"),
                        rs.getString("nickName"),
                        rs.getString("email")
                ));
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


    public GetUserRes getUserByIdx(int userIdx){
        String getUserByIdxQuery = "select userIdx,name,nickName,email from User where userIdx=?";
        int getUserByIdxParams = userIdx;
        return this.jdbcTemplate.queryForObject(getUserByIdxQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("name"),
                        rs.getString("nickName"),
                        rs.getString("email")),
                getUserByIdxParams);
    }

    public int createUser(PostUserReq postUserReq){
        String createUserQuery = "insert into User (name, nickName, phone, email, password) VALUES (?,?,?,?,?)";
        Object[] createUserParams = new Object[]{postUserReq.getName(), postUserReq.getNickName(),postUserReq.getPhone(), postUserReq.getEmail(), postUserReq.getPassword()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery, int.class);
    }

    public int checkEmail(String email){
        String checkEmailQuery = "select exists(select email from User where email = ?)";
        String checkEmailParams = email;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);

    }

    public int modifyUserName(PatchUserReq patchUserReq){
        String modifyUserNameQuery = "update User set nickName = ? where userIdx = ? ";
        Object[] modifyUserNameParams = new Object[]{patchUserReq.getNickName(), patchUserReq.getUserIdx()};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }



    // User 삭제
    public DeleteUserRes deleteUserByIdx(DeleteUserReq deleteUserReq) {
        String getUserByIdxQueryForShow = "select name, nickName, gender, email, password from User where userIdx = ?";
        int getUserByIdxQueryForShowParams = deleteUserReq.getUserIdx();

        DeleteUserRes deleteUserRes = this.jdbcTemplate.queryForObject(getUserByIdxQueryForShow,
                (rs, rowNum) -> new DeleteUserRes(
                        rs.getString("name"),
                        rs.getString("nickName"),
                        rs.getString("gender"),
                        rs.getString("email"),
                        rs.getString("password")),
                getUserByIdxQueryForShowParams);

        String deleteUserByIdxQuery = "delete from User where userIdx = ?";
        this.jdbcTemplate.update(deleteUserByIdxQuery,deleteUserReq.getUserIdx());

        return deleteUserRes;
    }



    // 유저 피드 조회 중
    // 유저 정보
    public GetUserInfoRes selectUserInfo(int userIdx){
        String selectUsersInfoQuery = "select u.nickName as nickName,\n" +
                "       u.name as name,\n" +
                "       u.profileImgUrl as profileImgUrl,\n" +
                "       u.website as website,\n" +
                "       u.introduction as introduction,\n" +
                "       if(followerCount is null, 0, followerCount) as followerCount,\n" +
                "       if(followingCount is null, 0, followingCount) as followingCount,\n" +
                "       p.postCount as postCount\n" +
                "from User as u\n" +
                "    left join (select userIdx, count(postIdx) as postCount from Post where status = 'ACTIVE' group by userIdx) p on p.userIdx = u.userIdx\n" +
                "    left join (select followeeIdx, count(followIdx) as followerCount from Follow where status = 'ACTIVE' group by followeeIdx) fc on fc.followeeIdx = u.userIdx\n" +
                "    left join (select followerIdx, count(followIdx) as followingCount from Follow where status = 'ACTIVE' group by followerIdx) f on f.followerIdx = u.userIdx\n" +
                "where u.userIdx = ? and u.status = 'ACTIVE'";

        int selectUserInfoParam = userIdx;

        // List 반환할 때는 그냥 query를 사용하고,
        // 아닐 때는 queryForObject를 사용한다.
        return this.jdbcTemplate.queryForObject(selectUsersInfoQuery,
                (rs, rowNum) -> new GetUserInfoRes(
                        rs.getString("nickName"),
                        rs.getString("name"),
                        rs.getString("profileImgUrl"),
                        rs.getString("website"),
                        rs.getString("introduction"),
                        rs.getInt("followerCount"),
                        rs.getInt("followingCount"),
                        rs.getInt("postCount")
                ), selectUserInfoParam);
    }

    // 유저 피드 조회 중
    // 게시글 리스트
    public List<GetUserPostsRes> selectUserPosts(int userIdx){
        String selectUsersPostsQuery = "select p.postIdx as postIdx,\n" +
                "       pi.imgUrl as postImgUrl,\n" +
                "from Post as p\n" +
                "         join PostImgUrl as pi on pi.postIdx = p.userIdx and pi.status = 'ACTIVE'\n" +
                "         join User as u on p.userIdx = u.userIdx\n" +
                "where p.status = 'ACTIVE' and u.userIdx = ?\n" +
                "group by p.postIdx\n" +
                "having min(pi.postImgUrlIdx)\n" +
                "order by p.postIdx";

        int selectUserPostsParam = userIdx;

        return this.jdbcTemplate.query(selectUsersPostsQuery,
                (rs, rowNum) -> new GetUserPostsRes(
                        rs.getInt("postIdx"),
                        rs.getString("postImgUrl")
                ), selectUserPostsParam);
    }

    public int checkUserExist(int userIdx){
        String checkUserExistQuery = "select exists(select userIdx from User where userIdx = ?)";
        int checkUserExistParams = userIdx;
        return this.jdbcTemplate.queryForObject(checkUserExistQuery,
                int.class,
                checkUserExistParams);
    }
}



