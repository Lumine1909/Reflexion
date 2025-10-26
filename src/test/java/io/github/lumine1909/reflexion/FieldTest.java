package io.github.lumine1909.reflexion;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FieldTest {


    @Test
    public void testField() {
        A a = new A();
        Class<?> clazz = Class.of(A.class);
        Field<Byte> fb = clazz.getField("b", byte.class).orElseThrow();
        Field<Integer> fi = clazz.getField("i", int.class).orElseThrow();
        Field<String> fs = clazz.getField("str", String.class).orElseThrow();
        assertEquals((byte) 42, fb.get(a));
        assertEquals(42, fi.get(a));
        assertEquals("42", fs.get(a));
        fb.set(a, (byte) 41);
        fi.set(a, 41);
        fs.set(a, "41");
        assertEquals((byte) 41, fb.get(a));
        assertEquals(41, fi.get(a));
        assertEquals("41", fs.get(a));
    }
}

