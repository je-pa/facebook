package com.koreait.facebook.feed.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FeedCmtDomain extends FeedCmtEntity{
    private String writer;
    private String writerProfile;
}
