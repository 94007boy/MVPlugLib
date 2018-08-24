package com.djjie.mvpluglib.model;

import android.text.TextUtils;

import java.util.HashMap;

public class MVPlugRepository{

    private final HashMap<String, Object> pageFlagMap;

    public MVPlugRepository() {
        pageFlagMap = new HashMap<>();
    }

    public Object getPageFlagByTag(String tag) {
        if (TextUtils.isEmpty(tag))return null;
        return pageFlagMap.get(tag);
    }

    public void setPageFlagByTag(String tag,Object pageFlag) {
        pageFlagMap.put(tag,pageFlag);
    }

}
