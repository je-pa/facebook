package com.koreait.facebook.user;

import com.koreait.facebook.common.EmailService;
import com.koreait.facebook.common.MyFileUtils;
import com.koreait.facebook.common.MySecurityUtils;
import com.koreait.facebook.security.IAuthenticationFacade;
import com.koreait.facebook.user.model.UserEntity;
import com.koreait.facebook.user.model.UserProfileEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private EmailService email;

    @Autowired
    private IAuthenticationFacade auth;
    //인터페이스 -> 구현한 친구(class)만 바꾸면됨

    @Autowired
    private MyFileUtils myFileUtils;

    @Autowired
    private MySecurityUtils secUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper mapper;

    @Autowired
    private UserProfileMapper profileMapper;

    public int join(UserEntity param){
        String authCd = secUtils.getRandomDigit(5);

        //비밀번호 암호화
        String hashedPw = passwordEncoder.encode(param.getPw());
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

    //이메일 인증처리
    public int auth(UserEntity param){
        return mapper.auth(param);
    }

    public void profileImg(MultipartFile[] imgArr) {
        UserEntity loginUser = auth.getLoginUser();
        int iuser = auth.getLoginUserPk();

        System.out.println("iuser : " + iuser);
        String target = "profile/" + iuser;//D:\springImg\profile\11

        UserProfileEntity param = new UserProfileEntity();
        param.setIuser(iuser);

        for(MultipartFile img : imgArr) {
            String saveFileNm = myFileUtils.transferTo(img, target);//파일명
            // myFileUtils에서 정리르 해놓았기때문에 훨씬 짧음

            /* saveFileNm이 null이 아니라면 t_user_profile 테이블에 insert를 해주세요*/
            if(saveFileNm!=null){
                param.setImg(saveFileNm);
                //실행되고                                      한번 실행하면 null아니어서 실행만함
                if(profileMapper.insUserProfile(param) ==1 && loginUser.getMainProfile()==null){
                    UserEntity param2 = new UserEntity();
                    param2.setIuser(iuser);
                    param2.setMainProfile(saveFileNm);
                    if(mapper.updUser(param2)==1){
                        loginUser.setMainProfile(saveFileNm); //시큐리티에 적용
                    }
                }
            }
        }
    }

    public List<UserProfileEntity> selUserProfileList(UserEntity param) {
        return profileMapper.selUserProfileList(param);
    }

    //메인 이미지 변경
    public int updUserMainProfile(UserProfileEntity param) {
        param.setIuser(auth.getLoginUserPk());
        return mapper.updUserMainProfile(param);
    }

}