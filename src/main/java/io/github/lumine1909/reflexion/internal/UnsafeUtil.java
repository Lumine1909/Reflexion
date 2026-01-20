package io.github.lumine1909.reflexion.internal;

import io.github.lumine1909.reflexion.exception.OperationException;
import sun.misc.Unsafe;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;
import java.util.Map;

@SuppressWarnings({"deprecation", "DataFlowIssue"})
public class UnsafeUtil {

    public static final Unsafe UNSAFE = null;
    public static final MethodHandles.Lookup IMPL_LOOKUP = null;
    public static final Object INTERNAL_UNSAFE = null;

    private static final java.lang.Class<?> class$InternalUnsafe = null;
    private static final MethodHandle mh$objectFieldOffset = null;
    private static final MethodHandle mh$staticFieldBase = null;
    private static final MethodHandle mh$staticFieldOffset = null;

    static {
        try {
            Field field$unsafe = Unsafe.class.getDeclaredField("theUnsafe");
            field$unsafe.setAccessible(true);
            Unsafe unsafe = (Unsafe) field$unsafe.get(null);

            put(unsafe, null, null, null, null, null, null);
            clearReflectionFilter();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @SuppressWarnings("ConstantConditions")
    public static void put(Unsafe unsafe, MethodHandles.Lookup implLookup, Class<?> class$InternalUnsafe, Object internalUnsafe, MethodHandle objectFieldOffset, MethodHandle staticFieldBase, MethodHandle staticFieldOffset) {
        if (unsafe == null && UNSAFE == null) {
            throw new NullPointerException("Unsafe is null");
        }
        try {
            if (UNSAFE == null) {
                Field f = UnsafeUtil.class.getDeclaredField("UNSAFE");
                unsafe.putObject(unsafe.staticFieldBase(f), unsafe.staticFieldOffset(f), unsafe);
            }
            if (IMPL_LOOKUP == null) {
                if (implLookup == null) {
                    Field field$implLookup = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
                    implLookup = (MethodHandles.Lookup) UNSAFE.getObject(MethodHandles.Lookup.class, UNSAFE.staticFieldOffset(field$implLookup));
                }
                Field f = UnsafeUtil.class.getDeclaredField("IMPL_LOOKUP");
                UNSAFE.putObject(UNSAFE.staticFieldBase(f), UNSAFE.staticFieldOffset(f), implLookup);
            }
            if (UnsafeUtil.class$InternalUnsafe == null) {
                if (class$InternalUnsafe == null) {
                    class$InternalUnsafe = Class.forName("jdk.internal.misc.Unsafe");
                }
                Field f = UnsafeUtil.class.getDeclaredField("class$InternalUnsafe");
                UNSAFE.putObject(UNSAFE.staticFieldBase(f), UNSAFE.staticFieldOffset(f), class$InternalUnsafe);
            }
            if (INTERNAL_UNSAFE == null) {
                if (internalUnsafe == null) {
                    Field field$internalUnsafe = UnsafeUtil.class$InternalUnsafe.getDeclaredField("theUnsafe");
                    internalUnsafe = UNSAFE.getObject(UNSAFE.staticFieldBase(field$internalUnsafe), UNSAFE.staticFieldOffset(field$internalUnsafe));
                }
                Field f = UnsafeUtil.class.getDeclaredField("INTERNAL_UNSAFE");
                UNSAFE.putObject(UNSAFE.staticFieldBase(f), UNSAFE.staticFieldOffset(f), internalUnsafe);
            }
            if (mh$objectFieldOffset == null) {
                objectFieldOffset = IMPL_LOOKUP.findVirtual(class$InternalUnsafe, "objectFieldOffset", MethodType.methodType(long.class, Field.class));
                Field f = UnsafeUtil.class.getDeclaredField("mh$objectFieldOffset");
                UNSAFE.putObject(UNSAFE.staticFieldBase(f), UNSAFE.staticFieldOffset(f), objectFieldOffset);
            }
            if (mh$staticFieldBase == null) {
                staticFieldBase = IMPL_LOOKUP.findVirtual(class$InternalUnsafe, "staticFieldBase", MethodType.methodType(Object.class, Field.class));
                Field f = UnsafeUtil.class.getDeclaredField("mh$staticFieldBase");
                UNSAFE.putObject(UNSAFE.staticFieldBase(f), UNSAFE.staticFieldOffset(f), staticFieldBase);
            }
            if (mh$staticFieldOffset == null) {
                staticFieldOffset = IMPL_LOOKUP.findVirtual(class$InternalUnsafe, "staticFieldOffset", MethodType.methodType(long.class, Field.class));
                Field f = UnsafeUtil.class.getDeclaredField("mh$staticFieldOffset");
                UNSAFE.putObject(UNSAFE.staticFieldBase(f), UNSAFE.staticFieldOffset(f), staticFieldOffset);
            }
        } catch (Throwable t) {
            throw new OperationException(t);
        }
    }

    public static long objectFieldOffset(Field field) {
        try {
            return UNSAFE.objectFieldOffset(field);
        } catch (Throwable ignored) {
        }
        try {
            return (long) mh$objectFieldOffset.invoke(INTERNAL_UNSAFE, field);
        } catch (Throwable t) {
            throw new OperationException(t);
        }
    }

    public static Object staticFieldBase(Field field) {
        try {
            return UNSAFE.staticFieldBase(field);
        } catch (Throwable ignored) {
        }
        try {
            return mh$staticFieldBase.invoke(INTERNAL_UNSAFE, field);
        } catch (Throwable t) {
            throw new OperationException(t);
        }
    }

    public static long staticFieldOffset(Field field) {
        try {
            return UNSAFE.staticFieldOffset(field);
        } catch (Throwable ignored) {
        }
        try {
            return (long) mh$staticFieldOffset.invoke(INTERNAL_UNSAFE, field);
        } catch (Throwable t) {
            throw new OperationException(t);
        }
    }

    public static void clearReflectionFilter() {
        try {
            Class<?> class$Reflection = Class.forName("jdk.internal.reflect.Reflection");
            VarHandle vh$fieldFilterMap = IMPL_LOOKUP.findStaticVarHandle(class$Reflection, "fieldFilterMap", Map.class);
            vh$fieldFilterMap.set(Map.of());
            VarHandle vh$methodFilterMap = IMPL_LOOKUP.findStaticVarHandle(class$Reflection, "methodFilterMap", Map.class);
            vh$methodFilterMap.set(Map.of());
        } catch (Throwable t) {
            throw new OperationException(t);
        }
    }

    static void putObject(Field target, Object value) {
        UNSAFE.putObject(staticFieldBase(target), staticFieldOffset(target), value);
    }
}