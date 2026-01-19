package io.github.lumine1909.reflexion.internal;

import java.lang.invoke.MethodHandle;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public final class MethodHolder {

    // Placeholders to exploit java inline
    private static final AtomicInteger COUNTER = new AtomicInteger();
    private static final MethodHandle o0 = null;
    private static final MethodHandle o1 = null;
    private static final MethodHandle o2 = null;
    private static final MethodHandle o3 = null;
    private static final MethodHandle o4 = null;
    private static final MethodHandle o5 = null;
    private static final MethodHandle o6 = null;
    private static final MethodHandle o7 = null;
    private static final MethodHandle o8 = null;
    private static final MethodHandle o9 = null;
    private static final MethodHandle o10 = null;
    private static final MethodHandle o11 = null;
    private static final MethodHandle o12 = null;
    private static final MethodHandle o13 = null;
    private static final MethodHandle o14 = null;
    private static final MethodHandle o15 = null;
    private static final MethodHandle o16 = null;
    private static final MethodHandle o17 = null;
    private static final MethodHandle o18 = null;
    private static final MethodHandle o19 = null;
    private static final MethodHandle o20 = null;
    private static final MethodHandle o21 = null;
    private static final MethodHandle o22 = null;
    private static final MethodHandle o23 = null;
    private static final MethodHandle o24 = null;
    private static final MethodHandle o25 = null;
    private static final MethodHandle o26 = null;
    private static final MethodHandle o27 = null;
    private static final MethodHandle o28 = null;
    private static final MethodHandle o29 = null;
    private static final MethodHandle o30 = null;
    private static final MethodHandle o31 = null;
    private static final MethodHandle o32 = null;
    private static final MethodHandle o33 = null;
    private static final MethodHandle o34 = null;
    private static final MethodHandle o35 = null;
    private static final MethodHandle o36 = null;
    private static final MethodHandle o37 = null;
    private static final MethodHandle o38 = null;
    private static final MethodHandle o39 = null;
    private static final MethodHandle o40 = null;
    private static final MethodHandle o41 = null;
    private static final MethodHandle o42 = null;
    private static final MethodHandle o43 = null;
    private static final MethodHandle o44 = null;
    private static final MethodHandle o45 = null;
    private static final MethodHandle o46 = null;
    private static final MethodHandle o47 = null;
    private static final MethodHandle o48 = null;
    private static final MethodHandle o49 = null;
    @SuppressWarnings("unchecked")
    private static final Supplier<MethodHandle>[] SUPPLIERS = new Supplier[]{
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

    public static Supplier<MethodHandle> createSupplier(MethodHandle MethodHandle) {
        int index = COUNTER.getAndIncrement();
        try {
            UnsafeUtil.putObject(MethodHolder.class.getDeclaredField("o" + index), MethodHandle);
            return SUPPLIERS[index];
        } catch (Throwable t) {
            return null;
        }
    }
}
