package com.imyeego.promise;

import android.util.Log;

import java.util.concurrent.atomic.AtomicBoolean;

public class UIOnSubscribe<T> implements Promise.OnSubscribe<T> {

    final Promise<T> source;
    final Action<T> then;
    AtomicBoolean done = new AtomicBoolean(false);
    UISubscriber<T> parent;

    public UIOnSubscribe(Promise<T> source, Action<T> then) {
        this.source = source;
        this.then = then;
    }

    @Override
    public void call(Subscriber<? super T> subscriber) {
        parent = new UISubscriber<>(subscriber, then);
        source.make(parent);
    }

    @Override
    public void cancel(Subscriber<? super T> subscriber) {
        source.cancel(parent);
    }

    final class UISubscriber<T> implements Subscriber<T> {
        final Subscriber<? super T> actual;
        final Action<T> action;
        final Object mainLock = new Object();


        public UISubscriber(Subscriber<? super T> actual, Action<T> action) {
            this.actual = actual;
            this.action = action;
        }

        @Override
        public void onNext(T t) {
            Utils.getMainHandler().post(new UIRunnable<T>(action, t, mainLock));
            synchronized (mainLock) {
                try {
                    mainLock.wait();
                } catch (InterruptedException e) {
                    onError(e);
                    return;
                }
                if (done.get())
                    actual.onNext(t);

            }
        }

        @Override
        public void onError(Throwable e) {
            actual.onError(e);
        }
    }

    final class UIRunnable<T> implements Runnable {
        private Action<T> action;
        private T t;
        private Object lock;

        public UIRunnable(Action<T> action, T t, Object lock) {
            this.action = action;
            this.t = t;
            this.lock = lock;
        }

        @Override
        public void run() {
            try {
                action.call(t);
                done.getAndSet(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            synchronized (lock) {
                lock.notify();
            }
        }
    }
}
