package io.github.lumine1909.reflexion.method;

import java.lang.invoke.MethodType;
import java.util.Arrays;

public record FunctionProperty(
    boolean isVoidReturn, Class<?> returnType, Class<?>... parameterTypes
) {

    private static Class<?>[] makeObjects(int count) {
        Class<?>[] classes = new Class[count];
        Arrays.fill(classes, Object.class);
        return classes;
    }

    private static Class<?> box(Class<?> clazz) {
        if (clazz == byte.class) {
            return Byte.class;
        } else if (clazz == boolean.class) {
            return Boolean.class;
        } else if (clazz == int.class) {
            return Integer.class;
        } else if (clazz == long.class) {
            return Long.class;
        } else if (clazz == double.class) {
            return Double.class;
        } else if (clazz == float.class) {
            return Float.class;
        } else {
            return clazz;
        }
    }

    public MethodType invokedType() {
        String outer = isVoidReturn ? "ConsumerHolder" : "FunctionHolder";
        String inner = isVoidReturn ? "Cons" : "Func";
        String name = getClass().getPackageName() + "." + outer + "$" + inner + parameterTypes.length;
        try {
            return MethodType.methodType(Class.forName(name));
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public MethodType samMethodType() {
        Class<?>[] objects = makeObjects(parameterTypes.length);
        return isVoidReturn ? MethodType.methodType(void.class, objects) : MethodType.methodType(Object.class, objects);
    }

    public MethodType instMethodType() {
        return isVoidReturn ? MethodType.methodType(void.class, boxedTypes()) : MethodType.methodType(Object.class, boxedTypes());
    }

    private Class<?>[] boxedTypes() {
        Class<?>[] classes = new Class[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            classes[i] = box(parameterTypes[i]);
        }
        return classes;
    }
}
