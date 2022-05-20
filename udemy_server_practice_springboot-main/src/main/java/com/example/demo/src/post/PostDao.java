package com.example.demo.src.post;


import com.example.demo.src.post.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class PostDao {

    private JdbcTemplate jdbcTemplate;
    private List<GetPostImgRes> getPostImgRes;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }



    public int checkUserExist(int userIdx) {
        String checkUserExistQuery = "select exists(select userIdx from user where userIdx = ?)";
        int checkUserExistParams = userIdx;
        return this.jdbcTemplate.queryForObject(checkUserExistQuery,
                int.class,
                checkUserExistParams);
    }
    public int checkPostExist(int postIdx) {
        String checkPostExistQuery = "select exists(select postIdx from post where postIdx = ?)";
        int checkPostExistParams = postIdx;
        return this.jdbcTemplate.queryForObject(checkPostExistQuery,
                int.class,
                checkPostExistParams);
    }


    public List<GetPostsRes> selectPosts(int userIdx) {
        String selectPostsQuery =
                "select p.postIdx as postIdx,\n" +
                        "                       u.userIdx as UserIdx,\n" +
                        "                       u.nickName as nickName,\n" +
                        "                      u.profileImgUrl as profileImgUrl,\n" +
                        "                       p.content as content,\n" +
                        "                       if(postLikeCount is null, 0., postLikeCount) as postLikeCount,\n" +
                        "                       if(commentCount is null, 0., commentCount) as commentCount,\n" +
                        "                       case when timestampdiff(second, p.updatedAt, current_timestamp) < 60\n" +
                        "                           then concat(timestampdiff(second, p.updatedAt, current_timestamp), '초 전')\n" +
                        "                           when timestampdiff(minute, p.updatedAt, current_timestamp) < 60\n" +
                        "                               then concat(timestampdiff(minute, p.updatedAt, current_timestamp), '분 전')\n" +
                        "                           when timestampdiff(hour, p.updatedAt, current_timestamp) < 24\n" +
                        "                               then concat(timestampdiff(hour, p.updatedAt, current_timestamp), '시간 전')\n" +
                        "                          when timestampdiff(day, p.updatedAt, current_timestamp) < 365\n" +
                        "                               then concat(timestampdiff(day, p.updatedAt, current_timestamp), '일 전')\n" +
                        "                           else timestampdiff(year, p.updatedAt, current_timestamp)\n" +
                        "                           end as updatedAt,\n" +
                        "                       if(pl.status = 'ACTIVE', 'Y', 'N') as likeOrNot\n" +
                        "                from post as p\n" +
                        "                    join user as u on u.userIdx = p.userIdx\n" +
                        "                    left join (select postIdx, userIdx, count(postLikeidx) as postLikeCount from postlike where status='ACTIVE') as pol on pol.postIdx = p.postIdx\n" +
                        "                    left join (select postIdx, count(commentIdx) as commentCount from comment where status='ACTIVE') as cc on cc.postIdx = p.postIdx\n" +
                        "                       left join follow as f on f.followerIdx = p.userIdx and f.status = 'ACTIVE'\n" +
                        "                    left join postlike as pl on pl.userIdx = f.followeeIdx and pl.postIdx = p.postIdx\n" +
                        "                where f.followerIdx = ? and p.status = 'ACTIVE'\n" +
                        "                group by p.postIdx;";

        int selectPostsQueryParams = userIdx;

        return this.jdbcTemplate.query(selectPostsQuery,
                (rs, rownum) -> new GetPostsRes(
                        rs.getInt("postIdx"),
                        rs.getInt("userIdx"),
                        rs.getString("nickName"),
                        rs.getString("profileImgUrl"),
                        rs.getString("content"),
                        rs.getInt("postLikeCount"),
                        rs.getInt("commentCount"),
                        rs.getString("updatedAt"),
                        rs.getString("likeOrNot"),
                        getPostImgRes = this.jdbcTemplate.query("select pi.postImgUrlIdx, pi.imgUrl\n" +
                                "from postimgurl as pi\n" +
                                "    join post as p on p.postIdx = pi.postIdx\n" +
                                "where pi.status = 'ACTIVE' and p.postIdx = ?;",
                        (rk,rowNum) -> new GetPostImgRes(
                                rk.getInt("postImgUrlIdx"),
                                rk.getString("imgUrl")
                        ),rs.getInt("postIdx")
                        )),selectPostsQueryParams);

    }

    public int insertPosts(PostPostsReq postPostsReq) {
        String insertPostsQuery = "insert into post(userIdx, content) values (?,?)";
        Object[] insertPostsParams = new Object[] {postPostsReq.getUserIdx(),postPostsReq.getContent()};

        this.jdbcTemplate.update(insertPostsQuery,insertPostsParams);

        String lastInsertIdxQuery= "select last_insert_id()";

        return this.jdbcTemplate.queryForObject(lastInsertIdxQuery, int.class);
    }

    public void insertPostImgUrls(int postIdx,PostImgUrlReq postImgUrlReq) {
        String insertPostImgUrlsQuery = "insert into postimgurl(postIdx,imgUrl) values(?,?)";
        Object[] insertPostsImgUrlParams = new Object[] {postIdx,postImgUrlReq.getImgUrl()};

        this.jdbcTemplate.update(insertPostImgUrlsQuery, insertPostsImgUrlParams);
    }

    public int updatePost(int postIdx, String content) {

        String updatePostQuery = "update post set content = ? where postIdx = ?";
        Object[] updatePostParams = new Object[]{content, postIdx};

        return this.jdbcTemplate.update(updatePostQuery, updatePostParams);
    }

    public int deletePost(int postIdx) {
        String deletePostQuery = "update post set status = 'INACTIVE' where postIdx = ?";
        Object[] deletePostParams = new Object[]{postIdx};

        return this.jdbcTemplate.update(deletePostQuery, deletePostParams);
    }
}
