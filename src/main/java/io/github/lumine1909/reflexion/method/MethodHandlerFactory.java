package io.github.lumine1909.reflexion.method;

import java.lang.invoke.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static io.github.lumine1909.reflexion.UnsafeUtil.IMPL_LOOKUP;

@SuppressWarnings("unchecked")
public class MethodHandlerFactory {

    public static MethodHandle convertToHandle(Method method) {
        Class<?> clazz = method.getDeclaringClass();
        MethodType type = MethodType.methodType(method.getReturnType(), method.getParameterTypes());
        String name = method.getName();
        MethodHandles.Lookup lookup = IMPL_LOOKUP.in(clazz);
        try {
            return Modifier.isStatic(method.getModifiers()) ? lookup.findStatic(clazz, name, type) : lookup.findVirtual(clazz, name, type);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}
