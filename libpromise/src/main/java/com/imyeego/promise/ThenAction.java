package com.imyeego.promise;

public class ThenAction<T> extends AbstractAction<T> {

    public ThenAction(IAction<T> target, Action<T> action) {
        super(target, action);
    }

    @Override
    public void execute(T t) {
        if (target != null) {
            target.execute(t);
        }
        action.call(t);
    }
}
