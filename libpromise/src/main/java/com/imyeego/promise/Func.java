package com.imyeego.promise;

public interface Func<T, R> {
    R call(T t);
}
