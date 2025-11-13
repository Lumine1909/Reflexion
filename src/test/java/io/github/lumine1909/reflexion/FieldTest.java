package io.github.lumine1909.reflexion;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FieldTest {

    @Test
    public void testField() {
        A a = new A();
        Field<Byte> fb = Field.of(A.class, "b", byte.class);
        Field<Integer> fi = Field.of(A.class, "i", int.class);
        Field<String> fs = Field.of(A.class, "str", String.class);
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

