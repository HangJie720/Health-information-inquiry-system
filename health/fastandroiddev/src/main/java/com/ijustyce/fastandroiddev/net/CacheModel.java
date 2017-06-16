package com.ijustyce.fastandroiddev.net;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangchun on 16/4/28.  缓存Model
 */
public class CacheModel implements Serializable {

    private Map<String, String> map = new HashMap<>();

    public CacheModel() {
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }
}
