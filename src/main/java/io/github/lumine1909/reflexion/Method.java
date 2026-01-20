package io.github.lumine1909.reflexion;

import io.github.lumine1909.reflexion.exception.OperationException;

import java.lang.Class;
import java.lang.invoke.MethodHandle;
import java.util.function.Supplier;

/**
 * A high-performance wrapper around {@link java.lang.reflect.Method}
 * backed by {@link MethodHandle}.
 *
 * <p>This abstraction provides:
 * <ul>
 *   <li>Fast invocation via inlined {@link MethodHandle#invokeExact}</li>
 *   <li>Fallback to uninlined {@link MethodHandle#invokeExact}</li>
 *   <li>Automatic handling of static vs virtual methods</li>
 *   <li>Varargs spreading for high-arity methods</li>
 * </ul>
 *
 * <p>All reflective failures are wrapped in {@link OperationException}.</p>
 *
 * @param <T> the method return type
 */
public record Method<T>(java.lang.reflect.Method javaMethod, int parameterCount, boolean isStatic,
                        MethodHandle methodHandle, MethodHandle spreader, Supplier<MethodHandle> supplier) {

    /**
     * Looks up a method by name, return type, and parameter types
     * from the given class.
     *
     * @param clazz          the declaring class
     * @param name           the method name
     * @param returnType     the expected return type
     * @param parameterTypes the parameter types
     * @param <T>            return type
     * @return a {@link Method} wrapper
     */
    public static <T> Method<T> of(Class<?> clazz, String name, Class<T> returnType, Class<?>... parameterTypes) {
        return io.github.lumine1909.reflexion.Class.of(clazz).getMethod(name, returnType, parameterTypes);
    }

    /**
     * Looks up a method by name, return type, and parameter types
     * from the given class name.
     *
     * @param className      fully-qualified class name
     * @param name           the method name
     * @param returnType     the expected return type
     * @param parameterTypes the parameter types
     * @param <T>            return type
     * @return a {@link Method} wrapper
     */
    public static <T> Method<T> of(String className, String name, Class<T> returnType, Class<?>... parameterTypes) {
        return io.github.lumine1909.reflexion.Class.forName(className).getMethod(name, returnType, parameterTypes);
    }

    /**
     * Invokes the underlying method.
     *
     * <p>For instance methods, {@code instance} must be non-null.
     * For static methods, {@code instance} is ignored and may be {@code null}.</p>
     *
     * @param instance the target instance, or {@code null} for static methods
     * @param args     method arguments
     * @return the invocation result
     * @throws OperationException if invocation fails
     */
    public T invoke(Object instance, Object... args) {
        try {
            if (supplier == null) {
                return isStatic ? invokeStatic(methodHandle, instance, args) : invokeVirtual(methodHandle, instance, args);
            } else {
                return isStatic ? invokeStatic(supplier.get(), instance, args) : invokeVirtual(supplier.get(), instance, args);
            }
        } catch (Throwable t) {
            throw new OperationException(t);
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