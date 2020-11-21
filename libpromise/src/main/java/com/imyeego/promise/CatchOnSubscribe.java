package com.imyeego.promise;

public class CatchOnSubscribe<T> implements Promise.OnSubscribe<T> {

    final Promise<T> source;
    final Err err;
    CatchSubscriber<T> parent;

    public CatchOnSubscribe(Promise<T> source, Err err) {
        this.source = source;
        this.err = err;
    }

    @Override
    public void call(Subscriber<? super T> subscriber) {
        parent = new CatchSubscriber<>(subscriber, err);
        source.make(parent);
    }

    @Override
    public void cancel(Subscriber<? super T> subscriber) {
        source.cancel(parent);
    }

    static final class CatchSubscriber<T> implements Subscriber<T> {
        final Subscriber<? super T> actual;
        final Err err;

        public CatchSubscriber(Subscriber<? super T> actual, Err err) {
            this.actual = actual;
            this.err = err;
        }

        @Override
        public void onNext(T t) {

        }

        @Override
        public void onError(Throwable e) {
            err.call(e);
        }
    }
}
