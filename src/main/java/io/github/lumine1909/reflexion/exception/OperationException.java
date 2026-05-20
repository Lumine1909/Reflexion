package io.github.lumine1909.reflexion.exception;

public final class OperationException extends RuntimeException {

    public OperationException(Throwable cause) {
        super(cause);
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

    @Override
    public StackTraceElement[] getStackTrace() {
        return getCause() == null ? null : getCause().getStackTrace();
    }
}
