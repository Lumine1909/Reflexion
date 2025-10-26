package io.github.lumine1909.reflexion.method;

final class FunctionHolder {

    @FunctionalInterface
    interface Func0<R> {

        R invoke();
    }

    @FunctionalInterface
    interface Func1<R> {

        R invoke(Object o1);
    }

    @FunctionalInterface
    interface Func2<R> {

        R invoke(Object o1, Object o2);
    }

    @FunctionalInterface
    interface Func3<R> {

        R invoke(Object o1, Object o2, Object o3);
    }

    @FunctionalInterface
    interface Func4<R> {

        R invoke(Object o1, Object o2, Object o3, Object o4);
    }

    @FunctionalInterface
    interface Func5<R> {

        R invoke(Object o1, Object o2, Object o3, Object o4, Object o5);
    }

    @FunctionalInterface
    interface Func6<R> {

        R invoke(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6);
    }

    @FunctionalInterface
    interface Func7<R> {

        R invoke(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7);
    }

    @FunctionalInterface
    interface Func8<R> {

        R invoke(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8);
    }

    @FunctionalInterface
    interface Func9<R> {

        R invoke(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8, Object o9);
    }

    @FunctionalInterface
    interface Func10<R> {

        R invoke(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8, Object o9, Object o10);
    }
}
