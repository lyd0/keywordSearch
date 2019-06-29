package com.advanceSearch.utils;

import java.io.FileNotFoundException;
import java.io.InputStream;


public class Config {
    public static Rule defaultRule=null;

    static{

        InputStream is = Rule.class.getResourceAsStream("/rule.json");
        defaultRule=Rule.createFromInputStream(is);
    }
}
