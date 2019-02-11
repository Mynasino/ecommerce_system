package com.ecommerce.back.statistic;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class Statistic {
    public static final ConcurrentHashMap<String, Date> onlineUsers = new ConcurrentHashMap<>();
    public static final ReentrantLock[] userLocks = new ReentrantLock[100000];

    static {
        for (int i = 0; i < userLocks.length; i++)
            userLocks[i] = new ReentrantLock();
    }
}
