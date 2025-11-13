package io.github.lumine1909.reflexion.method;

final class FunctionHolder {

    @FunctionalInterface
    public interface Func0<R> extends Caller {

        R invoke();

        @Override
        default Object callStatic(Object... args) {
            return invoke();
        }

        @Override
        default Object callVirtual(Object instance, Object... args) {
            throw new UnsupportedOperationException();
        }
    }

    @FunctionalInterface
    public interface Func1<R, T1> extends Caller {

        R invoke(T1 t1);

        @SuppressWarnings("unchecked")
        @Override
        default Object callStatic(Object... args) {
            return invoke((T1) args[0]);
        }

        @SuppressWarnings("unchecked")
        @Override
        default Object callVirtual(Object instance, Object... args) {
            return invoke((T1) instance);
        }
    }

    @FunctionalInterface
    public interface Func2<R, T1, T2> extends Caller {

        R invoke(T1 t1, T2 t2);

        @SuppressWarnings("unchecked")
        @Override
        default Object callStatic(Object... args) {
            return invoke((T1) args[0], (T2) args[1]);
        }

        @SuppressWarnings("unchecked")
        @Override
        default Object callVirtual(Object instance, Object... args) {
            return invoke((T1) instance, (T2) args[0]);
        }
    }

    @FunctionalInterface
    public interface Func3<R, T1, T2, T3> extends Caller {

        R invoke(T1 t1, T2 t2, T3 t3);

        @SuppressWarnings("unchecked")
        @Override
        default Object callStatic(Object... args) {
            return invoke((T1) args[0], (T2) args[1], (T3) args[2]);
        }

        @SuppressWarnings("unchecked")
        @Override
        default Object callVirtual(Object instance, Object... args) {
            return invoke((T1) instance, (T2) args[0], (T3) args[1]);
        }
    }

    @FunctionalInterface
    public interface Func4<R, T1, T2, T3, T4> extends Caller {

        R invoke(T1 t1, T2 t2, T3 t3, T4 t4);

        @SuppressWarnings("unchecked")
        @Override
        default Object callStatic(Object... args) {
            return invoke((T1) args[0], (T2) args[1], (T3) args[2], (T4) args[3]);
        }

        @SuppressWarnings("unchecked")
        @Override
        default Object callVirtual(Object instance, Object... args) {
            return invoke((T1) instance, (T2) args[0], (T3) args[1], (T4) args[2]);
        }
    }

    @FunctionalInterface
    public interface Func5<R, T1, T2, T3, T4, T5> extends Caller {

        R invoke(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5);

        @SuppressWarnings("unchecked")
        @Override
        default Object callStatic(Object... args) {
            return invoke((T1) args[0], (T2) args[1], (T3) args[2], (T4) args[3], (T5) args[4]);
        }

        @SuppressWarnings("unchecked")
        @Override
        default Object callVirtual(Object instance, Object... args) {
            return invoke((T1) instance, (T2) args[0], (T3) args[1], (T4) args[2], (T5) args[3]);
        }
    }

    @FunctionalInterface
    public interface Func6<R, T1, T2, T3, T4, T5, T6> extends Caller {

        R invoke(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6);

        @SuppressWarnings("unchecked")
        @Override
        default Object callStatic(Object... args) {
            return invoke((T1) args[0], (T2) args[1], (T3) args[2], (T4) args[3], (T5) args[4], (T6) args[5]);
        }

        @SuppressWarnings("unchecked")
        @Override
        default Object callVirtual(Object instance, Object... args) {
            return invoke((T1) instance, (T2) args[0], (T3) args[1], (T4) args[2], (T5) args[3], (T6) args[4]);
        }
    }

    @FunctionalInterface
    public interface Func7<R, T1, T2, T3, T4, T5, T6, T7> extends Caller {

        R invoke(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7);

        @SuppressWarnings("unchecked")
        @Override
        default Object callStatic(Object... args) {
            return invoke((T1) args[0], (T2) args[1], (T3) args[2], (T4) args[3], (T5) args[4], (T6) args[5], (T7) args[6]);
        }

        @SuppressWarnings("unchecked")
        @Override
        default Object callVirtual(Object instance, Object... args) {
            return invoke((T1) instance, (T2) args[0], (T3) args[1], (T4) args[2], (T5) args[3], (T6) args[4], (T7) args[5]);
        }
    }

    @FunctionalInterface
    public interface Func8<R, T1, T2, T3, T4, T5, T6, T7, T8> extends Caller {

        R invoke(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8);

        @SuppressWarnings("unchecked")
        @Override
        default Object callStatic(Object... args) {
            return invoke((T1) args[0], (T2) args[1], (T3) args[2], (T4) args[3], (T5) args[4], (T6) args[5],
                (T7) args[6], (T8) args[7]);
        }

        @SuppressWarnings("unchecked")
        @Override
        default Object callVirtual(Object instance, Object... args) {
            return invoke((T1) instance, (T2) args[0], (T3) args[1], (T4) args[2], (T5) args[3], (T6) args[4],
                (T7) args[5], (T8) args[6]);
        }
    }

    @FunctionalInterface
    public interface Func9<R, T1, T2, T3, T4, T5, T6, T7, T8, T9> extends Caller {

        R invoke(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9);

        @SuppressWarnings("unchecked")
        @Override
        default Object callStatic(Object... args) {
            return invoke((T1) args[0], (T2) args[1], (T3) args[2], (T4) args[3], (T5) args[4], (T6) args[5],
                (T7) args[6], (T8) args[7], (T9) args[8]);
        }

        @SuppressWarnings("unchecked")
        @Override
        default Object callVirtual(Object instance, Object... args) {
            return invoke((T1) instance, (T2) args[0], (T3) args[1], (T4) args[2], (T5) args[3], (T6) args[4],
                (T7) args[5], (T8) args[6], (T9) args[7]);
        }
    }

    @FunctionalInterface
    public interface Func10<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> extends Caller {

        R invoke(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10);

        @SuppressWarnings("unchecked")
        @Override
        default Object callStatic(Object... args) {
            return invoke((T1) args[0], (T2) args[1], (T3) args[2], (T4) args[3], (T5) args[4], (T6) args[5],
                (T7) args[6], (T8) args[7], (T9) args[8], (T10) args[9]);
        }

        @SuppressWarnings("unchecked")
        @Override
        default Object callVirtual(Object instance, Object... args) {
            return invoke((T1) instance, (T2) args[0], (T3) args[1], (T4) args[2], (T5) args[3], (T6) args[4],
                (T7) args[5], (T8) args[6], (T9) args[7], (T10) args[8]);
        }
    }
}