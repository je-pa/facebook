package com.koreait.facebook;

import com.koreait.facebook.common.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FacebookApplicationTests {

    @Autowired
    private EmailService email;

    @Test
    void sendEmail() {
        String to = "qkrwldms0502@naver.com";
        String subject = "제ddd";
        String txt = "내용입니sss <a href=\"http://localhost:8090/user/login\">로그인으로이동</a>";
        email.sendMimeMessage(to, subject, txt);
    }

}
