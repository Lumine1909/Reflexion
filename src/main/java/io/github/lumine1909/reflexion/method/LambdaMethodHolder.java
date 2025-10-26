package io.github.lumine1909.reflexion.method;

import java.lang.invoke.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import static io.github.lumine1909.reflexion.method.FunctionHolder.*;

@SuppressWarnings("unchecked")
public class LambdaMethodHolder<T> {

    private final Object lambda;
    private final int paramCount;
    private final boolean isStatic;

    public LambdaMethodHolder(Method method) {
        this.paramCount = method.getParameterCount();
        this.isStatic = Modifier.isStatic(method.getModifiers());
        this.lambda = createLambda(method);
    }

    public T invoke(Object instance, Object... args) {
        if (isStatic) {
            return invokeStatic(args);
        } else {
            return invokeVirtual(instance, args);
        }
    }

    // Static method call
    private T invokeStatic(Object... args) {
        return switch (paramCount) {
            case 0 -> ((Func0<T>) lambda).invoke();
            case 1 -> ((Func1<T>) lambda).invoke(args[0]);
            case 2 -> ((Func2<T>) lambda).invoke(args[0], args[1]);
            case 3 -> ((Func3<T>) lambda).invoke(args[0], args[1], args[2]);
            case 4 -> ((Func4<T>) lambda).invoke(args[0], args[1], args[2], args[3]);
            case 5 -> ((Func5<T>) lambda).invoke(args[0], args[1], args[2], args[3], args[4]);
            case 6 -> ((Func6<T>) lambda).invoke(args[0], args[1], args[2], args[3], args[4], args[5]);
            case 7 -> ((Func7<T>) lambda).invoke(args[0], args[1], args[2], args[3], args[4], args[5], args[6]);
            case 8 -> ((Func8<T>) lambda).invoke(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7]);
            case 9 -> ((Func9<T>) lambda).invoke(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8]);
            case 10 -> ((Func10<T>) lambda).invoke(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9]);
            default -> throw new IllegalStateException("Unsupported arity: " + paramCount);
        };
    }

    // Instance method call
    private T invokeVirtual(Object instance, Object... args) {
        return switch (paramCount) {
            case 0 -> ((Func1<T>) lambda).invoke(instance);
            case 1 -> ((Func2<T>) lambda).invoke(instance, args[0]);
            case 2 -> ((Func3<T>) lambda).invoke(instance, args[0], args[1]);
            case 3 -> ((Func4<T>) lambda).invoke(instance, args[0], args[1], args[2]);
            case 4 -> ((Func5<T>) lambda).invoke(instance, args[0], args[1], args[2], args[3]);
            case 5 -> ((Func6<T>) lambda).invoke(instance, args[0], args[1], args[2], args[3], args[4]);
            case 6 -> ((Func7<T>) lambda).invoke(instance, args[0], args[1], args[2], args[3], args[4], args[5]);
            case 7 -> ((Func8<T>) lambda).invoke(instance, args[0], args[1], args[2], args[3], args[4], args[5], args[6]);
            case 8 -> ((Func9<T>) lambda).invoke(instance, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7]);
            case 9 -> ((Func10<T>) lambda).invoke(instance, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8]);
            case 10 -> throw new IllegalStateException("Instance methods >9 params not implemented");
            default -> throw new IllegalStateException("Unsupported arity: " + paramCount);
        };
    }

    private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();

    private static Object createLambda(Method method) {
        try {
            boolean isStatic = Modifier.isStatic(method.getModifiers());
            int paramCount = method.getParameterCount();

            MethodHandle handle = LOOKUP.unreflect(method);

            // If instance method, adjust type to include "this"
            if (!isStatic) {
                handle = handle.asType(MethodType.methodType(method.getReturnType(), prepend(Object.class, paramCount + 1)));
            }

            Class<?> fiClazz = getFunctionalInterface(paramCount, isStatic);
            Method sam = fiClazz.getMethods()[0];

            MethodType invokedType = MethodType.methodType(fiClazz);
            MethodType samType;
            if (isStatic) {
                samType = MethodType.methodType(method.getReturnType(), repeat(Object.class, paramCount));
            } else {
                samType = MethodType.methodType(method.getReturnType(), prepend(Object.class, paramCount + 1));
            }

            CallSite site = LambdaMetafactory.metafactory(
                LOOKUP,
                sam.getName(),
                invokedType,
                samType.erase(),
                handle,
                handle.type()
            );

            return site.getTarget().invokeExact();

        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    private static Class<?> getFunctionalInterface(int paramCount, boolean isStatic) {
        if (isStatic) {
            return switch (paramCount) {
                case 0 -> Func0.class;
                case 1 -> Func1.class;
                case 2 -> Func2.class;
                case 3 -> Func3.class;
                case 4 -> Func4.class;
                case 5 -> Func5.class;
                case 6 -> Func6.class;
                case 7 -> Func7.class;
                case 8 -> Func8.class;
                case 9 -> Func9.class;
                case 10 -> Func10.class;
                default -> throw new IllegalStateException("Unsupported arity: " + paramCount);
            };
        } else {
            // instance methods need arity+1
            return switch (paramCount) {
                case 0 -> Func1.class; // this -> invoke(instance)
                case 1 -> Func2.class;
                case 2 -> Func3.class;
                case 3 -> Func4.class;
                case 4 -> Func5.class;
                case 5 -> Func6.class;
                case 6 -> Func7.class;
                case 7 -> Func8.class;
                case 8 -> Func9.class;
                case 9 -> Func10.class;
                default -> throw new IllegalStateException("Unsupported arity: " + paramCount);
            };
        }
    }

    private static Class<?>[] repeat(Class<?> clazz, int n) {
        Class<?>[] arr = new Class<?>[n];
        Arrays.fill(arr, clazz);
        return arr;
    }

    private static Class<?>[] prepend(Class<?> clazz, int n) {
        Class<?>[] arr = new Class<?>[n];
        arr[0] = Object.class;
        Arrays.fill(arr, 1, n, Object.class);
        return arr;
    }
}
