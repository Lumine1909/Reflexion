package io.github.lumine1909.reflexion.field;

import java.lang.reflect.Field;

import static io.github.lumine1909.reflexion.internal.UnsafeUtil.UNSAFE;

@SuppressWarnings("DataFlowIssue")
final class IntField extends UnsafeFieldHolder<Integer> {

    public IntField(Field javaField) {
        super(javaField);
    }

    @Override
    protected Integer getStatic() {
        return UNSAFE.getInt(staticBase, staticOffset);
    }

    @Override
    protected void setStatic(Integer value) {
        UNSAFE.putInt(staticBase, staticOffset, value);
    }

    @Override
    protected Integer getInstance(Object object) {
        return UNSAFE.getInt(object, objectOffset);
    }

    @Override
    protected void setInstance(Object object, Integer value) {
        UNSAFE.putInt(object, objectOffset, value);
    }
}
