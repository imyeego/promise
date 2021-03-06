package com.imyeego.promise;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Promise<T> {
    private final OnSubscribe<T> onSubscribe;


    public static <T> Promise<T> of(Callable<T> t) {
        return create(new OfOnSubscribe<>(t));

    }

    public static <T> Promise<T> forEach(Fun1<T> t, long period) {
        return create(new ForOnSubscribe<T>(t, period));
    }

    private Promise(OnSubscribe<T> onSubscribe) {
        this.onSubscribe = onSubscribe;
    }


    public Promise<T> then(Action<T> action) {
        return create(new ThenOnSubscribe<>(this, action));
    }

    public Promise<T> ui(Action<T> action) {
        return create(new UIOnSubscribe<T>(this, action));
    }

    public <R> Promise<R> map(Func<T, R> func) {
        return create(new MapOnSubscribe<T, R>(this, func));
    }

    private static <T> Promise<T> create(OnSubscribe<T> f) {
        return new Promise<T>(f);
    }

    public Promise<T> make() {
        return make(new Empty<>());
    }

    public Promise<T> make(Subscriber<? super T> subscriber) {
        this.onSubscribe.call(subscriber);
        return this;
    }

    public Promise<T> excep(Err err) {
        return create(new CatchOnSubscribe<T>(this, err));
    }

    public void cancel() {
        cancel(new Empty<>());
    }

    public void cancel(Subscriber<? super T> subscriber) {
        this.onSubscribe.cancel(subscriber);
    }

    public interface OnSubscribe<T> extends Action<Subscriber<? super T>> {
        void cancel(Subscriber<? super T> subscriber);
    }

    static final class Empty<T> implements Subscriber<T> {
        @Override
        public void onNext(T t) {

        }

        @Override
        public void onError(Throwable e) {

        }
    }

}
