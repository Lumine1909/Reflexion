package io.github.lumine1909.reflexion.internal;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class FieldHolder {

    // Placeholders to exploit java inline
    private static final Field o0 = null;
    private static final Field o1 = null;
    private static final Field o2 = null;
    private static final Field o3 = null;
    private static final Field o4 = null;
    private static final Field o5 = null;
    private static final Field o6 = null;
    private static final Field o7 = null;
    private static final Field o8 = null;
    private static final Field o9 = null;
    private static final Field o10 = null;
    private static final Field o11 = null;
    private static final Field o12 = null;
    private static final Field o13 = null;
    private static final Field o14 = null;
    private static final Field o15 = null;
    private static final Field o16 = null;
    private static final Field o17 = null;
    private static final Field o18 = null;
    private static final Field o19 = null;
    private static final Field o20 = null;
    private static final Field o21 = null;
    private static final Field o22 = null;
    private static final Field o23 = null;
    private static final Field o24 = null;
    private static final Field o25 = null;
    private static final Field o26 = null;
    private static final Field o27 = null;
    private static final Field o28 = null;
    private static final Field o29 = null;
    private static final Field o30 = null;
    private static final Field o31 = null;
    private static final Field o32 = null;
    private static final Field o33 = null;
    private static final Field o34 = null;
    private static final Field o35 = null;
    private static final Field o36 = null;
    private static final Field o37 = null;
    private static final Field o38 = null;
    private static final Field o39 = null;
    private static final Field o40 = null;
    private static final Field o41 = null;
    private static final Field o42 = null;
    private static final Field o43 = null;
    private static final Field o44 = null;
    private static final Field o45 = null;
    private static final Field o46 = null;
    private static final Field o47 = null;
    private static final Field o48 = null;
    private static final Field o49 = null;
    @SuppressWarnings("unchecked")
    private static final Supplier<Field>[] SUPPLIERS = new Supplier[]{
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

    public static Supplier<Field> createSupplier(Field field) {
        int index = COUNTER.getAndIncrement();
        try {
            field.setAccessible(true);
            UnsafeUtil.putValue(FieldHolder.class.getDeclaredField("o" + index), field);
            return SUPPLIERS[index];
        } catch (Throwable t) {
            return null;
        }
    }
}