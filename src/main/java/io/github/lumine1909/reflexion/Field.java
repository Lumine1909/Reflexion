package io.github.lumine1909.reflexion;

import io.github.lumine1909.reflexion.field.UnsafeFieldHolder;
import io.github.lumine1909.reflexion.internal.VarHolder;

import java.lang.Class;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Modifier;
import java.util.function.Supplier;

import static io.github.lumine1909.reflexion.field.UnsafeFieldHolder.createHolder;

@SuppressWarnings("unchecked")
public record Field<T>(java.lang.reflect.Field javaField, UnsafeFieldHolder<T> holder,
                       boolean isStatic, Supplier<VarHandle> supplier) {

    public Field(java.lang.reflect.Field javaField) {
        this(javaField, (UnsafeFieldHolder<T>) createHolder(javaField), Modifier.isStatic(javaField.getModifiers()), VarHolder.createSupplier(javaField));
    }

    public static <T> Field<T> of(Class<?> clazz, String name, Class<T> type) {
        return io.github.lumine1909.reflexion.Class.of(clazz).getField(name, type).orElseThrow();
    }

    public static <T> Field<T> of(String className, String name, Class<T> type) {
        return io.github.lumine1909.reflexion.Class.forName(className).orElseThrow().getField(name, type).orElseThrow();
    }

    public static <T> Field<T> of(Class<?> clazz, String name) {
        return (Field<T>) io.github.lumine1909.reflexion.Class.of(clazz).getField(name).orElseThrow();
    }

    public static <T> Field<T> of(String className, String name) {
        return (Field<T>) io.github.lumine1909.reflexion.Class.forName(className).orElseThrow().getField(name).orElseThrow();
    }

    public boolean isStatic() {
        return Modifier.isStatic(javaField.getModifiers());
    }

    public T get(Object instance) {
        return holder.get(instance);
    }

    public T getFast(Object instance) {
        try {
            if (supplier != null) {
                return instance == null ? (T) supplier.get().get() : (T) supplier.get().get(instance);
            } else {
                return get(instance);
            }
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public <S> S getUnsafe(Object instance) {
        return (S) holder.get(instance);
    }

    public <S> S getUnsafeFast(Object instance) {
        try {
            if (supplier != null) {
                return instance == null ? (S) supplier.get().get() : (S) supplier.get().get(instance);
            } else {
                return getUnsafe(instance);
            }
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public void set(Object instance, T value) {
        holder.set(instance, value);
    }

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
            throw new RuntimeException(t);
        }
    }
}