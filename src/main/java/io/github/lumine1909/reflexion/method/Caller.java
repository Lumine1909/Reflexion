package io.github.lumine1909.reflexion.method;

public interface Caller<R> {

    R callStatic(Object... args);

    R callVirtual(Object instance, Object... args);
}
