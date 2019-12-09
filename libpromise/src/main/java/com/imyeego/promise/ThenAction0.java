package com.imyeego.promise;

public class ThenAction0 extends AbstractAction0 {

    public ThenAction0(IAction0 target, Action0 action) {
        super(target, action);
    }

    @Override
    public void execute() {
        if (target != null) {
            target.execute();
        }
        action.call();
    }
}
