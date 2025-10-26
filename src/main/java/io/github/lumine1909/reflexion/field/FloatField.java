package io.github.lumine1909.reflexion.field;

import static io.github.lumine1909.reflexion.UnsafeUtil.UNSAFE;

import java.lang.reflect.Field;

final class FloatField extends UnsafeFieldHolder<Float> {

    public FloatField(Field javaField) {
        super(javaField);
    }

    @Override
    protected Float getStatic() {
        return UNSAFE.getFloat(staticBase, staticOffset);
    }

    @Override
    protected void setStatic(Float value) {
        UNSAFE.putFloat(staticBase, staticOffset, value);
    }

    @Override
    protected Float getInstance(Object object) {
        return UNSAFE.getFloat(object, objectOffset);
    }

    @Override
    protected void setInstance(Object object, Float value) {
        UNSAFE.putFloat(object, objectOffset, value);
    }
}
