package com.example.demo.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserInfoRes {
    // 유저 정보 객체
    private String nickName;
    private String name;
    private String profileImgUrl;
    private String website;
    private String introduction;
    private int followerCount;
    private int followingCount;
    private int postCount;
}
