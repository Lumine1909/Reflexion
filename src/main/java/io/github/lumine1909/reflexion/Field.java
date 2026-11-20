package io.github.lumine1909.reflexion;

import io.github.lumine1909.reflexion.internal.UnsafeUtil;

import java.lang.Class;
import java.lang.reflect.Modifier;

import static io.github.lumine1909.reflexion.internal.UnsafeUtil.UNSAFE;

/**
 * A high-performance wrapper around {@link java.lang.reflect.Field} that provides unsafe and varhandle-based access.
 *
 * <p>This abstraction supports:
 * <ul>
 *   <li>Bypassing Java access checks</li>
 *   <li>Fallback to {@code Unsafe}-based access</li>
 * </ul>
 *
 * <p>Static fields may be accessed with {@code instance == null}.</p>
 *
 * @param <T> the field type
 */
@SuppressWarnings({"DataFlowIssue", "deprecation", "removal", "unchecked"})
public final class Field<T> {

    private final java.lang.reflect.Field javaField;
    private final boolean isStatic;
    private final Object staticBase;
    private final long offset;
    private final Type type;

    Field(java.lang.reflect.Field javaField) {
        this.javaField = javaField;
        this.isStatic = Modifier.isStatic(javaField.getModifiers());
        this.staticBase = isStatic ? UnsafeUtil.staticFieldBase(javaField) : null;
        this.offset = isStatic ? UnsafeUtil.staticFieldOffset(javaField) : UnsafeUtil.objectFieldOffset(javaField);
        this.type = getType(javaField);
    }

    Field(Class<?> clazz, String name) {
        this.javaField = null;
        this.isStatic = false;
        this.staticBase = clazz;
        this.offset = UnsafeUtil.fieldOffset(clazz, name);
        this.type = Type.NULL;
    }

    private static Type getType(java.lang.reflect.Field javaField) {
        Class<?> type = javaField.getType();
        if (type == byte.class) return Type.BYTE;
        if (type == int.class) return Type.INT;
        if (type == short.class) return Type.SHORT;
        if (type == long.class) return Type.LONG;
        if (type == float.class) return Type.FLOAT;
        if (type == double.class) return Type.DOUBLE;
        if (type == char.class) return Type.CHAR;
        if (type == boolean.class) return Type.BOOLEAN;
        return Type.OBJECT;
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
        return (T) get0(base(instance), offset);
    }

    private Object base(Object object) {
        return (isStatic || object == null) ? staticBase : object;
    }

    /**
     * Reads the field value without static typing.
     *
     * @param instance target instance, or {@code null} for static fields
     * @param <S>      expected return type
     * @return field value
     */
    public <S> S getUntyped(Object instance) {
        return (S) get0(base(instance), offset);
    }


    /**
     * Writes a value to the field.
     *
     * @param instance target instance, or {@code null} for static fields
     * @param value    new field value
     */
    public void set(Object instance, T value) {
        set0(base(instance), offset, value);
    }

    private Object get0(Object base, long offset) {
        switch (type) {
            case OBJECT:
                return UNSAFE.getObject(base, offset);
            case BYTE:
                return UNSAFE.getByte(base, offset);
            case INT:
                return UNSAFE.getInt(base, offset);
            case SHORT:
                return UNSAFE.getShort(base, offset);
            case LONG:
                return UNSAFE.getLong(base, offset);
            case FLOAT:
                return UNSAFE.getFloat(base, offset);
            case DOUBLE:
                return UNSAFE.getDouble(base, offset);
            case CHAR:
                return UNSAFE.getChar(base, offset);
            case BOOLEAN:
                return UNSAFE.getBoolean(base, offset);
            case NULL:
            default:
                throw new UnsupportedOperationException("Unknown type");
        }
    }

    private void set0(Object base, long offset, Object value) {
        switch (type) {
            case OBJECT:
                UNSAFE.putObject(base, offset, value);
                return;
            case BYTE:
                UNSAFE.putByte(base, offset, (byte) value);
                return;
            case INT:
                UNSAFE.putInt(base, offset, (int) value);
                return;
            case SHORT:
                UNSAFE.putShort(base, offset, (short) value);
                return;
            case LONG:
                UNSAFE.putLong(base, offset, (long) value);
                return;
            case FLOAT:
                UNSAFE.putFloat(base, offset, (float) value);
                return;
            case DOUBLE:
                UNSAFE.putDouble(base, offset, (double) value);
                return;
            case CHAR:
                UNSAFE.putChar(base, offset, (char) value);
                return;
            case BOOLEAN:
                UNSAFE.putBoolean(base, offset, (boolean) value);
                return;
            case NULL:
                throw new UnsupportedOperationException("Unknown type");
        }
    }

    /**
     * Reads field object value.
     *
     * @param instance target instance, or {@code null} for static fields
     * @return field object value
     */
    public Object getObject(Object instance) {
        return UNSAFE.getObject(base(instance), offset);
    }

    /**
     * Writes an object value to the field.
     *
     * @param instance target instance, or {@code null} for static fields
     * @param value    new field object value
     */
    public void setObject(Object instance, Object value) {
        UNSAFE.putObject(base(instance), offset, value);
    }

    /**
     * Reads the field byte value.
     *
     * @param instance target instance, or {@code null} for static fields
     * @return field byte value
     */
    public byte getByte(Object instance) {
        return UNSAFE.getByte(base(instance), offset);
    }

    /**
     * Writes a byte value to the field.
     *
     * @param instance target instance, or {@code null} for static fields
     * @param value    new field byte value
     */
    public void setByte(Object instance, byte value) {
        UNSAFE.putByte(base(instance), offset, value);
    }

    /**
     * Reads  field short value.
     *
     * @param instance target instance, or {@code null} for static fields
     * @return field short value
     */
    public short getShort(Object instance) {
        return UNSAFE.getShort(base(instance), offset);
    }

    /**
     * Writes a short value to the field.
     *
     * @param instance target instance, or {@code null} for static fields
     * @param value    new field short value
     */
    public void setShort(Object instance, short value) {
        UNSAFE.putShort(base(instance), offset, value);
    }

    /**
     * Reads the field int value.
     *
     * @param instance target instance, or {@code null} for static fields
     * @return field int value
     */
    public int getInt(Object instance) {
        return UNSAFE.getInt(base(instance), offset);
    }

    /**
     * Writes a int value to the field.
     *
     * @param instance target instance, or {@code null} for static fields
     * @param value    new field int value
     */
    public void setInt(Object instance, int value) {
        UNSAFE.putInt(base(instance), offset, value);
    }

    /**
     * Reads the field long value.
     *
     * @param instance target instance, or {@code null} for static fields
     * @return field long value
     */
    public long getLong(Object instance) {
        return UNSAFE.getLong(base(instance), offset);
    }

    /**
     * Writes a long value to the field.
     *
     * @param instance target instance, or {@code null} for static fields
     * @param value    new field long value
     */
    public void setLong(Object instance, long value) {
        UNSAFE.putLong(base(instance), offset, value);
    }

    /**
     * Reads the field float value.
     *
     * @param instance target instance, or {@code null} for static fields
     * @return field float value
     */
    public float getFloat(Object instance) {
        return UNSAFE.getFloat(base(instance), offset);
    }

    /**
     * Writes a float value to the field.
     *
     * @param instance target instance, or {@code null} for static fields
     * @param value    new field float value
     */
    public void setFloat(Object instance, float value) {
        UNSAFE.putFloat(base(instance), offset, value);
    }

    /**
     * Reads the field double value.
     *
     * @param instance target instance, or {@code null} for static fields
     * @return field double value
     */
    public double getDouble(Object instance) {
        return UNSAFE.getDouble(base(instance), offset);
    }

    /**
     * Writes a double value to the field.
     *
     * @param instance target instance, or {@code null} for static fields
     * @param value    new field double value
     */
    public void setDouble(Object instance, double value) {
        UNSAFE.putDouble(base(instance), offset, value);
    }

    /**
     * Reads the field char value.
     *
     * @param instance target instance, or {@code null} for static fields
     * @return field char value
     */
    public char getChar(Object instance) {
        return UNSAFE.getChar(base(instance), offset);
    }

    /**
     * Writes a char value to the field.
     *
     * @param instance target instance, or {@code null} for static fields
     * @param value    new field char value
     */
    public void setChar(Object instance, char value) {
        UNSAFE.putChar(base(instance), offset, value);
    }

    /**
     * Reads the field boolean value.
     *
     * @param instance target instance, or {@code null} for static fields
     * @return field boolean value
     */
    public boolean getBoolean(Object instance) {
        return UNSAFE.getBoolean(base(instance), offset);
    }

    /**
     * Writes a boolean value to the field.
     *
     * @param instance target instance, or {@code null} for static fields
     * @param value    new field boolean value
     */
    public void setBoolean(Object instance, boolean value) {
        UNSAFE.putBoolean(base(instance), offset, value);
    }

    private enum Type {
        NULL, OBJECT, BYTE, SHORT, INT, LONG, FLOAT, DOUBLE, CHAR, BOOLEAN
    }
}