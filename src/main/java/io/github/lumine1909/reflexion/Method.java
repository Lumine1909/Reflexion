package io.github.lumine1909.reflexion;

import java.lang.Class;
import java.lang.invoke.MethodHandle;
import java.util.function.Supplier;

public record Method<T>(java.lang.reflect.Method javaMethod, int parameterCount, boolean isStatic,
                        MethodHandle methodHandle, Supplier<java.lang.reflect.Method> supplier) {

    public static <T> Method<T> of(Class<?> clazz, String name, Class<T> returnType, Class<?>... parameterTypes) {
        return io.github.lumine1909.reflexion.Class.of(clazz).getMethod(name, returnType, parameterTypes).orElseThrow();
    }

    public static <T> Method<T> of(String className, String name, Class<T> returnType, Class<?>... parameterTypes) {
        return io.github.lumine1909.reflexion.Class.forName(className).orElseThrow().getMethod(name, returnType, parameterTypes).orElseThrow();
    }

    public T invoke(Object instance, Object... args) {
        return isStatic ? invokeStatic(args) : invokeVirtual(instance, args);
    }

    @SuppressWarnings("unchecked")
    public T invokeFast(Object instance, Object... args) {
        try {
            return supplier != null ? (T) supplier.get().invoke(instance, args) : invoke(instance, args);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    @SuppressWarnings("unchecked")
    private T invokeStatic(Object... args) {
        try {
            return (T) switch (parameterCount) {
                case 0 -> methodHandle.invoke();
                case 1 -> methodHandle.invoke(args[0]);
                case 2 -> methodHandle.invoke(args[0], args[1]);
                case 3 -> methodHandle.invoke(args[0], args[1], args[2]);
                default -> methodHandle.invoke(args);
            };
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    @SuppressWarnings("unchecked")
    private T invokeVirtual(Object instance, Object... args) {
        try {
            return (T) switch (parameterCount) {
                case 0 -> methodHandle.invoke(instance);
                case 1 -> methodHandle.invoke(instance, args[0]);
                case 2 -> methodHandle.invoke(instance, args[0], args[1]);
                case 3 -> methodHandle.invoke(instance, args[0], args[1], args[2]);
                default -> methodHandle.invoke(instance, args);
            };
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}