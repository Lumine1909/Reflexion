package io.github.lumine1909.reflexion;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MethodTest {

    @Test
    public void testMethod() {
        A a = new A();
        io.github.lumine1909.reflexion.Class<?> clazz = Class.of(A.class);
        Method<Integer> test = clazz.getMethod("test", int.class);
        Method<String> getStr = Method.of(A.class, "getStr", String.class);
        Method<Void> setStr = Method.of(A.class, "setStr", void.class, String.class);
        Method<Void> longTest = Method.of(A.class, "test", void.class, int.class, int.class, int.class, int.class, int.class, Object.class);
        Method<Void> longTestStatic = Method.of(A.class, "testStatic", void.class, int.class, int.class, int.class, int.class, int.class, Object.class);
        assertEquals(42, test.invoke(null));
        assertEquals("42", getStr.invoke(a));
        assertEquals("42", getStr.invoke(a));
        setStr.invoke(a, "43");
        setStr.invoke(a, "43");
        longTest.invoke(a, 0, 1, 2, 3, 4, "5");
        longTestStatic.invoke(a, 0, 1, 2, 3, 4, "5");
        assertEquals("43", getStr.invoke(a));
    }
}
