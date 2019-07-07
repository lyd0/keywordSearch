package com.advanceSearch.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.advanceSearch.entity.KeyWord;
import com.advanceSearch.entity.SearchItem;
import com.advanceSearch.service.AdvanceSearchService;
import com.advanceSearch.service.SpiderSearch;


@Controller
@RequestMapping("views/")
public class AdvanceSearchController {
    @Autowired
    private AdvanceSearchService advanceSearchService;
    @Autowired
    private SpiderSearch spiderSearch;

    /**
     * 根据关键字搜索并抓取
     * @param key
     * @return
     */
    @RequestMapping(value="advanceSearch")
    public String searchAndCrawling(@RequestParam("question_text") String key, Map map){

        if("".equals(key.trim())) {
            return "index";
        }

        List<SearchItem> list = new ArrayList<SearchItem>();
        int flag=1; //flag=1,该关键词没搜索过，0代表搜索过
        if(flag != advanceSearchService.judgeKey(key)){

            list = advanceSearchService.query(key);
            if(list.size() == 0 || list.size()<3) {
                try {
                    spiderSearch.searchByKey(key); //抓取

                } catch (UnsupportedEncodingException e) {

                }

            }
            SearchItem unreadSearchItem = advanceSearchService.getUnreadSearchItem(key);
            map.put("content",unreadSearchItem);
            return "index";
        }

        try {
            long t = System.currentTimeMillis();
            spiderSearch.searchByKey(key); //抓取
            System.out.println("--"+(System.currentTimeMillis()-t));
            SearchItem unreadSearchItem = advanceSearchService.getUnreadSearchItem(key);
            map.put("content",unreadSearchItem);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "index";
    }




    @RequestMapping(value="advanceSearch/findAllSearch")
    public List<SearchItem> findAllSearch(){
        List<SearchItem> list = new ArrayList<SearchItem>();
        list = advanceSearchService.findAllSearch();
        return list;
    }

    /**
     * 查询所有关键字
     * @return
     */
    @RequestMapping(value="advanceSearch/findAllKey")
    public List<KeyWord> findAllKey(){
        List<KeyWord> keys = new ArrayList<KeyWord>();
        keys = advanceSearchService.findAllKey();
        return keys;
    }


}
