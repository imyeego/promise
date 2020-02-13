package com.imyeego.promise;

public class ThenOnSubscribe<T> implements Promise.OnSubscribe<T> {

    final Promise<T> source;
    final Action<T> then;

    public ThenOnSubscribe(Promise<T> source, Action<T> then) {
        this.source = source;
        this.then = then;
    }

    @Override
    public void call(Subscriber<? super T> subscriber) {
        ThenSubscriber<T> thenOnSubscribe = new ThenSubscriber<>(subscriber, then);
        source.make(thenOnSubscribe);
    }

    static final class ThenSubscriber<T> implements Subscriber<T> {
        final Subscriber<? super T> actual;
        final Action<T> action;

        public ThenSubscriber(Subscriber<? super T> actual, Action<T> action) {
            this.actual = actual;
            this.action = action;
        }

        @Override
        public void onNext(T t) {
            action.call(t);
            actual.onNext(t);
        }

        @Override
        public void onError(Throwable e) {
            actual.onError(e);
        }
    }
}
