package io.github.lumine1909.reflexion.field;

import java.lang.reflect.Field;

import static io.github.lumine1909.reflexion.internal.UnsafeUtil.UNSAFE;

final class BooleanField extends UnsafeFieldHolder<Boolean> {

    public BooleanField(Field javaField) {
        super(javaField);
    }

    @Override
    protected Boolean getStatic() {
        return UNSAFE.getBoolean(staticBase, staticOffset);
    }

    @Override
    protected void setStatic(Boolean value) {
        UNSAFE.putBoolean(staticBase, staticOffset, value);
    }

    @Override
    protected Boolean getInstance(Object object) {
        return UNSAFE.getBoolean(object, objectOffset);
    }

    @Override
    protected void setInstance(Object object, Boolean value) {
        UNSAFE.putBoolean(object, objectOffset, value);
    }
}
