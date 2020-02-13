package com.imyeego.promise;

public interface Subscriber<T> {
    void onNext(T t);
    void onError(Throwable e);
}
