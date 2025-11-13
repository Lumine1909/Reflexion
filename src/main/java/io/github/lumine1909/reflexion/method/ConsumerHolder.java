package io.github.lumine1909.reflexion.method;

final class ConsumerHolder {

    @FunctionalInterface
    public interface Cons0 extends Caller {

        void invoke();

        @Override
        default Object callStatic(Object... args) {
            invoke();
            return null;
        }

        @Override
        default Object callVirtual(Object instance, Object... args) {
            throw new UnsupportedOperationException();
        }
    }

    @FunctionalInterface
    public interface Cons1<T1> extends Caller {

        void invoke(T1 t1);

        @SuppressWarnings("unchecked")
        @Override
        default Object callStatic(Object... args) {
            invoke((T1) args[0]);
            return null;
        }

        @SuppressWarnings("unchecked")
        @Override
        default Object callVirtual(Object instance, Object... args) {
            invoke((T1) instance);
            return null;
        }
    }

    @FunctionalInterface
    public interface Cons2<T1, T2> extends Caller {

        void invoke(T1 t1, T2 t2);

        @SuppressWarnings("unchecked")
        @Override
        default Object callStatic(Object... args) {
            invoke((T1) args[0], (T2) args[1]);
            return null;
        }

        @SuppressWarnings("unchecked")
        @Override
        default Object callVirtual(Object instance, Object... args) {
            invoke((T1) instance, (T2) args[0]);
            return null;
        }
    }

    @FunctionalInterface
    public interface Cons3<T1, T2, T3> extends Caller {

        void invoke(T1 t1, T2 t2, T3 t3);

        @SuppressWarnings("unchecked")
        @Override
        default Object callStatic(Object... args) {
            invoke((T1) args[0], (T2) args[1], (T3) args[2]);
            return null;
        }

        @SuppressWarnings("unchecked")
        @Override
        default Object callVirtual(Object instance, Object... args) {
            invoke((T1) instance, (T2) args[0], (T3) args[1]);
            return null;
        }
    }

    @FunctionalInterface
    public interface Cons4<T1, T2, T3, T4> extends Caller {

        void invoke(T1 t1, T2 t2, T3 t3, T4 t4);

        @SuppressWarnings("unchecked")
        @Override
        default Object callStatic(Object... args) {
            invoke((T1) args[0], (T2) args[1], (T3) args[2], (T4) args[3]);
            return null;
        }

        @SuppressWarnings("unchecked")
        @Override
        default Object callVirtual(Object instance, Object... args) {
            invoke((T1) instance, (T2) args[0], (T3) args[1], (T4) args[2]);
            return null;
        }
    }

    @FunctionalInterface
    public interface Cons5<T1, T2, T3, T4, T5> extends Caller {

        void invoke(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5);

        @SuppressWarnings("unchecked")
        @Override
        default Object callStatic(Object... args) {
            invoke((T1) args[0], (T2) args[1], (T3) args[2], (T4) args[3], (T5) args[4]);
            return null;
        }

        @SuppressWarnings("unchecked")
        @Override
        default Object callVirtual(Object instance, Object... args) {
            invoke((T1) instance, (T2) args[0], (T3) args[1], (T4) args[2], (T5) args[3]);
            return null;
        }
    }

    @FunctionalInterface
    public interface Cons6<T1, T2, T3, T4, T5, T6> extends Caller {

        void invoke(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6);

        @SuppressWarnings("unchecked")
        @Override
        default Object callStatic(Object... args) {
            invoke((T1) args[0], (T2) args[1], (T3) args[2], (T4) args[3], (T5) args[4], (T6) args[5]);
            return null;
        }

        @SuppressWarnings("unchecked")
        @Override
        default Object callVirtual(Object instance, Object... args) {
            invoke((T1) instance, (T2) args[0], (T3) args[1], (T4) args[2], (T5) args[3], (T6) args[4]);
            return null;
        }
    }

    @FunctionalInterface
    public interface Cons7<T1, T2, T3, T4, T5, T6, T7> extends Caller {

        void invoke(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7);

        @SuppressWarnings("unchecked")
        @Override
        default Object callStatic(Object... args) {
            invoke((T1) args[0], (T2) args[1], (T3) args[2], (T4) args[3], (T5) args[4], (T6) args[5], (T7) args[6]);
            return null;
        }

        @SuppressWarnings("unchecked")
        @Override
        default Object callVirtual(Object instance, Object... args) {
            invoke((T1) instance, (T2) args[0], (T3) args[1], (T4) args[2], (T5) args[3], (T6) args[4], (T7) args[5]);
            return null;
        }
    }

    @FunctionalInterface
    public interface Cons8<T1, T2, T3, T4, T5, T6, T7, T8> extends Caller {

        void invoke(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8);

        @SuppressWarnings("unchecked")
        @Override
        default Object callStatic(Object... args) {
            invoke((T1) args[0], (T2) args[1], (T3) args[2], (T4) args[3], (T5) args[4],
                (T6) args[5], (T7) args[6], (T8) args[7]);
            return null;
        }

        @SuppressWarnings("unchecked")
        @Override
        default Object callVirtual(Object instance, Object... args) {
            invoke((T1) instance, (T2) args[0], (T3) args[1], (T4) args[2], (T5) args[3],
                (T6) args[4], (T7) args[5], (T8) args[6]);
            return null;
        }
    }

    @FunctionalInterface
    public interface Cons9<T1, T2, T3, T4, T5, T6, T7, T8, T9> extends Caller {

        void invoke(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9);

        @SuppressWarnings("unchecked")
        @Override
        default Object callStatic(Object... args) {
            invoke((T1) args[0], (T2) args[1], (T3) args[2], (T4) args[3], (T5) args[4],
                (T6) args[5], (T7) args[6], (T8) args[7], (T9) args[8]);
            return null;
        }

        @SuppressWarnings("unchecked")
        @Override
        default Object callVirtual(Object instance, Object... args) {
            invoke((T1) instance, (T2) args[0], (T3) args[1], (T4) args[2], (T5) args[3],
                (T6) args[4], (T7) args[5], (T8) args[6], (T9) args[7]);
            return null;
        }
    }

    @FunctionalInterface
    public interface Cons10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> extends Caller {

        void invoke(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10);

        @SuppressWarnings("unchecked")
        @Override
        default Object callStatic(Object... args) {
            invoke((T1) args[0], (T2) args[1], (T3) args[2], (T4) args[3], (T5) args[4],
                (T6) args[5], (T7) args[6], (T8) args[7], (T9) args[8], (T10) args[9]);
            return null;
        }

        @SuppressWarnings("unchecked")
        @Override
        default Object callVirtual(Object instance, Object... args) {
            invoke((T1) instance, (T2) args[0], (T3) args[1], (T4) args[2], (T5) args[3],
                (T6) args[4], (T7) args[5], (T8) args[6], (T9) args[7], (T10) args[8]);
            return null;
        }
    }
}