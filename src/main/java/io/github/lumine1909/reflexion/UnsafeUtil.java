package io.github.lumine1909.reflexion;

import sun.misc.Unsafe;

import java.lang.Class;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;

@SuppressWarnings("deprecation")
public class UnsafeUtil {

    public static final Unsafe UNSAFE;
    public static final MethodHandles.Lookup IMPL_LOOKUP;
    public static final Object INTERNAL_UNSAFE;

    private static final Class<?> class$InternalUnsafe;
    private static final MethodHandle method$objectFieldOffset;
    private static final MethodHandle method$staticFieldBase;
    private static final MethodHandle method$staticFieldOffset;

    static {
        try {
            Field field$theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            field$theUnsafe.setAccessible(true);
            UNSAFE = (Unsafe) field$theUnsafe.get(null);
            Field field$implLookup = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
            IMPL_LOOKUP = (MethodHandles.Lookup) UNSAFE.getObject(MethodHandles.Lookup.class, UNSAFE.staticFieldOffset(field$implLookup));
            class$InternalUnsafe = Class.forName("jdk.internal.misc.Unsafe");
            Field field$internalUnsafe = class$InternalUnsafe.getDeclaredField("theUnsafe");
            INTERNAL_UNSAFE = UNSAFE.getObject(UNSAFE.staticFieldBase(field$internalUnsafe), UNSAFE.staticFieldOffset(field$internalUnsafe));
            method$objectFieldOffset = IMPL_LOOKUP.findVirtual(class$InternalUnsafe, "objectFieldOffset", MethodType.methodType(long.class, Field.class));
            method$staticFieldBase = IMPL_LOOKUP.findVirtual(class$InternalUnsafe, "staticFieldBase", MethodType.methodType(Object.class, Field.class));
            method$staticFieldOffset = IMPL_LOOKUP.findVirtual(class$InternalUnsafe, "staticFieldOffset", MethodType.methodType(long.class, Field.class));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static long objectFieldOffset(Field field) {
        try {
            return UNSAFE.objectFieldOffset(field);
        } catch (UnsupportedOperationException ignored) {
        }
        try {
            return (long) method$objectFieldOffset.invoke(INTERNAL_UNSAFE, field);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public static Object staticFieldBase(Field field) {
        try {
            return UNSAFE.staticFieldBase(field);
        } catch (UnsupportedOperationException ignored) {
        }
        try {
            return method$staticFieldBase.invoke(INTERNAL_UNSAFE, field);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public static long staticFieldOffset(Field field) {
        try {
            return UNSAFE.staticFieldOffset(field);
        } catch (UnsupportedOperationException ignored) {
        }
        try {
            return (long) method$staticFieldOffset.invoke(INTERNAL_UNSAFE, field);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}