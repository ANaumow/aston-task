package ru.company.app.common.util.exception;

public class BusinessLogicException extends RuntimeException {

    private final Object[] args;

    public BusinessLogicException(String message, Object... args) {
        this(null, message, args);
    }

    public BusinessLogicException(Throwable cause, String message, Object... args) {
        super(message, cause);
        this.args = args;
    }

    public Object[] getArgs() {
        return args;
    }
}
