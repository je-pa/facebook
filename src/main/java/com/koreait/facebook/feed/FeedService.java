package com.koreait.facebook.feed;

import com.koreait.facebook.common.MyFileUtils;
import com.koreait.facebook.feed.model.FeedDomain;
import com.koreait.facebook.feed.model.FeedEntity;
import com.koreait.facebook.feed.model.FeedImgEntity;
import com.koreait.facebook.security.IAuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class FeedService {
    @Autowired private FeedMapper mapper;
    @Autowired private IAuthenticationFacade auth;
    @Autowired private MyFileUtils myFileUtils;

    public int reg(MultipartFile[] imgArr, FeedEntity param){
        if(imgArr==null && param.getCtnt()==null){ return 0;  }
        param.setIuser(auth.getLoginUserPk());
        //t_feed insert해야함 -그래야 ifeed 얻을 수 있다
        int result = mapper.insFeed(param);
        if(param.getIfeed()>0 && imgArr != null && imgArr.length>0){ //등록이 잘 되었음
            //이미지 등록
            FeedImgEntity param2 = new FeedImgEntity();
            param2.setIfeed(param.getIfeed());
            //이미지 저장
            String target = "feed/"+param.getIfeed();
            for(MultipartFile img : imgArr){
                String saveFileNm = myFileUtils.transferTo(img, target);
                if(saveFileNm != null){ //이미지 정보 db에 저장
                    param2.setImg(saveFileNm);
                    mapper.insFeedImg(param2);
                }
            }
        }
        System.out.println(param);
        return result;
    }

    public List<FeedDomain> selFeedList() {
        return mapper.selFeedList();
    }
}
