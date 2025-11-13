package io.github.lumine1909.reflexion.method;

final class FunctionHolder {

    @FunctionalInterface
    public interface Func0<R> {

        R invoke();
    }

    @FunctionalInterface
    public interface Func1<R, T1> {

        R invoke(T1 t1);
    }

    @FunctionalInterface
    public interface Func2<R, T1, T2> {

        R invoke(T1 t1, T2 t2);
    }

    @FunctionalInterface
    public interface Func3<R, T1, T2, T3> {

        R invoke(T1 t1, T2 t2, T3 t3);
    }

    @FunctionalInterface
    public interface Func4<R, T1, T2, T3, T4> {

        R invoke(T1 t1, T2 t2, T3 t3, T4 t4);
    }

    @FunctionalInterface
    public interface Func5<R, T1, T2, T3, T4, T5> {

        R invoke(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5);
    }

    @FunctionalInterface
    public interface Func6<R, T1, T2, T3, T4, T5, T6> {

        R invoke(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6);
    }

    @FunctionalInterface
    public interface Func7<R, T1, T2, T3, T4, T5, T6, T7> {

        R invoke(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7);
    }

    @FunctionalInterface
    public interface Func8<R, T1, T2, T3, T4, T5, T6, T7, T8> {

        R invoke(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8);
    }

    @FunctionalInterface
    public interface Func9<R, T1, T2, T3, T4, T5, T6, T7, T8, T9> {

        R invoke(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9);
    }

    @FunctionalInterface
    public interface Func10<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> {

        R invoke(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10);
    }
}