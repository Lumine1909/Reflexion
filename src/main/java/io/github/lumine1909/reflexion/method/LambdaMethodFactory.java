package io.github.lumine1909.reflexion.method;

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static io.github.lumine1909.reflexion.UnsafeUtil.IMPL_LOOKUP;

@SuppressWarnings("unchecked")
public class LambdaMethodFactory {

    public static <T> Caller<T> createCaller(Method method) {
        return createLambda(method, createProperty(method));
    }

    private static FunctionProperty createProperty(Method method) {
        boolean isStatic = Modifier.isStatic(method.getModifiers());
        Class<?> returnType = method.getReturnType();
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (!isStatic) {
            Class<?>[] realParameterTypes = new Class[parameterTypes.length + 1];
            realParameterTypes[0] = method.getDeclaringClass();
            System.arraycopy(parameterTypes, 0, realParameterTypes, 1, parameterTypes.length);
            parameterTypes = realParameterTypes;
        }
        return new FunctionProperty(returnType == void.class, returnType, parameterTypes);
    }

    private static <T> Caller<T> createLambda(Method method, FunctionProperty property) {
        try {
            MethodHandles.Lookup lookup = IMPL_LOOKUP.in(method.getDeclaringClass());
            MethodHandle target = lookup.unreflect(method);

            CallSite site = LambdaMetafactory.metafactory(
                lookup,
                "invoke",
                property.invokedType(),
                property.samMethodType(),
                target,
                property.instMethodType()
            );
            return (Caller<T>) site.getTarget().invoke();
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}
