package io.github.lumine1909.reflexion.internal;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class MethodHolder {

    // Placeholders to exploit java inline
    private static final AtomicInteger COUNTER = new AtomicInteger();
    private static final Method o0 = null;
    private static final Method o1 = null;
    private static final Method o2 = null;
    private static final Method o3 = null;
    private static final Method o4 = null;
    private static final Method o5 = null;
    private static final Method o6 = null;
    private static final Method o7 = null;
    private static final Method o8 = null;
    private static final Method o9 = null;
    private static final Method o10 = null;
    private static final Method o11 = null;
    private static final Method o12 = null;
    private static final Method o13 = null;
    private static final Method o14 = null;
    private static final Method o15 = null;
    private static final Method o16 = null;
    private static final Method o17 = null;
    private static final Method o18 = null;
    private static final Method o19 = null;
    private static final Method o20 = null;
    private static final Method o21 = null;
    private static final Method o22 = null;
    private static final Method o23 = null;
    private static final Method o24 = null;
    private static final Method o25 = null;
    private static final Method o26 = null;
    private static final Method o27 = null;
    private static final Method o28 = null;
    private static final Method o29 = null;
    private static final Method o30 = null;
    private static final Method o31 = null;
    private static final Method o32 = null;
    private static final Method o33 = null;
    private static final Method o34 = null;
    private static final Method o35 = null;
    private static final Method o36 = null;
    private static final Method o37 = null;
    private static final Method o38 = null;
    private static final Method o39 = null;
    private static final Method o40 = null;
    private static final Method o41 = null;
    private static final Method o42 = null;
    private static final Method o43 = null;
    private static final Method o44 = null;
    private static final Method o45 = null;
    private static final Method o46 = null;
    private static final Method o47 = null;
    private static final Method o48 = null;
    private static final Method o49 = null;
    @SuppressWarnings("unchecked")
    private static final Supplier<Method>[] SUPPLIERS = new Supplier[]{
        () -> o0, () -> o1, () -> o2, () -> o3, () -> o4,
        () -> o5, () -> o6, () -> o7, () -> o8, () -> o9,
        () -> o10, () -> o11, () -> o12, () -> o13, () -> o14,
        () -> o15, () -> o16, () -> o17, () -> o18, () -> o19,
        () -> o20, () -> o21, () -> o22, () -> o23, () -> o24,
        () -> o25, () -> o26, () -> o27, () -> o28, () -> o29,
        () -> o30, () -> o31, () -> o32, () -> o33, () -> o34,
        () -> o35, () -> o36, () -> o37, () -> o38, () -> o39,
        () -> o40, () -> o41, () -> o42, () -> o43, () -> o44,
        () -> o45, () -> o46, () -> o47, () -> o48, () -> o49,
    };

    public static Supplier<Method> createSupplier(Method method) {
        int index = COUNTER.getAndIncrement();
        try {
            method.setAccessible(true);
            UnsafeUtil.putValue(MethodHolder.class.getDeclaredField("o" + index), method);
            return SUPPLIERS[index];
        } catch (Throwable t) {
            return null;
        }
    }
}
