package com.example.demo.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

/*
    게시글 보기
    내가 팔로우하는 사람들의
    - 유저 정보
    - 포스트 리스트
*/

@Getter
@Setter
@AllArgsConstructor
public class GetUserFeedRes {
    // 피드 보기
      // 1. 내 피드
      // 2. 다른 유저의 피드
    // -> 구별하기 위한 column 전달
    private boolean _isMyFeed;

    private GetUserInfoRes getUserInfo;
    private List<GetUserPostsRes> getUserPosts;
}
