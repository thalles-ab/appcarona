package br.uvv.carona.exception;

public class BaseException extends Exception {
    protected String message;

    public BaseException() {
    }

    public BaseException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
