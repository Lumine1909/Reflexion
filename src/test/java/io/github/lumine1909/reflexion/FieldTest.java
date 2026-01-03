package io.github.lumine1909.reflexion;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FieldTest {

    @Test
    public void testField() {
        A a = new A();
        Field<Byte> fb = Field.of("io.github.lumine1909.reflexion.A", "b", byte.class);
        Field<Integer> fi = Field.of(A.class, "i");
        Field<String> fs = Field.of(A.class, "str", String.class);
        assertEquals((byte) 42, fb.get(a));
        assertEquals(42, fi.get(null));
        assertEquals("42", fs.get(a));
        fb.set(a, (byte) 41);
        fb.setFast(a, (byte) 41);
        fi.set(null, 41);
        fs.set(a, "41");
        fs.setFast(a, "41");
        assertEquals((byte) 41, fb.get(a));
        assertEquals((byte) 41, fb.getFast(a));
        assertEquals(41, fi.get(null));
        assertEquals(41, fi.getFast(null));
        assertEquals("41", fs.get(a));
        assertEquals("41", fs.getFast(a));
    }
}

