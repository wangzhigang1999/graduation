package com.bupt.graduation.utils;

import com.google.gson.Gson;

public class JsonUtil {
    private final static Gson gs = new Gson();


    public static synchronized String toJson(Object o) {
        return gs.toJson(o);
    }

}
