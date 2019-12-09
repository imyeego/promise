package com.imyeego.promise;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Utils {

    private static volatile Handler handler;
    private static volatile ExecutorService service;

    public static Handler getMainHandler() {
        if (handler == null) {
            synchronized (Utils.class) {
                if (handler == null) {
                    handler = new Handler(Looper.getMainLooper());
                }
            }
        }

        return handler;
    }

    public static synchronized ExecutorService executorService() {
        if (service == null) {
            service = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 10, TimeUnit.SECONDS,
                    new SynchronousQueue<Runnable>(), Utils.threadFactory("Promise-Executor",false));
        }
        return service;
    }

    public static ThreadFactory threadFactory(final String name, final boolean daemon) {
        return new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull Runnable r) {
                Thread result = new Thread(r, name);
                result.setDaemon(daemon);
                return result;
            }
        };
    }
}
