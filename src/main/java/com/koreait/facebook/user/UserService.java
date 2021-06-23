package com.koreait.facebook.user;

import com.koreait.facebook.common.EmailServiceImpl;
import com.koreait.facebook.common.MySecurityUtils;
import com.koreait.facebook.user.model.UserEntity;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private EmailServiceImpl email;

    @Autowired
    private UserMapper mapper;

    @Autowired
    private MySecurityUtils secUtils;

    public int join(UserEntity param){
        String authCd = secUtils.getRandomDigit(5);

        //비밀번호 암호화
        String hashedPw = BCrypt.hashpw(param.getPw(), BCrypt.gensalt());
        param.setPw(hashedPw);
        param.setAuthCd(authCd);
        int result = mapper.join(param);

        if(result==1){ //메일 쏘기 (id, authcd 값을 메일로 쏜다.)
            String subject = "[얼굴책] 인증메일입니다.";
            String txt = String.format("<a href=\"http://localhost:8090/user/auth?email=%s&authCd=%s\">인증하기</a>",
                    param.getEmail(),authCd);
            email.sendMimeMessage(param.getEmail(),subject,txt);
        }
        return result;
    }

    public void sendEmail(){
        String to= "qkrwldms0502@naver.com";
        String subject = "제목입";
        String txt = "내용입";
        email.sendSimpleMessage(to, subject, txt);
    }

    public int auth(UserEntity param){
        return mapper.auth(param);
    }

    //로그인
    public String login(UserEntity param) {
        UserEntity loginUser = mapper.selUser(param);

        return "";
    }
}