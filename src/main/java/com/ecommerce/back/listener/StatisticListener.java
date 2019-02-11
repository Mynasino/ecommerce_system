package com.ecommerce.back.listener;

import com.ecommerce.back.dao.UserDAO;
import com.ecommerce.back.statistic.Statistic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@WebListener
@Component
public class StatisticListener implements ServletContextListener {
    private final UpdateOnlineUserRunnable updateOnlineUserRunnable = new UpdateOnlineUserRunnable();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        //start monitor online user -> "/login"
        new Thread(updateOnlineUserRunnable).start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        updateOnlineUserRunnable.stopUpdate();
    }

    class UpdateOnlineUserRunnable implements Runnable {
        private static final long UPDATE_INTERVAL_MILLIS = 10 * 1000;
        private boolean continueUpdateFlag = true;

        @Override
        @SuppressWarnings("InfiniteLoopStatement")
        public void run() {
            while (continueUpdateFlag) {
                try {
                    Thread.sleep(UPDATE_INTERVAL_MILLIS);
                    ConcurrentHashMap<String, Date> onlineUsers = Statistic.onlineUsers;
                    for (Map.Entry<String, Date> entry : onlineUsers.entrySet()) {
                        Date date = entry.getValue();
                        if (date != null && date.before(new Date()))
                            onlineUsers.remove(entry.getKey());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        void stopUpdate() {
            this.continueUpdateFlag = false;
        }
    }
}
