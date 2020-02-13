package com.imyeego.promise;

public class MapOnSubscribe<T, R> implements Promise.OnSubscribe<R> {
    final Promise<T> source;
    final Func<? super T, ? extends R> func;

    public MapOnSubscribe(Promise<T> source, Func<? super T, ? extends R> func) {
        this.source = source;
        this.func = func;
    }

    @Override
    public void call(Subscriber<? super R> subscriber) {
        MapSubscriber<T, R> parent = new MapSubscriber<>(subscriber, func);
        source.make(parent);
    }

    static final class MapSubscriber<T, R> implements Subscriber<T> {
        final Subscriber<? super R> actual;

        final Func<? super T, ? extends R> mapper;

        public MapSubscriber(Subscriber<? super R> actual, Func<? super T, ? extends R> mapper) {
            this.actual = actual;
            this.mapper = mapper;
        }

        @Override
        public void onNext(T t) {
            R result = mapper.call(t);
            actual.onNext(result);
        }

        @Override
        public void onError(Throwable e) {
            actual.onError(e);
        }
    }
}
