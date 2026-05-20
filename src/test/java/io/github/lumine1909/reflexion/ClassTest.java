package io.github.lumine1909.reflexion;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClassTest {

    @Test
    public void testNew() {
        Class<?> class$A = Class.forName("io.github.lumine1909.reflexion.A", 1);
        assertNotNull(class$A);
        A a = (A) class$A.newInstance();
        Field<String> fs = class$A.getField("str");
        Field<Byte> fb = class$A.getField("b");
        assertNull(fs.get(a));
        assertEquals((byte) 0, fb.get(a));
    }
}
