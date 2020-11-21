package com.imyeego.promise;

import android.util.Log;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ForOnSubscribe<T> implements Promise.OnSubscribe<T> {

    final Fun1<T> fun1;
    final long period;
    ScheduledFuture<?> future;

    public ForOnSubscribe(Fun1<T> fun1, long period) {
        this.fun1 = fun1;
        this.period = period;
    }

    @Override
    public void call(Subscriber<? super T> subscriber) {
        Log.i("Promise", "foreach is start");
        future = Utils.scheduledService().scheduleAtFixedRate(new SubscriberFutureRunnable<T>(subscriber, fun1), 0, period, TimeUnit.MILLISECONDS);
    }

    @Override
    public void cancel(Subscriber<? super T> subscriber) {
        if (future != null) {
            Log.i("Promise", "foreach is stop");
            future.cancel(false);
        }
    }

    static class SubscriberFutureRunnable<T> implements Runnable {
        final Subscriber<? super T> subscriber;
        final Fun1<T> fun1;

        public SubscriberFutureRunnable(Subscriber<? super T> subscriber, Fun1<T> fun1) {
            this.subscriber = subscriber;
            this.fun1 = fun1;
        }

        @Override
        public void run() {
            try {
                T t = fun1.call();
                subscriber.onNext(t);
            } catch (ExecutionException e) {
                subscriber.onError(e);
            }
        }
    }

}
