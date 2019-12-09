package com.imyeego.promise;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class MapAction<T, R> extends AbstractAction<T> {
    private Fun0<T, R> fun0;

    public MapAction(IAction<T> target, Fun0<T, R> fun0) {
        super(target);
        this.fun0 = fun0;
    }

    @Override
    public void execute(T t) {
        if (target != null) {
            target.execute(t);
        }
        Promise.of(new WrapperCallable<R>(fun0, t)).make();
    }

    class WrapperCallable<R> implements Callable<R> {
        Fun0<T, R> fun0;
        T t;

        public WrapperCallable(Fun0<T, R> fun0, T t) {
            this.t = t;
            this.fun0 = fun0;
        }

        @Override
        public R call() throws Exception {
            return fun0.call(t);
        }
    }
}
