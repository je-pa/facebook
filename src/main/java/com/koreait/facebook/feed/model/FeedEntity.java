package com.koreait.facebook.feed.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
@ToString
public class FeedEntity {
    private int ifeed;
    private String location;
    private String ctnt;
    private int iuser;
    private String regdt;

}
