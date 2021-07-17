package com.koreait.facebook.security;

import com.koreait.facebook.security.model.CustomUserPrincipal;
import com.koreait.facebook.user.model.UserEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component //interface를 구현하고 있는 친구중에 얘를 쓰겠다!!
public class AuthenticationFacadeImpl implements IAuthenticationFacade{
    @Override
    public UserEntity getLoginUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserPrincipal userDetails = (CustomUserPrincipal)auth.getPrincipal(); //인증받기 위한 정보 //Principal(ID) Credential(pw)
        return userDetails.getUser();
    }

    @Override
    public int getLoginUserPk() {
        return getLoginUser().getIuser();
    }
}
