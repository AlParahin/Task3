package com.example.task3;

import java.util.Date;

public class CachedObject {
    private final Object cachedObject;
    private Date timeToLive;

    public CachedObject(Object cachedObject, Date timeToLive) {
        this.cachedObject = cachedObject;
        this.timeToLive = timeToLive;
    }

    public Object getCachedObject() {
        timeToLive = new Date((new Date()).getTime() + 1000L);
        return cachedObject;
    }

    public boolean isExpired() {
        return this.timeToLive.compareTo(new Date()) <= 0;
    }

}
