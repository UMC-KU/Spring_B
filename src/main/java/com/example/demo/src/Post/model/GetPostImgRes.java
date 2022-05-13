package com.example.demo.src.Post.model;


import com.example.demo.src.user.model.GetUserInfoRes;
import com.example.demo.src.user.model.GetUserPostsRes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class GetPostImgRes {
    private int postImgUrlIdx;
    private String imgUrl;
}
