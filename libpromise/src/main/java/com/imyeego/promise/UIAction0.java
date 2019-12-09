package com.imyeego.promise;

public class UIAction0 extends AbstractAction0 {
    public UIAction0(IAction0 target, Action0 action) {
        super(target, action);
    }

    @Override
    public void execute() {
        if (target != null) {
            target.execute();
        }
        Utils.getMainHandler().post(new UIRunnable(action));
    }

    private static class UIRunnable implements Runnable {
        private Action0 action;

        public UIRunnable(Action0 action) {
            this.action = action;
        }

        @Override
        public void run() {
            action.call();
        }
    }
}
