package com.imyeego.promise;

public class UIOnSubscribe<T> implements Promise.OnSubscribe<T> {

    final Promise<T> source;
    final Action<T> then;

    public UIOnSubscribe(Promise<T> source, Action<T> then) {
        this.source = source;
        this.then = then;
    }

    @Override
    public void call(Subscriber<? super T> subscriber) {
        UISubscriber<T> parent = new UISubscriber<>(subscriber, then);
        source.make(parent);
    }

    static final class UISubscriber<T> implements Subscriber<T> {
        final Subscriber<? super T> actual;
        final Action<T> action;

        public UISubscriber(Subscriber<? super T> actual, Action<T> action) {
            this.actual = actual;
            this.action = action;
        }

        @Override
        public void onNext(T t) {
            Utils.getMainHandler().post(new UIRunnable<T>(action, t));
            actual.onNext(t);
        }

        @Override
        public void onError(Throwable e) {
            actual.onError(e);
        }
    }

    static final class UIRunnable<T> implements Runnable {
        private Action<T> action;
        private T t;

        public UIRunnable(Action<T> action, T t) {
            this.action = action;
            this.t = t;
        }

        @Override
        public void run() {
            action.call(t);
        }
    }
}
