package io.github.lumine1909.reflexion;

import io.github.lumine1909.reflexion.exception.OperationException;
import io.github.lumine1909.reflexion.field.UnsafeFieldHolder;
import io.github.lumine1909.reflexion.internal.VarHolder;

import java.lang.Class;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Modifier;
import java.util.function.Supplier;

import static io.github.lumine1909.reflexion.field.UnsafeFieldHolder.createHolder;

/**
 * A high-performance wrapper around {@link java.lang.reflect.Field} that
 * provides unsafe and varhandle-based access.
 *
 * <p>This abstraction supports:
 * <ul>
 *   <li>Bypassing Java access checks</li>
 *   <li>Fast field access via inlined {@link VarHandle}</li>
 *   <li>Fallback to {@code Unsafe}-based access</li>
 * </ul>
 *
 * <p>Static fields may be accessed with {@code instance == null}.</p>
 *
 * @param <T> the field type
 */
@SuppressWarnings("unchecked")
public record Field<T>(java.lang.reflect.Field javaField, UnsafeFieldHolder<T> holder, boolean isStatic, Supplier<VarHandle> supplier) {

    /**
     * Creates a {@link Field} wrapper from a reflective {@link java.lang.reflect.Field}
     *
     * @param javaField the reflective field
     */
    public Field(java.lang.reflect.Field javaField) {
        this(javaField, (UnsafeFieldHolder<T>) createHolder(javaField), Modifier.isStatic(javaField.getModifiers()), VarHolder.createSupplier(javaField));
    }

    /**
     * Looks up a field by name and type from the given class.
     *
     * @param clazz the declaring class
     * @param name the field name
     * @param type the expected field type
     * @param <T> field type
     * @return a {@link Field} wrapper
     */
    public static <T> Field<T> of(Class<?> clazz, String name, Class<T> type) {
        return io.github.lumine1909.reflexion.Class.of(clazz).getField(name, type);
    }

    /**
     * Looks up a field by name and type from the given class name.
     *
     * @param className fully-qualified class name
     * @param name field name
     * @param type expected field type
     * @param <T> field type
     * @return a {@link Field} wrapper
     */
    public static <T> Field<T> of(String className, String name, Class<T> type) {
        return io.github.lumine1909.reflexion.Class.forName(className).getField(name, type);
    }

    /**
     * Looks up a field by name from the given class.
     *
     * @param clazz the declaring class
     * @param name the field name
     * @param <T> inferred field type
     * @return a {@link Field} wrapper
     */
    public static <T> Field<T> of(Class<?> clazz, String name) {
        return io.github.lumine1909.reflexion.Class.of(clazz).getField(name);
    }

    /**
     * Looks up a field by name from the given class name.
     *
     * @param className fully-qualified class name
     * @param name the field name
     * @param <T> inferred field type
     * @return a {@link Field} wrapper
     */
    public static <T> Field<T> of(String className, String name) {
        return io.github.lumine1909.reflexion.Class.forName(className).getField(name);
    }

    /**
     * Returns whether this field is static.
     *
     * @return {@code true} if the field is static
     */
    public boolean isStatic() {
        return isStatic;
    }

    /**
     * Reads the field value.
     *
     * @param instance the target instance, or {@code null} for static fields
     * @return the field value
     */
    public T get(Object instance) {
        return holder.get(instance);
    }

    /**
     * Reads the field value using an inlined {@link VarHandle} when available.
     *
     * <p>This method is typically faster than {@link #get(Object)}.</p>
     *
     * @param instance the target instance, or {@code null} for static fields
     * @return the field value
     * @throws OperationException if access fails
     */
    public T getFast(Object instance) {
        try {
            if (supplier != null) {
                return instance == null ? (T) supplier.get().get() : (T) supplier.get().get(instance);
            } else {
                return get(instance);
            }
        } catch (Throwable t) {
            throw new OperationException(t);
        }
    }

    /**
     * Reads the field value without static typing.
     *
     * @param instance the target instance, or {@code null} for static fields
     * @param <S> expected return type
     * @return the field value
     */
    public <S> S getUntyped(Object instance) {
        return (S) holder.get(instance);
    }

    /**
     * Reads the field value without static typing using an inlined {@link VarHandle}.
     *
     * @param instance the target instance, or {@code null} for static fields
     * @param <S> expected return type
     * @return the field value
     * @throws OperationException if access fails
     */
    public <S> S getUntypedFast(Object instance) {
        try {
            if (supplier != null) {
                return instance == null ? (S) supplier.get().get() : (S) supplier.get().get(instance);
            } else {
                return getUntyped(instance);
            }
        } catch (Throwable t) {
            throw new OperationException(t);
        }
    }

    /**
     * Writes a value to the field.
     *
     * @param instance the target instance, or {@code null} for static fields
     * @param value the new field value
     */
    public void set(Object instance, T value) {
        holder.set(instance, value);
    }

    /**
     * Writes a value to the field using an inlined {@link VarHandle} when available.
     *
     * <p>This method is typically faster than {@link #set(Object, Object)}.</p>
     *
     * @param instance the target instance, or {@code null} for static fields
     * @param value the new field value
     * @throws OperationException if access fails
     */
    public void setFast(Object instance, T value) {
        try {
            if (supplier != null) {
                if (instance == null) {
                    supplier.get().set(value);
                } else {
                    supplier.get().set(instance, value);
                }
            } else {
                set(instance, value);
            }
        } catch (Throwable t) {
            throw new OperationException(t);
        }
    }
}
