package io.github.lumine1909.reflexion.field;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static io.github.lumine1909.reflexion.UnsafeUtil.UNSAFE;

public abstract class UnsafeFieldHolder<T> {

    protected final long objectOffset;
    protected final Object staticBase;
    protected final long staticOffset;

    @SuppressWarnings("deprecation")
    public UnsafeFieldHolder(Field javaField) {
        this.objectOffset = Modifier.isStatic(javaField.getModifiers()) ? -1 : UNSAFE.objectFieldOffset(javaField);
        this.staticBase = Modifier.isStatic(javaField.getModifiers()) ? UNSAFE.staticFieldBase(javaField) : null;
        this.staticOffset = Modifier.isStatic(javaField.getModifiers()) ? UNSAFE.staticFieldOffset(javaField) : -1;
    }

    public static UnsafeFieldHolder<?> createHolder(java.lang.reflect.Field field) {
        Class<?> type = field.getType();
        if (type == byte.class) {
            return new ByteField(field);
        } else if (type == boolean.class) {
            return new BooleanField(field);
        } else if (type == int.class) {
            return new IntField(field);
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
        if (objectOffset == -1 && obj == null) {
            throw new NullPointerException("Instance is null for non-static field");
        }
        return objectOffset == -1 ? getStatic() : getInstance(obj);
    }

    public void set(Object obj, T value) {
        if (objectOffset == -1) {
            if (obj == null) {
                throw new NullPointerException("Instance is null for non-static field");
            }
            setStatic(value);
            return;
        }
        setInstance(obj, value);
    }

    protected abstract T getStatic();

    protected abstract void setStatic(T value);

    protected abstract T getInstance(Object object);

    protected abstract void setInstance(Object object, T value);
}
