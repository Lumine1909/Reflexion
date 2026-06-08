package io.github.lumine1909.reflexion.internal;

import io.github.lumine1909.reflexion.exception.OperationException;
import sun.misc.Unsafe;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;
import java.util.Map;

@SuppressWarnings({"DataFlowIssue", "deprecation", "removal"})
public final class UnsafeUtil {

    public static final Unsafe UNSAFE = null;
    public static final MethodHandles.Lookup IMPL_LOOKUP = null;
    public static final Object INTERNAL_UNSAFE = null;

    private static final java.lang.Class<?> class$InternalUnsafe = null;
    private static final MethodHandle mh$fieldOffset = null;
    private static final MethodHandle mh$objectFieldOffset = null;
    private static final MethodHandle mh$staticFieldBase = null;
    private static final MethodHandle mh$staticFieldOffset = null;

    static {
        try {
            Field field$unsafe = Unsafe.class.getDeclaredField("theUnsafe");
            field$unsafe.setAccessible(true);
            Unsafe unsafe = (Unsafe) field$unsafe.get(null);

            init(unsafe, null, null, null);
            clearReflectionFilter();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @SuppressWarnings("ConstantConditions")
    public static void init(Unsafe unsafe, MethodHandles.Lookup implLookup, Class<?> class$InternalUnsafe, Object internalUnsafe) {
        if (unsafe == null && UNSAFE == null) {
            throw new NullPointerException("Unsafe is null");
        }
        try {
            if (UNSAFE == null) {
                Field f = UnsafeUtil.class.getDeclaredField("UNSAFE");
                unsafe.putObject(UnsafeUtil.class, unsafe.staticFieldOffset(f), unsafe);
            }
            if (IMPL_LOOKUP == null) {
                if (implLookup == null) {
                    Field field$implLookup = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
                    implLookup = (MethodHandles.Lookup) UNSAFE.getObject(MethodHandles.Lookup.class, UNSAFE.staticFieldOffset(field$implLookup));
                }
                Field f = UnsafeUtil.class.getDeclaredField("IMPL_LOOKUP");
                UNSAFE.putObject(UnsafeUtil.class, UNSAFE.staticFieldOffset(f), implLookup);
            }
            if (UnsafeUtil.class$InternalUnsafe == null) {
                if (class$InternalUnsafe == null) {
                    class$InternalUnsafe = Class.forName("jdk.internal.misc.Unsafe");
                }
                Field f = UnsafeUtil.class.getDeclaredField("class$InternalUnsafe");
                UNSAFE.putObject(UnsafeUtil.class, UNSAFE.staticFieldOffset(f), class$InternalUnsafe);
            }
            if (INTERNAL_UNSAFE == null) {
                if (internalUnsafe == null) {
                    Field field$internalUnsafe = UnsafeUtil.class$InternalUnsafe.getDeclaredField("theUnsafe");
                    internalUnsafe = UNSAFE.getObject(UNSAFE.staticFieldBase(field$internalUnsafe), UNSAFE.staticFieldOffset(field$internalUnsafe));
                }
                Field f = UnsafeUtil.class.getDeclaredField("INTERNAL_UNSAFE");
                UNSAFE.putObject(UnsafeUtil.class, UNSAFE.staticFieldOffset(f), internalUnsafe);
            }
            if (mh$fieldOffset == null) {
                MethodHandle fieldOffset = IMPL_LOOKUP.findVirtual(class$InternalUnsafe, "objectFieldOffset", MethodType.methodType(long.class, Class.class, String.class));
                Field f = UnsafeUtil.class.getDeclaredField("mh$fieldOffset");
                UNSAFE.putObject(UnsafeUtil.class, UNSAFE.staticFieldOffset(f), fieldOffset);
            }
            if (mh$objectFieldOffset == null) {
                MethodHandle objectFieldOffset = IMPL_LOOKUP.findVirtual(class$InternalUnsafe, "objectFieldOffset", MethodType.methodType(long.class, Field.class));
                UNSAFE.putObject(UnsafeUtil.class, fieldOffset(UnsafeUtil.class, "mh$objectFieldOffset"), objectFieldOffset);
            }
            if (mh$staticFieldBase == null) {
                MethodHandle staticFieldBase = IMPL_LOOKUP.findVirtual(class$InternalUnsafe, "staticFieldBase", MethodType.methodType(Object.class, Field.class));
                UNSAFE.putObject(UnsafeUtil.class, fieldOffset(UnsafeUtil.class, "mh$staticFieldBase"), staticFieldBase);
            }
            if (mh$staticFieldOffset == null) {
                MethodHandle staticFieldOffset = IMPL_LOOKUP.findVirtual(class$InternalUnsafe, "staticFieldOffset", MethodType.methodType(long.class, Field.class));
                UNSAFE.putObject(UnsafeUtil.class, fieldOffset(UnsafeUtil.class, "mh$staticFieldOffset"), staticFieldOffset);
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

    public static long fieldOffset(Class<?> clazz, String name) {
        try {
            return (long) mh$fieldOffset.invoke(INTERNAL_UNSAFE, clazz, name);
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

    public static void putObject(Class<?> clazz, String name, Object value) {
        UNSAFE.putObject(clazz, fieldOffset(clazz, name), value);
    }
}