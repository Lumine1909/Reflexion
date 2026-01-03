package io.github.lumine1909.reflexion;

import io.github.lumine1909.reflexion.internal.MethodHolder;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Optional;

import static io.github.lumine1909.reflexion.internal.UnsafeUtil.IMPL_LOOKUP;
import static io.github.lumine1909.reflexion.internal.UnsafeUtil.UNSAFE;

public record Class<T>(java.lang.Class<T> javaClass) {

    @SuppressWarnings("unchecked")
    public static <T> Optional<Class<T>> forName(String name) {
        try {
            java.lang.Class<T> clazz = (java.lang.Class<T>) java.lang.Class.forName(name);
            return Optional.of(new Class<>(clazz));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> Optional<Class<T>> forName(String name, boolean initialize, ClassLoader loader) {
        try {
            java.lang.Class<T> clazz = (java.lang.Class<T>) java.lang.Class.forName(name, initialize, loader);
            return Optional.of(new Class<>(clazz));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static <T> Class<T> of(java.lang.Class<T> clazz) {
        return new Class<>(clazz);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public <S> Optional<Field<S>> getField(String name) {
        return getField(name, (java.lang.Class) null);
    }

    public <S> Optional<Field<S>> getField(String name, Class<S> type) {
        return getField(name, type.javaClass);
    }

    public <S> Optional<Field<S>> getField(String name, java.lang.Class<S> type) {
        try {
            java.lang.reflect.Field field = javaClass.getDeclaredField(name);
            if (type != null && field.getType() != type) {
                return Optional.empty();
            }
            return Optional.of(new Field<>(field));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public <S> Optional<Method<S>> getMethod(String name, Class<S> returnType, Class<?>... parameterTypes) {
        return getMethod(
            name,
            returnType.javaClass,
            Arrays.stream(parameterTypes).map(c -> c.javaClass).toArray(java.lang.Class[]::new)
        );
    }

    public <S> Optional<Method<S>> getMethod(String name, java.lang.Class<S> returnType, java.lang.Class<?>... parameterTypes) {
        try {
            java.lang.reflect.Method method = javaClass.getDeclaredMethod(name, parameterTypes);
            MethodHandle methodHandle = IMPL_LOOKUP.unreflect(method);
            if (!returnType.isAssignableFrom(method.getReturnType())) {
                return Optional.empty();
            }
            return Optional.of(new Method<>(method, parameterTypes.length, Modifier.isStatic(method.getModifiers()), methodHandle, MethodHolder.createSupplier(method)));
        } catch (Exception ignored) {
        }
        try {
            MethodHandle methodHandle = IMPL_LOOKUP.findVirtual(javaClass, name, MethodType.methodType(returnType, parameterTypes));
            return Optional.of(new Method<>(null, parameterTypes.length, false, methodHandle, null));
        } catch (Exception ignored) {
        }
        try {
            MethodHandle methodHandle = IMPL_LOOKUP.findStatic(javaClass, name, MethodType.methodType(returnType, parameterTypes));
            return Optional.of(new Method<>(null, parameterTypes.length, true, methodHandle, null));
        } catch (Exception ignored) {
        }
        return Optional.empty();
    }

    @SuppressWarnings("unchecked")
    public T newInstance() {
        try {
            return (T) UNSAFE.allocateInstance(javaClass);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}