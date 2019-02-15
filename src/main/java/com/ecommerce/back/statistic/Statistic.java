package com.ecommerce.back.statistic;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class Statistic {
    /**
     * 用于统计每个用户名登陆Token过期的时间
     */
    public static final ConcurrentHashMap<String, Date> onlineUsers = new ConcurrentHashMap<>();
    /**
     * 用于给每个用户名放入对应的用做锁的Object，用了putIfAbsent方法防止并发时取到不同的Object
     */
    public static final ConcurrentHashMap<String, Object> userNameLock = new ConcurrentHashMap<>();
    /**
     * 用于给每个商品名放入对应的用做锁的Object，用了putIfAbsent方法防止并发时取到不同的Object
     */
    public static final ConcurrentHashMap<String, Object> productNameLock = new ConcurrentHashMap<>();
}
