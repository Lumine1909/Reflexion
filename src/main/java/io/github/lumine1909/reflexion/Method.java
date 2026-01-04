package io.github.lumine1909.reflexion;

import java.lang.Class;
import java.lang.invoke.MethodHandle;
import java.util.function.Supplier;

public record Method<T>(java.lang.reflect.Method javaMethod, int parameterCount, boolean isStatic,
                        MethodHandle methodHandle, MethodHandle spreader, Supplier<MethodHandle> supplier) {

    public static <T> Method<T> of(Class<?> clazz, String name, Class<T> returnType, Class<?>... parameterTypes) {
        return io.github.lumine1909.reflexion.Class.of(clazz).getMethod(name, returnType, parameterTypes).orElseThrow();
    }

    public static <T> Method<T> of(String className, String name, Class<T> returnType, Class<?>... parameterTypes) {
        return io.github.lumine1909.reflexion.Class.forName(className).orElseThrow().getMethod(name, returnType, parameterTypes).orElseThrow();
    }

    public T invoke(Object instance, Object... args) {
        try {
            if (supplier == null) {
                return isStatic ? invokeStatic(methodHandle, instance, args) : invokeVirtual(methodHandle, instance, args);
            } else {
                return isStatic ? invokeStatic(supplier.get(), instance, args) : invokeVirtual(supplier.get(), instance, args);
            }
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    @SuppressWarnings("unchecked")
    private T invokeStatic(MethodHandle methodHandle, Object instance, Object... args) throws Throwable {
        return (T) switch (parameterCount) {
            case 0 -> methodHandle.invokeExact();
            case 1 -> methodHandle.invokeExact(args[0]);
            case 2 -> methodHandle.invokeExact(args[0], args[1]);
            case 3 -> methodHandle.invokeExact(args[0], args[1], args[2]);
            case 4 -> methodHandle.invokeExact(args[0], args[1], args[2], args[3]);
            case 5 -> methodHandle.invokeExact(args[0], args[1], args[2], args[3], args[4]);
            default -> spreader.invokeExact(args);
        };
    }

    @SuppressWarnings("unchecked")
    private T invokeVirtual(MethodHandle methodHandle, Object instance, Object... args) throws Throwable {
        return (T) switch (parameterCount) {
            case 0 -> methodHandle.invokeExact(instance);
            case 1 -> methodHandle.invokeExact(instance, args[0]);
            case 2 -> methodHandle.invokeExact(instance, args[0], args[1]);
            case 3 -> methodHandle.invokeExact(instance, args[0], args[1], args[2]);
            case 4 -> methodHandle.invokeExact(instance, args[0], args[1], args[2], args[3]);
            case 5 -> methodHandle.invokeExact(instance, args[0], args[1], args[2], args[3], args[4]);
            default -> spreader.invokeExact(instance, args);
        };
    }
}