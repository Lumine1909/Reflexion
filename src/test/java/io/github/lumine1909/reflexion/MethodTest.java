package io.github.lumine1909.reflexion;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MethodTest {

    @Test
    public void testMethod() {
        A a = new A();
        Class<?> clazz = Class.of(A.class);
        Method<String> msGet = clazz.getMethod("getStr", String.class).orElseThrow();
        Method<Void> msSet = clazz.getMethod("setStr", void.class, String.class).orElseThrow();
        Method<Integer> mTest = clazz.getMethod("test", int.class).orElseThrow();
        assertEquals("42", msGet.invoke(a));
        msSet.invoke(a, "41");
        assertEquals("41", msGet.invoke(a));
        assertEquals(42, mTest.invoke(a));
    }
}
