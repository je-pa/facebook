package com.koreait.facebook.user.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDomain extends UserEntity{
    private int cntFeed;
    private int cntFollower; //팔로워
    private int cntFollow;
    private int isYouFollowMe; //너는 나를 팔로우 했니
    private int isMeFollowYou; //나는 너를 팔로우 했니
}
