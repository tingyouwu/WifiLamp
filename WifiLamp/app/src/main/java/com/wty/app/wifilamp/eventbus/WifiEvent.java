package com.wty.app.wifilamp.eventbus;

import java.util.HashMap;

/**
 * @Desc wifi EventBus 事件
 **/
public class WifiEvent {

    public static int Type_Bright = 1;//亮度
    public static int Type_Color = 1;//颜色
    public static int Type_Gradien = 1;//呼吸

    private int type;

    private HashMap<String, Object> hashMap;

    public WifiEvent(int type){
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public HashMap<String, Object> appendHashParam(String key, Object object) {
        if (hashMap == null) {
            hashMap = new HashMap<>();
        }
        hashMap.put(key, object);
        return hashMap;
    }

    public HashMap<String, Object> getHashMap() {
        return hashMap;
    }
}
