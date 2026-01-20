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
     * @throws OperationException if the class cannot be loaded
     */
    public static <T> Class<T> forName(String name) {
        try {
            java.lang.Class<T> clazz = (java.lang.Class<T>) java.lang.Class.forName(name);
            return new Class<>(clazz);
        } catch (Throwable t) {
            throw new OperationException(t);
        }
    }

    /**
     * Loads a class by name, returning {@code null} if loading fails.
     *
     * @param name the class name
     * @param <T>  inferred class type
     * @return a {@link Class} wrapper or {@code null} if not found
     */
    public static <T> Class<T> forNameOrNull(String name) {
        try {
            return new Class<>((java.lang.Class<T>) java.lang.Class.forName(name));
        } catch (Throwable t) {
            return null;
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
     * @throws OperationException if loading fails
     */
    public static <T> Class<T> forName(String name, boolean initialize, ClassLoader loader) {
        try {
            java.lang.Class<T> clazz = (java.lang.Class<T>) java.lang.Class.forName(name, initialize, loader);
            return new Class<>(clazz);
        } catch (Throwable t) {
            throw new OperationException(t);
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
     * @param <S>  field type
     * @return a {@link Field} wrapper
     * @throws NotFoundException if the field does not exist
     */
    @SuppressWarnings("rawtypes")
    public <S> Field<S> getField(String name) {
        return getField(name, (java.lang.Class) null);
    }

    /**
     * Same as {@link #getField(String)} but returns {@code null} on failure.
     *
     * @param name field name
     * @param <S>  field type
     * @return a {@link Field} or {@code null}
     */
    @SuppressWarnings("rawtypes")
    public <S> Field<S> getFieldOrNull(String name) {
        try {
            return getField(name, (java.lang.Class) null);
        } catch (Throwable t) {
            return null;
        }
    }

    /**
     * Looks up a declared field by name and expected type.
     *
     * @param name field name
     * @param type expected field type
     * @param <S>  field type
     * @return a {@link Field} wrapper
     * @throws NotFoundException if the field does not exist or type mismatches
     */
    public <S> Field<S> getField(String name, Class<S> type) {
        return getField(name, type.javaClass);
    }

    /**
     * Internal field lookup implementation.
     *
     * <p>Access checks are bypassed using {@code IMPL_LOOKUP}.</p>
     *
     * @param name field name
     * @param type expected field type (nullable)
     * @param <S>  field type
     * @return a {@link Field} wrapper
     */
    public <S> Field<S> getField(String name, java.lang.Class<S> type) {
        try {
            java.lang.reflect.Field field = javaClass.getDeclaredField(name);
            if (type != null && field.getType() != type) {
                throw new NotFoundException("Can not find field \"" + name + "\" in " + javaClass + " with type " + type);
            }
            return new Field<>(field);
        } catch (NoSuchFieldException e) {
            throw new NotFoundException("Can not find field \"" + name + "\" in " + javaClass + " with type " + type);
        } catch (Throwable t) {
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
        return getMethod(name, returnType.javaClass, Arrays.stream(parameterTypes).map(c -> c.javaClass).toArray(java.lang.Class[]::new));
    }

    /**
     * Same as {@link #getMethod(String, Class, Class[])} but returns {@code null} on failure.
     */
    public <S> Method<S> getMethodOrNull(String name, Class<S> returnType, Class<?>... parameterTypes) {
        try {
            return getMethod(name, returnType.javaClass, Arrays.stream(parameterTypes).map(c -> c.javaClass).toArray(java.lang.Class[]::new));
        } catch (Throwable t) {
            return null;
        }
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
    public <S> Method<S> getMethod(String name, java.lang.Class<S> returnType, java.lang.Class<?>... parameterTypes) {
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