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
                long t = System.currentTimeMillis();
                doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.93 Safari/537.36").get();
                System.out.println(System.currentTimeMillis()-t);
                return doc;
            } catch (IOException e) {
                ex=e;
            }

        }
        throw ex;
    }
}
