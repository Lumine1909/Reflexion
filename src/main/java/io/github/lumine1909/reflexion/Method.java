package io.github.lumine1909.reflexion;

import io.github.lumine1909.reflexion.method.LambdaMethodHolder;

import java.lang.Class;
import java.lang.reflect.Modifier;

public record Method<T>(java.lang.reflect.Method javaMethod, boolean isStatic, LambdaMethodHolder<T> holder) {

    public Method(java.lang.reflect.Method javaMethod) {
        this(javaMethod, Modifier.isStatic(javaMethod.getModifiers()), new LambdaMethodHolder<>(javaMethod));
    }

    public static <T> Method<T> of(Class<?> clazz, String name, Class<T> returnType, Class<?>... parameterTypes) {
        return io.github.lumine1909.reflexion.Class.of(clazz).getMethod(name, returnType, parameterTypes).orElseThrow();
    }

    public T invoke(Object instance, Object... args) {
        if (isStatic) {
            return holder.invokeStatic(args);
        } else {
            return holder.invokeVirtual(instance, args);
        }
    }
}
