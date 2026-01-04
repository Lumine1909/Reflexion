package io.github.lumine1909.reflexion.internal;

import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static io.github.lumine1909.reflexion.internal.UnsafeUtil.IMPL_LOOKUP;

public class VarHolder {

    // Placeholders to exploit java inline
    private static final VarHandle o0 = null;
    private static final VarHandle o1 = null;
    private static final VarHandle o2 = null;
    private static final VarHandle o3 = null;
    private static final VarHandle o4 = null;
    private static final VarHandle o5 = null;
    private static final VarHandle o6 = null;
    private static final VarHandle o7 = null;
    private static final VarHandle o8 = null;
    private static final VarHandle o9 = null;
    private static final VarHandle o10 = null;
    private static final VarHandle o11 = null;
    private static final VarHandle o12 = null;
    private static final VarHandle o13 = null;
    private static final VarHandle o14 = null;
    private static final VarHandle o15 = null;
    private static final VarHandle o16 = null;
    private static final VarHandle o17 = null;
    private static final VarHandle o18 = null;
    private static final VarHandle o19 = null;
    private static final VarHandle o20 = null;
    private static final VarHandle o21 = null;
    private static final VarHandle o22 = null;
    private static final VarHandle o23 = null;
    private static final VarHandle o24 = null;
    private static final VarHandle o25 = null;
    private static final VarHandle o26 = null;
    private static final VarHandle o27 = null;
    private static final VarHandle o28 = null;
    private static final VarHandle o29 = null;
    private static final VarHandle o30 = null;
    private static final VarHandle o31 = null;
    private static final VarHandle o32 = null;
    private static final VarHandle o33 = null;
    private static final VarHandle o34 = null;
    private static final VarHandle o35 = null;
    private static final VarHandle o36 = null;
    private static final VarHandle o37 = null;
    private static final VarHandle o38 = null;
    private static final VarHandle o39 = null;
    private static final VarHandle o40 = null;
    private static final VarHandle o41 = null;
    private static final VarHandle o42 = null;
    private static final VarHandle o43 = null;
    private static final VarHandle o44 = null;
    private static final VarHandle o45 = null;
    private static final VarHandle o46 = null;
    private static final VarHandle o47 = null;
    private static final VarHandle o48 = null;
    private static final VarHandle o49 = null;
    @SuppressWarnings("unchecked")
    private static final Supplier<VarHandle>[] SUPPLIERS = new Supplier[]{
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
    public static AtomicInteger COUNTER = new AtomicInteger();

    public static Supplier<VarHandle> createSupplier(Field javaField) {
        int index = COUNTER.getAndIncrement();
        try {
            VarHandle varHandle = IMPL_LOOKUP.unreflectVarHandle(javaField);
            UnsafeUtil.putValue(VarHolder.class.getDeclaredField("o" + index), varHandle);
            return SUPPLIERS[index];
        } catch (Throwable t) {
            return null;
        }
    }
}