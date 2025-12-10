package io.github.lumine1909.reflexion.field;

import java.lang.reflect.Field;

import static io.github.lumine1909.reflexion.UnsafeUtil.UNSAFE;

public class ShortField extends UnsafeFieldHolder<Short> {

    public ShortField(Field javaField) {
        super(javaField);
    }

    @Override
    protected Short getStatic() {
        return UNSAFE.getShort(staticBase, staticOffset);
    }

    @Override
    protected void setStatic(Short value) {
        UNSAFE.putShort(staticBase, staticOffset, value);
    }

    @Override
    protected Short getInstance(Object object) {
        return UNSAFE.getShort(object, objectOffset);
    }

    @Override
    protected void setInstance(Object object, Short value) {
        UNSAFE.putShort(object, objectOffset, value);
    }
}