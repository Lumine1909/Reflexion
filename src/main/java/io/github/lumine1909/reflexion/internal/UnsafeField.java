package io.github.lumine1909.reflexion.internal;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static io.github.lumine1909.reflexion.internal.UnsafeUtil.UNSAFE;

@SuppressWarnings({"DataFlowIssue", "deprecation", "removal"})
public final class UnsafeField {

    private final int staticFlag;
    private final Object staticBase;
    private final long offset;
    private final Type type;

    public UnsafeField(Field javaField) {
        boolean isStatic = Modifier.isStatic(javaField.getModifiers());
        this.staticFlag = isStatic ? 1 : 0;
        this.staticBase = isStatic ? UnsafeUtil.staticFieldBase(javaField) : null;
        this.offset = isStatic ? UnsafeUtil.staticFieldOffset(javaField) : UnsafeUtil.objectFieldOffset(javaField);
        this.type = getType(javaField);
    }

    public UnsafeField(Class<?> clazz, String name) {
        this.staticFlag = -1;
        this.staticBase = clazz;
        this.offset = UnsafeUtil.fieldOffset(clazz, name);
        this.type = Type.NULL;
    }

    private static Type getType(Field javaField) {
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

    private Object base(Object object) {
        return (staticFlag == 1 || object == null) ? staticBase : object;
    }

    public Object get(Object object) {
        return get0(base(object), offset);
    }

    public void set(Object object, Object value) {
        set0(base(object), offset, value);
    }

    private Object get0(Object base, long offset) {
        return switch (type) {
            case OBJECT -> UNSAFE.getObject(base, offset);
            case BYTE -> UNSAFE.getByte(base, offset);
            case SHORT -> UNSAFE.getShort(base, offset);
            case INT -> UNSAFE.getInt(base, offset);
            case LONG -> UNSAFE.getLong(base, offset);
            case FLOAT -> UNSAFE.getFloat(base, offset);
            case DOUBLE -> UNSAFE.getDouble(base, offset);
            case CHAR -> UNSAFE.getChar(base, offset);
            case BOOLEAN -> UNSAFE.getBoolean(base, offset);
            case NULL -> throw new UnsupportedOperationException("Unknown type");
        };
    }

    private void set0(Object base, long offset, Object value) {
        switch (type) {
            case OBJECT -> UNSAFE.putObject(base, offset, value);
            case BYTE -> UNSAFE.putByte(base, offset, (byte) value);
            case SHORT -> UNSAFE.putShort(base, offset, (short) value);
            case INT -> UNSAFE.putInt(base, offset, (int) value);
            case LONG -> UNSAFE.putLong(base, offset, (long) value);
            case FLOAT -> UNSAFE.putFloat(base, offset, (float) value);
            case DOUBLE -> UNSAFE.putDouble(base, offset, (double) value);
            case CHAR -> UNSAFE.putChar(base, offset, (char) value);
            case BOOLEAN -> UNSAFE.putBoolean(base, offset, (boolean) value);
            case NULL -> throw new UnsupportedOperationException("Unknown type");
        }
    }

    public Object getObject(Object object) {
        return UNSAFE.getObject(base(object), offset);
    }

    public void setObject(Object object, Object value) {
        UNSAFE.putObject(base(object), offset, value);
    }

    public byte getByte(Object object) {
        return UNSAFE.getByte(base(object), offset);
    }

    public void setByte(Object object, byte value) {
        UNSAFE.putByte(base(object), offset, value);
    }

    public short getShort(Object object) {
        return UNSAFE.getShort(base(object), offset);
    }

    public void setShort(Object object, short value) {
        UNSAFE.putShort(base(object), offset, value);
    }

    public int getInt(Object object) {
        return UNSAFE.getInt(base(object), offset);
    }

    public void setInt(Object object, int value) {
        UNSAFE.putInt(base(object), offset, value);
    }

    public long getLong(Object object) {
        return UNSAFE.getLong(base(object), offset);
    }

    public void setLong(Object object, long value) {
        UNSAFE.putLong(base(object), offset, value);
    }

    public float getFloat(Object object) {
        return UNSAFE.getFloat(base(object), offset);
    }

    public void setFloat(Object object, float value) {
        UNSAFE.putFloat(base(object), offset, value);
    }

    public double getDouble(Object object) {
        return UNSAFE.getDouble(base(object), offset);
    }

    public void setDouble(Object object, double value) {
        UNSAFE.putDouble(base(object), offset, value);
    }

    public char getChar(Object object) {
        return UNSAFE.getChar(base(object), offset);
    }

    public void setChar(Object object, char value) {
        UNSAFE.putChar(base(object), offset, value);
    }

    public boolean getBoolean(Object object) {
        return UNSAFE.getBoolean(base(object), offset);
    }

    public void setBoolean(Object object, boolean value) {
        UNSAFE.putBoolean(base(object), offset, value);
    }

    private enum Type {
        NULL, OBJECT, BYTE, SHORT, INT, LONG, FLOAT, DOUBLE, CHAR, BOOLEAN
    }
}