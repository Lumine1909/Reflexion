package io.github.lumine1909.reflexion;

import sun.misc.Unsafe;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;

public class UnsafeUtil {

    public static final Unsafe UNSAFE;
    public static final MethodHandles.Lookup IMPL_LOOKUP;

    static {
        try {
            Field field$theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            field$theUnsafe.setAccessible(true);
            UNSAFE = (Unsafe) field$theUnsafe.get(null);
            Field field$IMPL_LOOKUP = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
            IMPL_LOOKUP = (MethodHandles.Lookup) UNSAFE.getObject(MethodHandles.Lookup.class, UNSAFE.staticFieldOffset(field$IMPL_LOOKUP));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}