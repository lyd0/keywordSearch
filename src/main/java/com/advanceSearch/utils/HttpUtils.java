package com.advanceSearch.utils;

import java.io.IOException;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class HttpUtils {
    public static Document fetchDoc(String url) throws Exception {


        Document doc=null;

        Exception ex =  new Exception();
        for (int i = 0; i <= 3; i++) {
            try {

                doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36").get();

                return doc;
            } catch (IOException e) {
                ex=e;
            }

        }
        throw ex;
    }
}
