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
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.25 Safari/537.36 Core/1.70.3704.400 QQBrowser/10.4.3587.400").get();
                System.out.println(System.currentTimeMillis()-t);
                return doc;
            } catch (IOException e) {
                ex=e;
            }

        }
        throw ex;
    }
}
