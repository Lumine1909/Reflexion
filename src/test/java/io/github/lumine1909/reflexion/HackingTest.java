package io.github.lumine1909.reflexion;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static io.github.lumine1909.reflexion.internal.UnsafeUtil.INTERNAL_UNSAFE;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HackingTest {


    @Test
    public void testHackRecord() {
        Field<String> field$str = Field.of(A.R.class, "str");
        A.R r = new A.R("test");
        assertEquals("test", r.str());
        field$str.set(r, "hacked");
        assertEquals("hacked", r.str());
    }

    @Test
    public void testHackString() {
        String str = "testString";
        Field<byte[]> field$value = Field.of(String.class, "value");
        assertEquals("testString", str);
        field$value.set(str, "hackedString".getBytes(StandardCharsets.UTF_8));
        assertEquals("hackedString", str);
    }

    @Test
    public void testInternalUnsafe() {
        Class<?> class$InternalUnsafe = Class.forName("jdk.internal.misc.Unsafe");
        Method<Object> method$staticFieldBase = Method.of(class$InternalUnsafe.javaClass(), "staticFieldBase", Object.class, java.lang.reflect.Field.class);
        Object base = method$staticFieldBase.invoke(INTERNAL_UNSAFE, Field.of(A.class, "i").javaField());
        assertEquals(A.class, base);
    }
}