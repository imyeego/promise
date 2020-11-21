package com.imyeego.promise;

import java.util.concurrent.ExecutionException;

public interface Fun1<T> {
    T call() throws ExecutionException;
}
