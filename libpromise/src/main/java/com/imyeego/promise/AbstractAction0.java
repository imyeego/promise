package com.imyeego.promise;

public abstract class AbstractAction0 implements IAction0 {
    protected IAction0 target;
    protected Action0 action;

    public AbstractAction0(IAction0 target, Action0 action) {
        this.target = target;
        this.action = action;
    }
}
