package com.example.demo.src.auth.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostLoginReq {
    // email로 로그인하기 중점이기 때문에 id를 email로 받음
    private String email;
    private String password;

}
