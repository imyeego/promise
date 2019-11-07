package com.imyeego.promise;

public abstract class AbstractAction<T> implements IAction<T> {
    protected IAction<T> target;
    protected Action<T> action;

    public AbstractAction(IAction<T> target, Action<T> action) {
        this.target = target;
        this.action = action;
    }
}
