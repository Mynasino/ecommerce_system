package com.ecommerce.back.statistic;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

public class Statistic {
    public static final ConcurrentHashMap<String, Date> onlineUsers = new ConcurrentHashMap<>();
}
