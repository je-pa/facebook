package com.koreait.facebook.feed;

import com.koreait.facebook.common.MyConst;
import com.koreait.facebook.feed.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/feed")
public class FeedController {
    @Autowired private FeedService service;
    @Autowired private MyConst myConst;

    @GetMapping("/home")
    public void home() {}

    @GetMapping("/reg")
    public void reg(FeedEntity obj){}

    @ResponseBody
    @PostMapping("/reg")
    public Map<String, Integer> reg(MultipartFile[] imgArr, FeedEntity param){
//        System.out.println("length : "+ imgArr.length);
//        System.out.println(": " +param);
        Map<String,Integer> res = new HashMap<>();
        res.put(myConst.RESULT, service.reg(imgArr,param));
        return res;
    }

    @ResponseBody
    @GetMapping("/list")
    public List<FeedDomain> selFeedlist(){
        return service.selFeedList();
    }

    @ResponseBody
    @GetMapping("/list2")
    public List<FeedDomain2> selFeedList2(FeedDTO param){
        return service.selFeedList2(param);
    }

    @ResponseBody
    @GetMapping("/fav")//쿼리로 ifeed, iuser, type(1:ins/0:del)보낼것
    public int favProc(FeedFavEntity param , int type){
        System.out.println(param);
        System.out.println("type: "+type);
        return service.feedFavProc(param,type);
    }

    @ResponseBody
    @PostMapping("/cmt")
    public int insFeedCmt(@RequestBody FeedCmtEntity param){
        return service.insFeedCmt(param);
    }
}
