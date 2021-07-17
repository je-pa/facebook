package com.koreait.facebook.user;

import com.koreait.facebook.common.EmailService;
import com.koreait.facebook.common.MyConst;
import com.koreait.facebook.common.MyFileUtils;
import com.koreait.facebook.common.MySecurityUtils;
import com.koreait.facebook.feed.FeedMapper;
import com.koreait.facebook.feed.model.FeedDTO;
import com.koreait.facebook.feed.model.FeedDomain2;
import com.koreait.facebook.security.IAuthenticationFacade;
import com.koreait.facebook.security.UserDetailsServiceImpl;
import com.koreait.facebook.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    @Autowired private EmailService email;
    @Autowired private IAuthenticationFacade auth;
    //인터페이스 -> 구현한 친구(class)만 바꾸면됨
    @Autowired private MyFileUtils myFileUtils;
    @Autowired private MySecurityUtils secUtils;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private UserMapper mapper;
    @Autowired private FeedMapper feedMapper;
    @Autowired private UserProfileMapper profileMapper;
    @Autowired private UserDetailsServiceImpl userDetailService;

    @Autowired
    private MyConst myConst;

    public int join(UserEntity param){
        String authCd = secUtils.getRandomDigit(5);

        //비밀번호 암호화
        String hashedPw = passwordEncoder.encode(param.getPw());
        param.setPw(hashedPw);
        param.setAuthCd(authCd);
        int result = userDetailService.join(param);

        if(result==1){ //메일 쏘기 (id, authcd 값을 메일로 쏜다.)
            String subject = "[얼굴책] 인증메일입니다.";
            String txt = String.format("<a href=\"http://localhost:8090/user/auth?email=%s&authCd=%s\">인증하기</a>",
                    param.getEmail(),authCd);
            email.sendMimeMessage(param.getEmail(),subject,txt);
        }
        return result;
    }

    public void sendEmail(){
//        String to= "qkrwldms0502@naver.com";
//        String subject = "제목입";
//        String txt = "내용입";
//        email.sendSimpleMessage(to, subject, txt);
        String to = "qkrwldms0502@naver.com";
        String subject = "제ddd";
        String txt = "내용입니sss <a href=\"http://localhost:8090/user/login\">로그인으로이동</a>";
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
    public UserDomain selUserProfile(UserDTO param){
        param.setMeIuser(auth.getLoginUserPk());
        return profileMapper.selUserProfile(param);
    }

    public List<UserProfileEntity> selUserProfileList(UserEntity param) {
        return profileMapper.selUserProfileList(param);
    }

    //메인 이미지 변경
    //Map< 키값 , 결과값>
    public Map<String,Object> updUserMainProfile(UserProfileEntity param) {
        UserEntity loginUser = auth.getLoginUser();

        param.setIuser(loginUser.getIuser());
        int result = mapper.updUserMainProfile(param);
        if(result==1){ //시큐리티 세션에 있는 loginUser에 있는 mainProfile값도 변경
            System.out.println("img : "+param.getImg());
            loginUser.setMainProfile(param.getImg());
        }
        Map<String, Object> res = new HashMap<>();
        res.put("result", result);
        res.put("img",param.getImg());

        return res;
    }

//    public List<FeedDomain2> selFeedList2(FeedDTO param) {//iuser값도 같이 보내줄거다 로그인한사람만 말고..?
//        return feedMapper.selFeedList2(param);
//    }

    //팔로우하기
    public Map<String,Object> insUserFollow(UserFollowEntity param){
        param.setIuserMe(auth.getLoginUserPk());
        Map<String,Object> res = new HashMap();
        res.put(myConst.RESULT, mapper.insUserFollow(param));

        return res;
    }
    public List<FeedDomain2> selFeedList2(FeedDTO param) {
        return feedMapper.selFeedList2(param);
    }
    public List<UserDomain> selUserFollowList(UserFollowEntity param) {
        param.setIuserMe(auth.getLoginUserPk());
        return mapper.selUserFollowList(param);
    }

    public List<UserDomain> selUserFollowerList(UserFollowEntity param){
        param.setIuserMe(auth.getLoginUserPk());
        return mapper.selUserFollowerList(param);
    }

    //팔로우ㅅ취소
    public Map<String,Object> delUserFollow(UserFollowEntity param){
        param.setIuserMe(auth.getLoginUserPk());
        int result = mapper . delUserFollow(param);

        Map<String, Object> res = new HashMap<>();
        res.put(myConst.RESULT, result);
        if(result == 1){
            UserFollowEntity param2 = new UserFollowEntity();
            param2.setIuserMe(param.getIuserYou());
            param2.setIuserYou(param.getIuserMe());

            UserFollowEntity result2 = mapper.selUserFollow(param2);
            res.put(myConst.YOU_FOLLOW_ME, result2);
        }
        return null;
    }
}