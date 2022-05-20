package com.example.demo.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserPostsRes {
    // 포스트 객체
    private int postIdx;
    private String postImgUrl;
}
