package com.imyeego.promise;

import java.util.concurrent.*;

public class OfOnSubscribe<T> implements Promise.OnSubscribe<T> {

    final Callable<T> callable;

    public OfOnSubscribe(Callable<T> callable) {
        this.callable = callable;
    }

    @Override
    public void call(Subscriber<? super T> subscriber) {
        Future<T> future = Utils.executorService().submit(callable);
        Utils.executorService().execute(new SubscriberFutureRunnable<T>(subscriber, future));
    }

    @Override
    public void cancel(Subscriber<? super T> subscriber) {

    }

    static class SubscriberFutureRunnable<T> implements Runnable {
        final Subscriber<? super T> subscriber;
        final Future<T> future;

        public SubscriberFutureRunnable(Subscriber<? super T> subscriber, Future<T> future) {
            this.subscriber = subscriber;
            this.future = future;
        }

        @Override
        public void run() {
            try {
                T t = future.get();
                subscriber.onNext(t);
            } catch (InterruptedException | ExecutionException e) {
                subscriber.onError(e);
            }
        }
    }

}
