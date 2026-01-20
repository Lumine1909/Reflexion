package io.github.lumine1909.reflexion.field;

import java.lang.reflect.Field;

import static io.github.lumine1909.reflexion.internal.UnsafeUtil.UNSAFE;

@SuppressWarnings("DataFlowIssue")
final class DoubleField extends UnsafeFieldHolder<Double> {

    public DoubleField(Field javaField) {
        super(javaField);
    }

    @Override
    protected Double getStatic() {
        return UNSAFE.getDouble(staticBase, staticOffset);
    }

    @Override
    protected void setStatic(Double value) {
        UNSAFE.putDouble(staticBase, staticOffset, value);
    }

    @Override
    protected Double getInstance(Object object) {
        return UNSAFE.getDouble(object, objectOffset);
    }

    @Override
    protected void setInstance(Object object, Double value) {
        UNSAFE.putDouble(object, objectOffset, value);
    }
}
