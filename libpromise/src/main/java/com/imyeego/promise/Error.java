package com.imyeego.promise;

public class Error implements IError {

    private Err err;

    public Error(Err err) {
        this.err = err;
    }

    @Override
    public void error(Throwable t) {
        err.call(t);
    }
}
