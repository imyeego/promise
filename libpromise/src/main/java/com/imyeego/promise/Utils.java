package com.imyeego.promise;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.ThreadFactory;

public class Utils {

    private static volatile Handler handler;

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
