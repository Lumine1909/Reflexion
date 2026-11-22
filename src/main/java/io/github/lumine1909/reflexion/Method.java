package io.github.lumine1909.reflexion;

import io.github.lumine1909.reflexion.exception.OperationException;
import io.github.lumine1909.reflexion.internal.MethodUtil;

import java.lang.Class;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.function.Supplier;

/**
 * A high-performance wrapper around {@link java.lang.reflect.Method} or {@link java.lang.reflect.Constructor} backed by {@link MethodHandle}.
 *
 * <p>This abstraction provides:
 * <ul>
 *   <li>Fast invocation via inlined {@link MethodHandle#invokeExact}</li>
 *   <li>Fallback to uninlined {@link MethodHandle#invokeExact}</li>
 *   <li>Automatic handling of static, virtual, special methods and constructor</li>
 *   <li>Varargs spreading for high-arity methods</li>
 * </ul>
 *
 * <p>All reflective failures are wrapped in {@link OperationException}.</p>
 *
 * @param <T> the method return type
 */
public final class Method<T> {

    public static final int STATIC = 1;
    public static final int NO_INSTANCE = 2;

    private final java.lang.reflect.Method javaMethod;
    private final int paramCount;
    private final int flag;
    private final MethodHandle handle;
    private final Supplier<MethodHandle> invoker;

    Method(java.lang.reflect.Method javaMethod, int paramCount, int flag, MethodHandle handle) {
        this.javaMethod = javaMethod;
        this.paramCount = paramCount;
        this.flag = flag;
        this.handle = handle;
        this.invoker = MethodUtil.inline(convert(handle, paramCount, (flag & NO_INSTANCE) == NO_INSTANCE));
    }

    /**
     * Looks up a method by name, return type, and parameter types
     * from the given class.
     *
     * @param clazz      declaring class
     * @param name       method name
     * @param returnType expected return type
     * @param paramTypes parameter types
     * @param <T>        return type
     * @return a {@link Method} wrapper
     * @throws io.github.lumine1909.reflexion.exception.NotFoundException if field not found
     */
    public static <T> Method<T> of(Class<?> clazz, String name, Class<T> returnType, Class<?>... paramTypes) {
        return io.github.lumine1909.reflexion.Class.of(clazz).getMethod(name, returnType, paramTypes);
    }

    /**
     * Looks up a method by name, return type, and parameter types from given class.
     *
     * @param clazz      declaring class
     * @param name       method name
     * @param flag       method flag
     * @param returnType expected return type
     * @param paramTypes parameter types
     * @param <T>        return type
     * @return a {@link Method} wrapper
     */
    public static <T> Method<T> of(Class<?> clazz, String name, int flag, Class<T> returnType, Class<?>... paramTypes) {
        return io.github.lumine1909.reflexion.Class.of(clazz).getMethod(name, flag, returnType, paramTypes);
    }

    /**
     * Looks up a method by name, return type, and parameter types from given class name.
     *
     * @param className  fully-qualified class name
     * @param name       method name
     * @param returnType expected return type
     * @param paramTypes parameter types
     * @param <T>        return type
     * @return a {@link Method} wrapper
     */
    public static <T> Method<T> of(String className, String name, Class<T> returnType, Class<?>... paramTypes) {
        return io.github.lumine1909.reflexion.Class.forName(className).getMethod(name, returnType, paramTypes);
    }

    /**
     * Looks up a method by name, return type, and parameter types from the given class name.
     *
     * @param className  fully-qualified class name
     * @param name       method name
     * @param flag       method flag
     * @param returnType expected return type
     * @param paramTypes parameter types
     * @param <T>        return type
     * @return a {@link Method} wrapper or {@code null} if not found
     */
    public static <T> Method<T> of(String className, String name, int flag, Class<T> returnType, Class<?>... paramTypes) {
        io.github.lumine1909.reflexion.Class<?> clazz = io.github.lumine1909.reflexion.Class.forName(className, flag);
        return clazz == null ? null : clazz.getMethod(name, flag, returnType, paramTypes);
    }

    private static MethodHandle convert(MethodHandle handle, int paramCount, boolean noInstance) {
        if (noInstance) {
            handle = MethodHandles.dropArguments(handle, 0, Object.class);
        }
        if (paramCount > 4) {
            handle = handle.asSpreader(Object[].class, handle.type().parameterCount() - 1);
        }
        return handle;
    }

    /**
     * Invokes the underlying method.
     *
     * <p>For instance methods, {@code instance} must be non-null.
     * For static methods, {@code instance} is ignored and may be {@code null}.</p>
     *
     * @param instance target instance, or {@code null} for static methods
     * @param args     method arguments
     * @return invocation result
     * @throws OperationException if invocation fails
     */
    @SuppressWarnings("unchecked")
    public T invoke(Object instance, Object... args) {
        try {
            return (T) invoker.get().invokeExact(instance, args);
        } catch (Throwable t) {
            throw new OperationException(t);
        }
    }

    @SuppressWarnings("unchecked")
    public T invoke(Object instance) {
        try {
            return (T) invoker.get().invokeExact(instance);
        } catch (Throwable t) {
            throw new OperationException(t);
        }
    }

    @SuppressWarnings("unchecked")
    public T invoke(Object instance, Object arg0) {
        try {
            return (T) invoker.get().invokeExact(instance, arg0);
        } catch (Throwable t) {
            throw new OperationException(t);
        }
    }

    @SuppressWarnings("unchecked")
    public T invoke(Object instance, Object arg0, Object arg1) {
        try {
            return (T) invoker.get().invokeExact(instance, arg0, arg1);
        } catch (Throwable t) {
            throw new OperationException(t);
        }
    }

    @SuppressWarnings("unchecked")
    public T invoke(Object instance, Object arg0, Object arg1, Object arg2) {
        try {
            return (T) invoker.get().invokeExact(instance, arg0, arg1, arg2);
        } catch (Throwable t) {
            throw new OperationException(t);
        }
    }

    @SuppressWarnings("unchecked")
    public T invoke(Object instance, Object arg0, Object arg1, Object arg2, Object arg3) {
        try {
            return (T) invoker.get().invokeExact(instance, arg0, arg1, arg2, arg3);
        } catch (Throwable t) {
            throw new OperationException(t);
        }
    }

    /**
     * Returns wrapped Java {@link java.lang.reflect.Method} object.
     *
     * @return wrapped Java method, nullable
     */
    public java.lang.reflect.Method javaMethod() {
        return javaMethod;
    }

    /**
     * Returns wrapped Java {@link MethodHandle} object.
     *
     * @return wrapped method handle
     */
    public MethodHandle handle() {
        return handle;
    }

    /**
     * Returns number of parameters declared by this method.
     *
     * @return method parameter count
     */
    public int paramCount() {
        return paramCount;
    }

    /**
     * Returns whether this method is static.
     *
     * @return {@code true} if this method is static
     */
    public boolean isStatic() {
        return (flag & STATIC) == STATIC;
    }
}