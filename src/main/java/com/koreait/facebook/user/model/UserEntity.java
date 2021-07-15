package com.koreait.facebook.user.model;

import lombok.*;
//import org.apache.ibatis.type.Alias;

@Getter
@Setter
@ToString
@Builder //빌드패턴 객체화할때 필요한 것들만 해서 만들수있다
@NoArgsConstructor //기본생성자
@AllArgsConstructor
public class UserEntity {
    private int iuser;
    private String provider;
    private String email;
    private String pw;
    private String nm;
    private String tel;
    private String authCd;
    private String mainProfile;
    private String regdt;
}
