package io.github.lumine1909.reflexion.field;

import io.github.lumine1909.reflexion.internal.UnsafeUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public abstract class UnsafeFieldHolder<T> {

    protected final long objectOffset;
    protected final Object staticBase;
    protected final long staticOffset;

    public UnsafeFieldHolder(Field javaField) {
        this.objectOffset = Modifier.isStatic(javaField.getModifiers()) ? -1 : UnsafeUtil.objectFieldOffset(javaField);
        this.staticBase = Modifier.isStatic(javaField.getModifiers()) ? UnsafeUtil.staticFieldBase(javaField) : null;
        this.staticOffset = Modifier.isStatic(javaField.getModifiers()) ? UnsafeUtil.staticFieldOffset(javaField) : -1;
    }

    public static UnsafeFieldHolder<?> createHolder(Field field) {
        Class<?> type = field.getType();
        if (type == byte.class) {
            return new ByteField(field);
        } else if (type == boolean.class) {
            return new BooleanField(field);
        } else if (type == int.class) {
            return new IntField(field);
        } else if (type == short.class) {
            return new ShortField(field);
        } else if (type == long.class) {
            return new LongField(field);
        } else if (type == double.class) {
            return new DoubleField(field);
        } else if (type == float.class) {
            return new FloatField(field);
        } else {
            return new ObjectField<>(field);
        }
    }

    public T get(Object obj) {
        if (objectOffset == -1) {
            return getStatic();
        }
        if (obj == null) {
            throw new NullPointerException("Instance is null for non-static field");
        }
        return getInstance(obj);
    }

    public void set(Object obj, T value) {
        if (objectOffset == -1) {
            setStatic(value);
            return;
        }
        if (obj == null) {
            throw new NullPointerException("Instance is null for non-static field");
        }
        setInstance(obj, value);
    }

    protected abstract T getStatic();

    protected abstract void setStatic(T value);

    protected abstract T getInstance(Object object);

    protected abstract void setInstance(Object object, T value);
}