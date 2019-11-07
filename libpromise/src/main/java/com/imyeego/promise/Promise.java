package com.imyeego.promise;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Promise<T> {
    private static ExecutorService service;
    private volatile IAction<T> iAction;
    private Callable callable;
    private Future future;
    private IError iError;

    public static <T> Promise<T> of(Callable<T> t) {
        return new Promise<>(t);
    }

    private <T> Promise(Callable<T> t){
        callable = t;
        executorService();
    }

    private synchronized ExecutorService executorService() {
        if (service == null) {
            service = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 10, TimeUnit.SECONDS,
                    new SynchronousQueue<Runnable>(), Utils.threadFactory("Promise-Executor",false));
        }
        return service;
    }

    public Promise<T> then(Action<T> action) {
        iAction = new ThenAction<T>(iAction, action);
        return this;
    }

    public Promise<T> ui(Action<T> action) {
        iAction = new UIAction<T>(iAction, action);
        return this;
    }


    public Promise<T> make() {
        Future<T> future = service.submit(callable);
        this.future = future;
        service.execute(new Loop<T>(future));
        return this;
    }

    public Promise<T> error(Err err) {
        iError = new Error(err);
        return this;
    }

    public void cancel() {
        if (!future.isCancelled())
            future.cancel(true);
        if (service != null) {
            service.shutdown();
        }
    }

    private class Loop<V> implements Runnable {
        Future<V> future;

        public Loop(Future<V> future) {
            this.future = future;
        }

        @Override
        public void run() {
            try {
                V t = future.get();
                if (t != null && iAction != null) {
                    iAction.execute((T) t);
                }

            } catch (InterruptedException | ExecutionException e) {
                if (iError != null) {
                    iError.error(e);
                }
//                e.printStackTrace();
            }
        }
    }


}
