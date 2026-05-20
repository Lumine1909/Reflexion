package io.github.lumine1909.reflexion;

import io.github.lumine1909.reflexion.exception.OperationException;
import io.github.lumine1909.reflexion.internal.UnsafeField;

import java.lang.Class;
import java.lang.invoke.VarHandle;
import java.util.function.Supplier;

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
public final class Field<T> {

    private final java.lang.reflect.Field javaField;
    private final UnsafeField unsafe;
    private final int staticFlag;
    private final VarHandle handle;
    private final Supplier<VarHandle> inline;

    public Field(java.lang.reflect.Field javaField, int staticFlag, UnsafeField unsafe, VarHandle handle, Supplier<VarHandle> inline) {
        this.javaField = javaField;
        this.staticFlag = staticFlag;
        this.unsafe = unsafe;
        this.handle = handle;
        this.inline = inline;
    }

    /**
     * Looks up a field by name from the given class.
     *
     * @param clazz declaring class
     * @param name  field name
     * @return a {@link Field} wrapper
     * @throws io.github.lumine1909.reflexion.exception.NotFoundException if not found
     */
    public static <T> Field<T> of(Class<?> clazz, String name) {
        return io.github.lumine1909.reflexion.Class.of(clazz).getField(name);
    }

    /**
     * Looks up a field by name from the given class.
     *
     * @param clazz declaring class
     * @param name  field name
     * @param <T>   inferred field type
     * @param flag  field flag
     * @return a {@link Field} wrapper or {@code null} if not found
     */
    public static <T> Field<T> of(Class<?> clazz, String name, int flag) {
        return io.github.lumine1909.reflexion.Class.of(clazz).getField(name, flag);
    }

    /**
     * Looks up a field by name from the given class name.
     *
     * @param className fully-qualified class name
     * @param name      field name
     * @return a {@link Field} wrapper
     * @throws io.github.lumine1909.reflexion.exception.NotFoundException if field not found
     */
    public static <T> Field<T> of(String className, String name) {
        return io.github.lumine1909.reflexion.Class.forName(className).getField(name);
    }

    /**
     * Looks up a field by name from the given class name.
     *
     * @param className fully-qualified class name
     * @param name      field name
     * @param flag      field flag
     * @return a {@link Field} wrapper or {@code null} if not found
     */
    public static <T> Field<T> of(String className, String name, int flag) {
        io.github.lumine1909.reflexion.Class<?> clazz = io.github.lumine1909.reflexion.Class.forName(className, flag);
        return clazz == null ? null : clazz.getField(name, flag);
    }

    /**
     * Returns wrapped Java {@link java.lang.reflect.Field} object.
     *
     * @return wrapped Java field, nullable
     */
    public java.lang.reflect.Field javaField() {
        return javaField;
    }

    /**
     * Returns wrapped Java {@link VarHandle} object.
     *
     * @return wrapped Java field, nullable
     */
    public VarHandle handle() {
        return handle;
    }

    /**
     * Returns whether this field is static.
     *
     * @return {@code true} if the field is static
     */
    public boolean isStatic() {
        return staticFlag == 1;
    }

    /**
     * Reads the field value.
     *
     * @param instance the target instance, or {@code null} for static fields
     * @return the field value
     */
    public T get(Object instance) {
        return (T) unsafe.get(instance);
    }

    /**
     * Reads the field value using an inlined {@link VarHandle} when available.
     *
     * <p>This method is typically faster than {@link #get(Object)}.</p>
     *
     * @param instance target instance, or {@code null} for static fields
     * @return field value
     * @throws OperationException if access fails
     */
    public T getFast(Object instance) {
        try {
            if (inline != null) {
                VarHandle vh = inline.get();
                return instance == null ? (T) vh.get() : (T) vh.get(instance);
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
     * @param instance target instance, or {@code null} for static fields
     * @param <S>      expected return type
     * @return field value
     */
    public <S> S getUntyped(Object instance) {
        return (S) unsafe.get(instance);
    }

    /**
     * Reads field value without static typing using an inlined {@link VarHandle}.
     *
     * @param instance target instance, or {@code null} for static fields
     * @param <S>      expected return type
     * @return field value
     * @throws OperationException if access fails
     */
    public <S> S getUntypedFast(Object instance) {
        try {
            if (inline != null) {
                VarHandle vh = inline.get();
                return instance == null ? (S) vh.get() : (S) vh.get(instance);
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
     * @param instance target instance, or {@code null} for static fields
     * @param value    new field value
     */
    public void set(Object instance, T value) {
        unsafe.set(instance, value);
    }

    /**
     * Writes a value to the field using an inlined {@link VarHandle} when available.
     *
     * <p>This method is typically faster than {@link #set(Object, Object)}.</p>
     *
     * @param instance target instance, or {@code null} for static fields
     * @param value    new field value
     * @throws OperationException if access fails
     */
    public void setFast(Object instance, T value) {
        try {
            if (inline != null) {
                VarHandle vh = inline.get();
                if (instance == null) {
                    vh.set(value);
                } else {
                    vh.set(instance, value);
                }
            } else {
                set(instance, value);
            }
        } catch (Throwable t) {
            throw new OperationException(t);
        }
    }

    /**
     * Reads field object value.
     *
     * @param instance target instance, or {@code null} for static fields
     * @return field object value
     */
    public Object getObject(Object instance) {
        return unsafe.getObject(instance);
    }

    /**
     * Writes an object value to the field.
     *
     * @param instance target instance, or {@code null} for static fields
     * @param value    new field object value
     */
    public void setObject(Object instance, Object value) {
        unsafe.setObject(instance, value);
    }

    /**
     * Reads the field byte value.
     *
     * @param instance target instance, or {@code null} for static fields
     * @return field byte value
     */
    public byte getByte(Object instance) {
        return unsafe.getByte(instance);
    }

    /**
     * Writes a byte value to the field.
     *
     * @param instance target instance, or {@code null} for static fields
     * @param value    new field byte value
     */
    public void setByte(Object instance, byte value) {
        unsafe.setByte(instance, value);
    }

    /**
     * Reads  field short value.
     *
     * @param instance target instance, or {@code null} for static fields
     * @return field short value
     */
    public short getShort(Object instance) {
        return unsafe.getShort(instance);
    }

    /**
     * Writes a short value to the field.
     *
     * @param instance target instance, or {@code null} for static fields
     * @param value    new field short value
     */
    public void setShort(Object instance, short value) {
        unsafe.setShort(instance, value);
    }

    /**
     * Reads the field int value.
     *
     * @param instance target instance, or {@code null} for static fields
     * @return field int value
     */
    public int getInt(Object instance) {
        return unsafe.getInt(instance);
    }

    /**
     * Writes a int value to the field.
     *
     * @param instance target instance, or {@code null} for static fields
     * @param value    new field int value
     */
    public void setInt(Object instance, int value) {
        unsafe.setInt(instance, value);
    }

    /**
     * Reads the field long value.
     *
     * @param instance target instance, or {@code null} for static fields
     * @return field long value
     */
    public long getLong(Object instance) {
        return unsafe.getLong(instance);
    }

    /**
     * Writes a long value to the field.
     *
     * @param instance target instance, or {@code null} for static fields
     * @param value    new field long value
     */
    public void setLong(Object instance, long value) {
        unsafe.setLong(instance, value);
    }

    /**
     * Reads the field float value.
     *
     * @param instance target instance, or {@code null} for static fields
     * @return field float value
     */
    public float getFloat(Object instance) {
        return unsafe.getFloat(instance);
    }

    /**
     * Writes a float value to the field.
     *
     * @param instance target instance, or {@code null} for static fields
     * @param value    new field float value
     */
    public void setFloat(Object instance, float value) {
        unsafe.setFloat(instance, value);
    }

    /**
     * Reads the field double value.
     *
     * @param instance target instance, or {@code null} for static fields
     * @return field double value
     */
    public double getDouble(Object instance) {
        return unsafe.getDouble(instance);
    }

    /**
     * Writes a double value to the field.
     *
     * @param instance target instance, or {@code null} for static fields
     * @param value    new field double value
     */
    public void setDouble(Object instance, double value) {
        unsafe.setDouble(instance, value);
    }

    /**
     * Reads the field char value.
     *
     * @param instance target instance, or {@code null} for static fields
     * @return field char value
     */
    public char getChar(Object instance) {
        return unsafe.getChar(instance);
    }

    /**
     * Writes a char value to the field.
     *
     * @param instance target instance, or {@code null} for static fields
     * @param value    new field char value
     */
    public void setChar(Object instance, char value) {
        unsafe.setChar(instance, value);
    }

    /**
     * Reads the field boolean value.
     *
     * @param instance target instance, or {@code null} for static fields
     * @return field boolean value
     */
    public boolean getBoolean(Object instance) {
        return unsafe.getBoolean(instance);
    }

    /**
     * Writes a boolean value to the field.
     *
     * @param instance target instance, or {@code null} for static fields
     * @param value    new field boolean value
     */
    public void setBoolean(Object instance, boolean value) {
        unsafe.setBoolean(instance, value);
    }
}
