package com.ecommerce.back.statistic;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class Statistic {
    public static final ConcurrentHashMap<String, Date> onlineUsers = new ConcurrentHashMap<>();
        public static final ConcurrentHashMap<String, Object> userNameLock = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<String, Object> productNameLock = new ConcurrentHashMap<>();
}
