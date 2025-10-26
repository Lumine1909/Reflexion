package io.github.lumine1909.reflexion.field;

import java.lang.reflect.Field;

import static io.github.lumine1909.reflexion.UnsafeUtil.UNSAFE;

final class ByteField extends UnsafeFieldHolder<Byte> {

    public ByteField(Field javaField) {
        super(javaField);
    }

    @Override
    protected Byte getStatic() {
        return UNSAFE.getByte(staticBase, staticOffset);
    }

    @Override
    protected void setStatic(Byte value) {
        UNSAFE.putByte(staticBase, staticOffset, value);
    }

    @Override
    protected Byte getInstance(Object object) {
        return UNSAFE.getByte(object, objectOffset);
    }

    @Override
    protected void setInstance(Object object, Byte value) {
        UNSAFE.putByte(object, objectOffset, value);
    }
}
