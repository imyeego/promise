package com.imyeego.promise;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class MapAction<T, R> extends AbstractAction<T> {
    private Fun0<T, R> fun0;
    private Promise<R> promise;

    public MapAction(IAction<T> target, Fun0<T, R> fun0, Promise<R> promise) {
        super(target);
        this.fun0 = fun0;
        this.promise = promise;
    }

    @Override
    public void execute(T t) {
        if (target != null) {
            target.execute(t);
        }
    }

}
