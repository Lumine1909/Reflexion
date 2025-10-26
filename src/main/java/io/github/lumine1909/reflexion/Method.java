package io.github.lumine1909.reflexion;

import java.lang.Class;
import java.lang.reflect.Modifier;

public record Method<T>(java.lang.reflect.Method javaMethod, boolean isStatic) {

    public Method(java.lang.reflect.Method javaMethod) {
        this(javaMethod, Modifier.isStatic(javaMethod.getModifiers()));
        javaMethod.setAccessible(true);
    }

    public static <T> Method<T> of(Class<?> clazz, String name, Class<T> returnType, Class<?>... parameterTypes) {
        return io.github.lumine1909.reflexion.Class.of(clazz).getMethod(name, returnType, parameterTypes).orElseThrow();
    }

    @SuppressWarnings("unchecked")
    public T invoke(Object instance, Object... args) {
        try {
            return (T) javaMethod.invoke(instance, args);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}
