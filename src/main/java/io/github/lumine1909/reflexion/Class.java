package io.github.lumine1909.reflexion;

import io.github.lumine1909.reflexion.exception.NotFoundException;
import io.github.lumine1909.reflexion.exception.OperationException;
import io.github.lumine1909.reflexion.internal.MethodHolder;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import static io.github.lumine1909.reflexion.internal.UnsafeUtil.IMPL_LOOKUP;
import static io.github.lumine1909.reflexion.internal.UnsafeUtil.UNSAFE;

/**
 * A high-level wrapper around {@link java.lang.Class} that provides
 * fast, unsafe, and flexible reflective access using
 * {@link MethodHandle}, {@link VarHandle}, and {@code Unsafe}.
 *
 * <p>This abstraction prioritizes performance and capability over safety:
 * <ul>
 *   <li>Bypasses access checks via {@code IMPL_LOOKUP}</li>
 *   <li>Allocates instances without invoking constructors</li>
 *   <li>Uses method handles instead of {@link java.lang.reflect.Method}</li>
 * </ul>
 *
 * <p>All failures are wrapped into {@link OperationException} unless explicitly
 * documented otherwise.</p>
 *
 * @param <T> the represented Java type
 */
@SuppressWarnings({"unchecked", "DataFlowIssue"})
public record Class<T>(java.lang.Class<T> javaClass) {

    /**
     * Loads a class by its fully-qualified name.
     *
     * @param name the class name
     * @param <T>  inferred class type
     * @return a {@link Class} wrapper for the loaded class
     * @throws NotFoundException if the class does not exist
     */
    public static <T> Class<T> forName(String name) {
        return forName(name, false);
    }

    /**
     * Loads a class by name, returning {@code null} if loading fails.
     *
     * @param name     the class name
     * @param <T>      inferred class type
     * @param nullable return null instead of throw {@link NotFoundException} if the class does not exist
     * @return a {@link Class} wrapper
     */
    public static <T> Class<T> forName(String name, boolean nullable) {
        try {
            return new Class<>((java.lang.Class<T>) java.lang.Class.forName(name));
        } catch (Throwable t) {
            if (nullable) return null;
            throw new NotFoundException("Can not find class " + name);
        }
    }

    /**
     * Loads a class with explicit initialization and class loader control.
     *
     * @param name       the class name
     * @param initialize whether the class should be initialized
     * @param loader     the class loader to use
     * @param <T>        inferred class type
     * @return a {@link Class} wrapper
     * @throws NotFoundException if the class does not exist
     */
    public static <T> Class<T> forName(String name, boolean initialize, ClassLoader loader) {
        try {
            java.lang.Class<T> clazz = (java.lang.Class<T>) java.lang.Class.forName(name, initialize, loader);
            return new Class<>(clazz);
        } catch (ClassNotFoundException e) {
            throw new NotFoundException("Can not find class " + name);
        }
    }

    /**
     * Wraps an existing {@link java.lang.Class}.
     *
     * @param clazz the class to wrap
     * @param <T>   class type
     * @return a new {@link Class} wrapper
     */
    public static <T> Class<T> of(java.lang.Class<T> clazz) {
        return new Class<>(clazz);
    }

    private static MethodHandle createSpreader(MethodHandle methodHandle, boolean isStatic) {
        return isStatic
            ? methodHandle.asSpreader(Object[].class, methodHandle.type().parameterCount())
            : methodHandle.asSpreader(Object[].class, methodHandle.type().parameterCount() - 1);
    }

    /**
     * Looks up a declared field by name.
     *
     * @param name field name
     * @return a {@link Field} wrapper
     * @throws NotFoundException if the field does not exist
     */
    public <S> Field<S> getField(String name) {
        return getField(name, false);
    }

    /**
     * Internal field lookup implementation.
     *
     * <p>Access checks are bypassed using {@code IMPL_LOOKUP}.</p>
     *
     * @param name     field name
     * @param nullable if returns null instead of throws exceptions
     * @return a {@link Field} wrapper
     */
    public <S> Field<S> getField(String name, boolean nullable) {
        try {
            java.lang.reflect.Field field = javaClass.getDeclaredField(name);
            return new Field<>(field);
        } catch (NoSuchFieldException e) {
            if (nullable) return null;
            throw new NotFoundException("Can not find field \"" + name + "\" in " + javaClass);
        } catch (Throwable t) {
            if (nullable) return null;
            throw new OperationException(t);
        }
    }

    /**
     * Looks up a method with wrapper-based parameter and return types.
     *
     * @param name           method name
     * @param returnType     expected return type
     * @param parameterTypes parameter types
     * @param <S>            return type
     * @return a {@link Method} wrapper
     */
    public <S> Method<S> getMethod(String name, Class<S> returnType, Class<?>... parameterTypes) {
        return getMethod(name, false, returnType, parameterTypes);
    }

    /**
     * Looks up a method with wrapper-based parameter and return types.
     *
     * @param name           method name
     * @param returnType     expected return type
     * @param parameterTypes parameter types
     * @param <S>            return type
     * @return a {@link Method} wrapper
     */
    public <S> Method<S> getMethod(String name, java.lang.Class<S> returnType, java.lang.Class<?>... parameterTypes) {
        return getMethod(name, false, returnType, parameterTypes);
    }

    /**
     * Looks up a method with wrapper-based parameter and return types.
     *
     * @param name           method name
     * @param nullable       if returns null instead of throws exceptions
     * @param returnType     expected return type
     * @param parameterTypes parameter types
     * @param <S>            return type
     * @return a {@link Method} wrapper
     */
    public <S> Method<S> getMethod(String name, boolean nullable, Class<S> returnType, Class<?>... parameterTypes) {
        return getMethod(name, nullable, returnType.javaClass, Arrays.stream(parameterTypes).map(c -> c.javaClass).toArray(java.lang.Class[]::new));
    }

    /**
     * Looks up a method using {@link MethodHandle} resolution.
     *
     * <p>Resolution order:
     * <ol>
     *   <li>{@code getDeclaredMethod}</li>
     *   <li>{@code findVirtual}</li>
     *   <li>{@code findStatic}</li>
     * </ol>
     *
     * @param name           method name
     * @param returnType     expected return type
     * @param parameterTypes parameter types
     * @param <S>            return type
     * @return a {@link Method} wrapper
     * @throws NotFoundException if no matching method is found
     */
    public <S> Method<S> getMethod(String name, boolean nullable, java.lang.Class<S> returnType, java.lang.Class<?>... parameterTypes) {
        try {
            java.lang.reflect.Method method = javaClass.getDeclaredMethod(name, parameterTypes);
            MethodHandle methodHandle = IMPL_LOOKUP.unreflect(method).asType(IMPL_LOOKUP.unreflect(method).type().generic());
            if (!returnType.isAssignableFrom(method.getReturnType())) {
                throw new OperationException(null);
            }
            return new Method<>(method, parameterTypes.length, Modifier.isStatic(method.getModifiers()), methodHandle, createSpreader(methodHandle, Modifier.isStatic(method.getModifiers())), MethodHolder.createSupplier(methodHandle));
        } catch (Throwable ignored) {
        }

        try {
            MethodHandle methodHandle = IMPL_LOOKUP
                .findVirtual(javaClass, name, MethodType.methodType(returnType, parameterTypes))
                .asType(MethodType.methodType(Object.class, Object[].class).generic());
            return new Method<>(null, parameterTypes.length, false, methodHandle, createSpreader(methodHandle, false), MethodHolder.createSupplier(methodHandle));
        } catch (Throwable ignored) {
        }

        try {
            MethodHandle methodHandle = IMPL_LOOKUP
                .findStatic(javaClass, name, MethodType.methodType(returnType, parameterTypes))
                .asType(MethodType.methodType(Object.class, Object[].class).generic());
            return new Method<>(null, parameterTypes.length, true, methodHandle, createSpreader(methodHandle, true), MethodHolder.createSupplier(methodHandle));
        } catch (Throwable ignored) {
        }

        if (nullable) return null;
        throw new NotFoundException("Can not find method \"" + name + "\" in " + javaClass + " with return type " + returnType + " and parameter types " + Arrays.toString(parameterTypes));
    }

    /**
     * Allocates an instance of this class without invoking any constructor.
     *
     * <p>This uses {@link sun.misc.Unsafe#allocateInstance(java.lang.Class)} and therefore:
     * <ul>
     *   <li>Does not run constructors</li>
     *   <li>Does not initialize fields</li>
     *   <li>May violate class invariants</li>
     * </ul>
     *
     * @return a new uninitialized instance
     * @throws OperationException if allocation fails
     */
    public T newInstance() {
        try {
            return (T) UNSAFE.allocateInstance(javaClass);
        } catch (Throwable t) {
            throw new OperationException(t);
        }
    }
}