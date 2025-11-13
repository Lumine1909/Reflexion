package io.github.lumine1909.reflexion.field;

import java.lang.reflect.Field;

import static io.github.lumine1909.reflexion.UnsafeUtil.UNSAFE;

final class LongField extends UnsafeFieldHolder<Long> {

    public LongField(Field javaField) {
        super(javaField);
    }

    @Override
    protected Long getStatic() {
        return UNSAFE.getLong(staticBase, staticOffset);
    }

    @Override
    protected void setStatic(Long value) {
        UNSAFE.putLong(staticBase, staticOffset, value);
    }

    @Override
    protected Long getInstance(Object object) {
        return UNSAFE.getLong(object, objectOffset);
    }

    @Override
    protected void setInstance(Object object, Long value) {
        UNSAFE.putLong(object, objectOffset, value);
    }
}
