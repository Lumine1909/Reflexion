package io.github.lumine1909.reflexion;

import io.github.lumine1909.reflexion.method.LambdaMethodHolder;

public record Method<T>(java.lang.reflect.Method javaMethod, LambdaMethodHolder<T> holder) {

    public Method(java.lang.reflect.Method javaMethod) {
        this(javaMethod, new LambdaMethodHolder<>(javaMethod));
    }

    public T invoke(Object instance, Object... args) {
        return holder.invoke(instance, args);
    }
}
