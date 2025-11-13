package io.github.lumine1909.reflexion.field;

import java.lang.reflect.Field;

import static io.github.lumine1909.reflexion.UnsafeUtil.UNSAFE;

@SuppressWarnings("unchecked")
final class ObjectField<T> extends UnsafeFieldHolder<T> {

    public ObjectField(Field javaField) {
        super(javaField);
    }

    @Override
    protected T getStatic() {
        return (T) UNSAFE.getObject(staticBase, staticOffset);
    }

    @Override
    protected void setStatic(T value) {
        UNSAFE.putObject(staticBase, staticOffset, value);
    }

    @Override
    protected T getInstance(Object object) {
        return (T) UNSAFE.getObject(object, objectOffset);
    }

    @Override
    protected void setInstance(Object object, T value) {
        UNSAFE.putObject(object, objectOffset, value);
    }
}
