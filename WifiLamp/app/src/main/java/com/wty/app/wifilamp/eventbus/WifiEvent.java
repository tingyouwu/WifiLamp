package com.wty.app.wifilamp.eventbus;

import java.util.HashMap;

/**
 * @Desc wifi EventBus 事件
 **/
public class WifiEvent {

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
