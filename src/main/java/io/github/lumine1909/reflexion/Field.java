package io.github.lumine1909.reflexion;

import io.github.lumine1909.reflexion.field.UnsafeFieldHolder;

import java.lang.Class;
import java.lang.reflect.Modifier;

import static io.github.lumine1909.reflexion.field.UnsafeFieldHolder.createHolder;

@SuppressWarnings("unchecked")
public record Field<T>(java.lang.reflect.Field javaField, UnsafeFieldHolder<T> holder) {

    public Field(java.lang.reflect.Field javaField) {
        this(javaField, (UnsafeFieldHolder<T>) createHolder(javaField));
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

    public <S> S getUnsafe(Object instance) {
        return (S) holder.get(instance);
    }

    public void set(Object instance, T value) {
        holder.set(instance, value);
    }
}
