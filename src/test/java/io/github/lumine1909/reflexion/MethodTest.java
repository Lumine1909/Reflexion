package io.github.lumine1909.reflexion;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MethodTest {

    @Test
    public void testVirtual() {
        A a = new A();
        Class<?> class$A = Class.of(A.class);
        Method<Integer> test = class$A.getMethod("test", int.class);
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
        assertTrue(longTestStatic.isStatic());
        assertFalse(longTest.isStatic());
        assertEquals("43", getStr.invoke(a));
    }

    @Test
    public void testSpecialAndConstructor() {
        Method<A> method$A$new = Method.of(A.class, "<init>", A.class, int.class, String.class);
        A a = method$A$new.invoke(null, 1, "1");
        Method<?> method$B$new = Method.of("io.github.lumine1909.reflexion.A$B", "<init>", void.class);
        Object b = method$B$new.invoke(null);
        Method<?> method$special$Object$toString = Method.of(Object.class, "toString", 3, String.class);
        Method<?> method$special$A$toString = Method.of(A.class, "toString", 3, String.class);
        assertNotEquals("B", method$special$Object$toString.invoke(b));
        assertEquals("A", method$special$A$toString.invoke(a));
        assertEquals("A", method$special$A$toString.invoke(b));
    }

    @Test
    public void testNull() {
        Method<Integer> null1 = Method.of("test.class", "null", 1, int.class);
        Method<String> null2 = Method.of(A.class, "null", 1, String.class);
        assertNull(null1);
        assertNull(null2);
    }
}
