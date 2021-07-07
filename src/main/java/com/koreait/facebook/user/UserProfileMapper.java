package com.koreait.facebook.user;

import com.koreait.facebook.user.model.UserEntity;
import com.koreait.facebook.user.model.UserDomain;
import com.koreait.facebook.user.model.UserProfileEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserProfileMapper {
    int insUserProfile(UserProfileEntity profileEntity);
    UserDomain selUserProfile(UserEntity param);
    List<UserProfileEntity> selUserProfileList(UserEntity param);
}