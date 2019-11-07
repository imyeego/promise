package com.imyeego.promise;

public class UIAction<T> extends AbstractAction<T> {

    public UIAction(IAction<T> target, Action<T> action) {
        super(target, action);
    }

    @Override
    public void execute(T t) {
        if (target != null) {
            target.execute(t);
        }
        Utils.getMainHandler().post(new UIRunnable<T>(action, t));
    }

    private static class UIRunnable<T> implements Runnable {
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
