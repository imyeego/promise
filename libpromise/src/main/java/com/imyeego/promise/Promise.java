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
    private volatile IAction0 iAction0;
    private Callable callable;
    private Runnable runnable;
    private Future future;
    private IError iError;

    public static <T> Promise<T> of(Callable<T> t) {
        return new Promise<>(t);
    }
    public static Promise of(Runnable r) {
        return new Promise<>(r);
    }

    public <T>Promise() {

    }

    public Promise(Runnable runnable) {
        this.runnable = runnable;
        service = Utils.executorService();
    }

    private <T> Promise(Callable<T> t){
        callable = t;
        service = Utils.executorService();

    }



    public Promise<T> then(Action<T> action) {
        iAction = new ThenAction<T>(iAction, action);
        return this;
    }

    public Promise then(Action0 action0) {
        iAction0 = new ThenAction0(iAction0, action0);
        return this;
    }

    public Promise<T> ui(Action<T> action) {
        iAction = new UIAction<T>(iAction, action);
        return this;
    }

    public Promise ui(Action0 action0) {
        iAction0 = new UIAction0(iAction0, action0);
        return this;
    }

    public <R> Promise<R> map(Fun0<T, R> fun0) {
        iAction = new MapAction<T, R>(iAction, fun0);
        return new Promise<>();
    }

    public Promise execute() {
        service.execute(new NamedRunnale(runnable));
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

    }

    class Loop<V> implements Runnable {
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

    private class NamedRunnale implements Runnable {
        Runnable runnable;

        NamedRunnale(Runnable runnable) {
            this.runnable = runnable;
        }

        @Override
        public void run() {
            runnable.run();
            if (iAction0 != null) {
                iAction0.execute();
            }
        }
    }



}
