package com.koreait.facebook.user;

import com.koreait.facebook.common.MyConst;
import com.koreait.facebook.security.UserDetailsImpl;
import com.koreait.facebook.user.model.UserEntity;
import com.koreait.facebook.user.model.UserProfileEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService service;

    @Autowired
    private MyConst myConst;

    @GetMapping("/login")
    public void login(UserEntity userEntity){}

    @GetMapping("/join")
    public void join(UserEntity userEntity){}

    @PostMapping("/join")
    public String joinProc(UserEntity userEntity){
        service.join(userEntity);
        return "redirect:login?needEmail=1";
    }
    @GetMapping("/email")
    public  String email(){
        service.sendEmail();
        return "";
    }
    @GetMapping("/auth")
    public String auth(UserEntity param){
        int result = service.auth(param);
        return "redirect:login?auth="+result;
    }

    @GetMapping("/profile")
    public void profile(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        UserEntity loginUser = userDetails.getUser();
        model.addAttribute(myConst.PROFILE_LIST, service.selUserProfileList(loginUser));
    }

    @PostMapping("/profileImg")// imgArr : 이름을 맞춰줘야함
    public String profileImg(MultipartFile[] imgArr) {
        service.profileImg(imgArr);
        return "redirect:profile";
    }

    @ResponseBody
    @GetMapping("/mainProfile")
    public Map<String,Object> mainProfile(UserProfileEntity param){
        return service.updUserMainProfile(param);
    }
}