package io.github.lumine1909.reflexion;

import io.github.lumine1909.reflexion.internal.UnsafeUtil;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.function.IntSupplier;

import static io.github.lumine1909.reflexion.internal.UnsafeUtil.INTERNAL_UNSAFE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    public void testHackHidden() {
        int i = 42;
        IntSupplier supplier = () -> i;
        assertEquals(42, supplier.getAsInt());
        Field<Integer> field$arg$1 = Field.of(supplier.getClass(), "arg$1");
        field$arg$1.set(supplier, 43);
        assertEquals(43, supplier.getAsInt());
    }

    @SuppressWarnings("removal")
    @Test
    public void testHackInternalFields() {
        Field<?> field$fieldFilterMap = Field.of("jdk.internal.reflect.Reflection", "fieldFilterMap");
        assertNotNull(field$fieldFilterMap.getObject(A.class));

        Field<ClassLoader> field$classLoader = Field.of(java.lang.Class.class, "classLoader");
        assertNotNull(field$classLoader.getObject(A.class));
    }

    @Test
    public void testInternalUnsafe() {
        Class<?> class$InternalUnsafe = Class.forName("jdk.internal.misc.Unsafe");
        Method<Object> method$staticFieldBase = Method.of(class$InternalUnsafe.javaClass(), "staticFieldBase", Object.class, java.lang.reflect.Field.class);
        Object base = method$staticFieldBase.invoke(INTERNAL_UNSAFE, Field.of(A.class, "i").javaField());
        assertEquals(A.class, base);
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void testFilteredField() {
        UnsafeUtil.clearReflectionFilter();
        Class<?> class$Reflection = Class.forName("jdk.internal.reflect.Reflection");
        Field<Map> f = class$Reflection.getField("fieldFilterMap");
        assertEquals(0, f.get(null).size());
    }
}