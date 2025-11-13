package io.github.lumine1909.reflexion;

import io.github.lumine1909.reflexion.method.Caller;
import io.github.lumine1909.reflexion.method.LambdaMethodFactory;

import java.lang.Class;
import java.lang.reflect.Modifier;

public record Method<T>(java.lang.reflect.Method javaMethod, boolean isStatic, Caller<T> caller) {

    public Method(java.lang.reflect.Method javaMethod) {
        this(javaMethod, Modifier.isStatic(javaMethod.getModifiers()), LambdaMethodFactory.createCaller(javaMethod));
    }

    public static <T> Method<T> of(Class<?> clazz, String name, Class<T> returnType, Class<?>... parameterTypes) {
        return io.github.lumine1909.reflexion.Class.of(clazz).getMethod(name, returnType, parameterTypes).orElseThrow();
    }

    public T invoke(Object instance, Object... args) {
        if (isStatic) {
            return caller.callStatic(args);
        } else {
            return caller.callVirtual(instance, args);
        }
    }
}
