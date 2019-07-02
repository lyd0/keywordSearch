package com.advanceSearch.dao;

import com.advanceSearch.entity.SearchItem;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

public class SearchItemsProvider {
    public String insertAll(Map map) {
        List<SearchItem> searchItems = (List<SearchItem>) map.get("list");
        StringBuilder sb = new StringBuilder();
        //"insert into searchitem(title,content,url,searchKey) values(#{title},#{content},#{url},#{searchKey})"
        sb.append("insert into searchitem");
        sb.append("(title,content,url,searchKey) ");
        sb.append("values");
//        System.out.println("0-0-0"+sb);
        System.out.println("searchItems ::: " + searchItems);
        MessageFormat mf = new MessageFormat("(#'{'list[{0}].title},#'{'list[{0}].content},#'{'list[{0}].url},#'{'list[{0}].searchKey})");

        for (int i = 0; i<searchItems.size(); i++) {
            sb.append(mf.format(new Object[]{i}));
            if(i<searchItems.size() -1) {
                sb.append(",");
            }
        }
//        System.out.println("0-0-1"+sb);
        return sb.toString();
    }
}
