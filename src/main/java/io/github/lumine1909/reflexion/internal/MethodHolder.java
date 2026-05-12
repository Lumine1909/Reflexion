package io.github.lumine1909.reflexion.internal;

import java.lang.invoke.MethodHandle;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public final class MethodHolder {

    // Placeholders to exploit java inline
    private static final MethodHandle o0 = null, o1 = null, o2 = null, o3 = null, o4 = null, o5 = null, o6 = null, o7 = null, o8 = null, o9 = null, o10 = null, o11 = null, o12 = null, o13 = null, o14 = null, o15 = null, o16 = null, o17 = null, o18 = null, o19 = null, o20 = null, o21 = null, o22 = null, o23 = null, o24 = null, o25 = null, o26 = null, o27 = null, o28 = null, o29 = null, o30 = null, o31 = null, o32 = null, o33 = null, o34 = null, o35 = null, o36 = null, o37 = null, o38 = null, o39 = null, o40 = null, o41 = null, o42 = null, o43 = null, o44 = null, o45 = null, o46 = null, o47 = null, o48 = null, o49 = null, o50 = null, o51 = null, o52 = null, o53 = null, o54 = null, o55 = null, o56 = null, o57 = null, o58 = null, o59 = null, o60 = null, o61 = null, o62 = null, o63 = null;
    private static final Supplier<MethodHandle>[] OBJ = new Supplier[]{() -> o0, () -> o1, () -> o2, () -> o3, () -> o4, () -> o5, () -> o6, () -> o7, () -> o8, () -> o9, () -> o10, () -> o11, () -> o12, () -> o13, () -> o14, () -> o15, () -> o16, () -> o17, () -> o18, () -> o19, () -> o20, () -> o21, () -> o22, () -> o23, () -> o24, () -> o25, () -> o26, () -> o27, () -> o28, () -> o29, () -> o30, () -> o31, () -> o32, () -> o33, () -> o34, () -> o35, () -> o36, () -> o37, () -> o38, () -> o39, () -> o40, () -> o41, () -> o42, () -> o43, () -> o44, () -> o45, () -> o46, () -> o47, () -> o48, () -> o49, () -> o50, () -> o51, () -> o51, () -> o52, () -> o53, () -> o54, () -> o55, () -> o56, () -> o57, () -> o58, () -> o59, () -> o60, () -> o61, () -> o62, () -> o63};
    private static final AtomicInteger ID = new AtomicInteger();

    public static Supplier<MethodHandle> createSupplier(MethodHandle MethodHandle) {
        int index = ID.getAndIncrement();
        try {
            UnsafeUtil.putObject(MethodHolder.class.getDeclaredField("o" + index), MethodHandle);
            return OBJ[index];
        } catch (Throwable t) {
            return null;
        }
    }
}
