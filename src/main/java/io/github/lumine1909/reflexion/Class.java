package io.github.lumine1909.reflexion;

import io.github.lumine1909.reflexion.exception.NotFoundException;
import io.github.lumine1909.reflexion.exception.OperationException;
import io.github.lumine1909.reflexion.internal.MethodHolder;
import io.github.lumine1909.reflexion.internal.UnsafeField;
import io.github.lumine1909.reflexion.internal.VarHolder;

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
public final class Class<T> {

    public static final int NULLABLE = 1;
    public static final int EXACT = 2;

    private final java.lang.Class<T> javaClass;

    public Class(java.lang.Class<T> javaClass) {
        this.javaClass = javaClass;
    }

    /**
     * Loads a class by its fully-qualified name.
     *
     * @param name class name
     * @param <T>  inferred class type
     * @return a {@link Class} wrapper for the loaded class
     * @throws NotFoundException if the class does not exist
     */
    public static <T> Class<T> forName(String name) {
        return forName(name, 0);
    }

    /**
     * Loads a class by name, returning {@code null} if loading fails.
     *
     * @param name class name
     * @param <T>  inferred class type
     * @param flag class flag
     * @return a {@link Class} wrapper
     */
    public static <T> Class<T> forName(String name, int flag) {
        try {
            return new Class<>((java.lang.Class<T>) java.lang.Class.forName(name));
        } catch (Throwable t) {
            if ((flag & NULLABLE) == NULLABLE) return null;
            throw new NotFoundException("Can not find class " + name);
        }
    }

    /**
     * Loads a class with explicit initialization and class loader control.
     *
     * @param name       class name
     * @param initialize whether the class should be initialized
     * @param loader     class loader to use
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
     * @param clazz class to wrap
     * @param <T>   class type
     * @return a new {@link Class} wrapper
     */
    public static <T> Class<T> of(java.lang.Class<T> clazz) {
        return new Class<>(clazz);
    }

    private static MethodHandle spreader(MethodHandle methodHandle, boolean noInstance) {
        return noInstance
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
        return getField(name, 0);
    }

    /**
     * Internal field lookup implementation.
     *
     * <p>Access checks are bypassed using {@code IMPL_LOOKUP}.</p>
     *
     * @param name field name
     * @param flag field flag
     * @return a {@link Field} wrapper
     */
    public <S> Field<S> getField(String name, int flag) {
        try {
            java.lang.reflect.Field field = javaClass.getDeclaredField(name);
            VarHandle vh = IMPL_LOOKUP.unreflectVarHandle(field);
            boolean isStatic = Modifier.isStatic(field.getModifiers());
            return new Field<>(field, isStatic ? 1 : 0, new UnsafeField(field), vh, VarHolder.inline(vh));
        } catch (Throwable ignored) {
        }
        try {
            // Internal fields
            return new Field<>(null, -1, new UnsafeField(javaClass, name), null, null);
        } catch (Throwable ignored) {
        }

        if ((flag & NULLABLE) == NULLABLE) return null;
        throw new NotFoundException("Can not find field \"" + name + "\" in " + javaClass);
    }

    /**
     * Looks up a method with wrapper-based parameter and return types.
     *
     * @param name       method name
     * @param returnType expected return type
     * @param paramTypes parameter types
     * @param <S>        return type
     * @return a {@link Method} wrapper
     */
    public <S> Method<S> getMethod(String name, Class<S> returnType, Class<?>... paramTypes) {
        return getMethod(name, 0, returnType, paramTypes);
    }

    /**
     * Looks up a method with wrapper-based parameter and return types.
     *
     * @param name       method name
     * @param returnType expected return type
     * @param paramTypes parameter types
     * @param <S>        return type
     * @return a {@link Method} wrapper
     */
    public <S> Method<S> getMethod(String name, java.lang.Class<S> returnType, java.lang.Class<?>... paramTypes) {
        return getMethod(name, 0, returnType, paramTypes);
    }

    /**
     * Looks up a method with wrapper-based parameter and return types.
     *
     * @param name       method name
     * @param flag       method flag
     * @param returnType expected return type
     * @param paramTypes parameter types
     * @param <S>        return type
     * @return a {@link Method} wrapper
     */
    public <S> Method<S> getMethod(String name, int flag, Class<S> returnType, Class<?>... paramTypes) {
        return getMethod(name, flag, returnType.javaClass, Arrays.stream(paramTypes).map(c -> c.javaClass).toArray(java.lang.Class[]::new));
    }

    /**
     * Looks up a method using {@link MethodHandle} resolution.
     *
     * <p>Resolution order:
     * <ol>
     *   <li>{@code findSpecial} if marked in flag</li>
     *   <li>{@code getDeclaredMethod}</li>
     *   <li>{@code findVirtual}</li>
     *   <li>{@code findStatic}</li>
     *   <li>{@code findConstructor} if the method is named "&ltinit&gt"</li>
     * </ol>
     *
     * @param name       method name
     * @param flag       method flag
     * @param returnType expected return type
     * @param paramTypes parameter types
     * @param <S>        return type
     * @return a {@link Method} wrapper
     * @throws NotFoundException if no matching method is found
     */
    public <S> Method<S> getMethod(String name, int flag, java.lang.Class<S> returnType, java.lang.Class<?>... paramTypes) {
        if ((flag & EXACT) == EXACT) {
            try {
                MethodHandle handle = IMPL_LOOKUP
                    .findSpecial(javaClass, name, MethodType.methodType(returnType, paramTypes), javaClass)
                    .asType(MethodType.genericMethodType(paramTypes.length + 1));
                return new Method<>(null, paramTypes.length, 0, handle, spreader(handle, false), MethodHolder.inline(handle));
            } catch (Throwable ignored) {
            }
        }
        try {
            MethodHandle handle = IMPL_LOOKUP
                .findVirtual(javaClass, name, MethodType.methodType(returnType, paramTypes))
                .asType(MethodType.genericMethodType(paramTypes.length + 1));
            return new Method<>(null, paramTypes.length, 0, handle, spreader(handle, false), MethodHolder.inline(handle));
        } catch (Throwable ignored) {
        }
        try {
            MethodHandle handle = IMPL_LOOKUP
                .findStatic(javaClass, name, MethodType.methodType(returnType, paramTypes))
                .asType(MethodType.genericMethodType(paramTypes.length));
            return new Method<>(null, paramTypes.length, 3, handle, spreader(handle, true), MethodHolder.inline(handle));
        } catch (Throwable ignored) {
        }
        if (name.equals("<init>")) {
            try {
                MethodHandle handle = IMPL_LOOKUP
                    .findConstructor(javaClass, MethodType.methodType(void.class, paramTypes))
                    .asType(MethodType.genericMethodType(paramTypes.length));
                return new Method<>(null, paramTypes.length, 2, handle, spreader(handle, true), MethodHolder.inline(handle));
            } catch (Throwable ignored) {
            }
        }

        if ((flag & NULLABLE) == NULLABLE) return null;
        throw new NotFoundException("Can not find method \"" + name + "\" in " + javaClass + " with return type " + returnType + " and parameter types " + Arrays.toString(paramTypes));
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

    /**
     * Returns wrapped Java {@link java.lang.Class} object.
     *
     * @return wrapped Java class
     */
    public java.lang.Class<T> javaClass() {
        return javaClass;
    }
}