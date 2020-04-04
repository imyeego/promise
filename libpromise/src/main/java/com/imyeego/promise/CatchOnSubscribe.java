package com.imyeego.promise;

public class CatchOnSubscribe<T> implements Promise.OnSubscribe<T> {

    final Promise<T> source;
    final Err err;

    public CatchOnSubscribe(Promise<T> source, Err err) {
        this.source = source;
        this.err = err;
    }

    @Override
    public void call(Subscriber<? super T> subscriber) {
        CatchSubscriber<T> catchSubscriber = new CatchSubscriber<>(subscriber, err);
        source.make(catchSubscriber);
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
