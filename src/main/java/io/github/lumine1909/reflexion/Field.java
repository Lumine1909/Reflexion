package io.github.lumine1909.reflexion;

import io.github.lumine1909.reflexion.field.UnsafeFieldHolder;

import java.lang.reflect.Modifier;

import static io.github.lumine1909.reflexion.field.UnsafeFieldHolder.createHolder;

@SuppressWarnings("unchecked")
public record Field<T>(java.lang.reflect.Field javaField, UnsafeFieldHolder<T> holder) {

    public Field(java.lang.reflect.Field javaField) {
        this(javaField, (UnsafeFieldHolder<T>) createHolder(javaField));
    }

    public boolean isStatic() {
        return Modifier.isStatic(javaField.getModifiers());
    }

    public T get(Object instance) {
        return holder.get(instance);
    }

    public void set(Object instance, T value) {
        holder.set(instance, value);
    }
}
