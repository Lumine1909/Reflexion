package io.github.lumine1909.reflexion.method;

import java.lang.reflect.Method;

public record LambdaMethodHolder<T>(Object lambda, boolean isVoidReturn) {

    public LambdaMethodHolder(Method javaMethod) {
        this(LambdaMethodFactory.createCaller(javaMethod), javaMethod.getReturnType() == void.class);
    }

    public T invokeStatic(Object... args) {
        return isVoidReturn ? invokeConsumerStatic(args) : invokeFunctionStatic(args);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private T invokeConsumerStatic(Object... args) {
        switch (args.length) {
            case 0 -> ((ConsumerHolder.Cons0) lambda).invoke();
            case 1 -> ((ConsumerHolder.Cons1) lambda).invoke(args[0]);
            case 2 -> ((ConsumerHolder.Cons2) lambda).invoke(args[0], args[1]);
            case 3 -> ((ConsumerHolder.Cons3) lambda).invoke(args[0], args[1], args[2]);
            case 4 -> ((ConsumerHolder.Cons4) lambda).invoke(args[0], args[1], args[2], args[3]);
            case 5 -> ((ConsumerHolder.Cons5) lambda).invoke(args[0], args[1], args[2], args[3], args[4]);
            case 6 -> ((ConsumerHolder.Cons6) lambda).invoke(args[0], args[1], args[2], args[3], args[4], args[5]);
            case 7 -> ((ConsumerHolder.Cons7) lambda).invoke(args[0], args[1], args[2], args[3], args[4], args[5], args[6]);
            case 8 -> ((ConsumerHolder.Cons8) lambda).invoke(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7]);
            //case 9 -> ((ConsumerHolder.Cons9) lambda).invoke(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8]);
            //case 10 -> ((ConsumerHolder.Cons10) lambda).invoke(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9]);
            default -> throw new UnsupportedOperationException();
        }
        return null;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private T invokeFunctionStatic(Object... args) {
        return (T) switch (args.length) {
            case 0 -> ((FunctionHolder.Func0) lambda).invoke();
            case 1 -> ((FunctionHolder.Func1) lambda).invoke(args[0]);
            case 2 -> ((FunctionHolder.Func2) lambda).invoke(args[0], args[1]);
            case 3 -> ((FunctionHolder.Func3) lambda).invoke(args[0], args[1], args[2]);
            case 4 -> ((FunctionHolder.Func4) lambda).invoke(args[0], args[1], args[2], args[3]);
            case 5 -> ((FunctionHolder.Func5) lambda).invoke(args[0], args[1], args[2], args[3], args[4]);
            case 6 -> ((FunctionHolder.Func6) lambda).invoke(args[0], args[1], args[2], args[3], args[4], args[5]);
            case 7 -> ((FunctionHolder.Func7) lambda).invoke(args[0], args[1], args[2], args[3], args[4], args[5], args[6]);
            case 8 -> ((FunctionHolder.Func8) lambda).invoke(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7]);
            //case 9 -> ((FunctionHolder.Func9) lambda).invoke(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8]);
            //case 10 -> ((FunctionHolder.Func10) lambda).invoke(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9]);
            default -> throw new UnsupportedOperationException();
        };
    }

    public T invokeVirtual(Object instance, Object... args) {
        return isVoidReturn ? invokeConsumerVirtual(instance, args) : invokeFunctionVirtual(instance, args);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private T invokeConsumerVirtual(Object instance, Object... args) {
        switch (args.length) {
            case 0 -> ((ConsumerHolder.Cons1) lambda).invoke(instance);
            case 1 -> ((ConsumerHolder.Cons2) lambda).invoke(instance, args[0]);
            case 2 -> ((ConsumerHolder.Cons3) lambda).invoke(instance, args[0], args[1]);
            case 3 -> ((ConsumerHolder.Cons4) lambda).invoke(instance, args[0], args[1], args[2]);
            case 4 -> ((ConsumerHolder.Cons5) lambda).invoke(instance, args[0], args[1], args[2], args[3]);
            case 5 -> ((ConsumerHolder.Cons6) lambda).invoke(instance, args[0], args[1], args[2], args[3], args[4]);
            case 6 -> ((ConsumerHolder.Cons7) lambda).invoke(instance, args[0], args[1], args[2], args[3], args[4], args[5]);
            case 7 -> ((ConsumerHolder.Cons8) lambda).invoke(instance, args[0], args[1], args[2], args[3], args[4], args[5], args[6]);
            case 8 -> ((ConsumerHolder.Cons9) lambda).invoke(instance, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7]);
            //case 9 -> ((ConsumerHolder.Cons10) lambda).invoke(instance, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8]);

            default -> throw new UnsupportedOperationException();
        }
        return null;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private T invokeFunctionVirtual(Object instance, Object... args) {
        return (T) switch (args.length) {
            case 0 -> ((FunctionHolder.Func1) lambda).invoke(instance);
            case 1 -> ((FunctionHolder.Func2) lambda).invoke(instance, args[0]);
            case 2 -> ((FunctionHolder.Func3) lambda).invoke(instance, args[0], args[1]);
            case 3 -> ((FunctionHolder.Func4) lambda).invoke(instance, args[0], args[1], args[2]);
            case 4 -> ((FunctionHolder.Func5) lambda).invoke(instance, args[0], args[1], args[2], args[3]);
            case 5 -> ((FunctionHolder.Func6) lambda).invoke(instance, args[0], args[1], args[2], args[3], args[4]);
            case 6 -> ((FunctionHolder.Func7) lambda).invoke(instance, args[0], args[1], args[2], args[3], args[4], args[5]);
            case 7 -> ((FunctionHolder.Func8) lambda).invoke(instance, args[0], args[1], args[2], args[3], args[4], args[5], args[6]);
            case 8 -> ((FunctionHolder.Func9) lambda).invoke(instance, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7]);
            //case 9 -> ((FunctionHolder.Func10) lambda).invoke(instance, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8]);
            default -> throw new UnsupportedOperationException();
        };
    }
}
