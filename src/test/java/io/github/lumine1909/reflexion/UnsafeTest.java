package io.github.lumine1909.reflexion;

import io.github.lumine1909.reflexion.internal.UnsafeUtil;
import org.junit.jupiter.api.Test;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

public class UnsafeTest {

    @SuppressWarnings({"deprecation", "DataFlowIssue"})
    @Test
    public void testMissingUnsafe() throws Throwable {
        Field field$unsafe = Unsafe.class.getDeclaredField("theUnsafe");
        field$unsafe.setAccessible(true);
        Unsafe unsafe = (Unsafe) field$unsafe.get(null);

        Field f = UnsafeUtil.class.getDeclaredField("UNSAFE");
        unsafe.putObject(unsafe.staticFieldBase(f), unsafe.staticFieldOffset(f), null);
        assertNull(UnsafeUtil.UNSAFE);

        UnsafeUtil.put(unsafe, null, null, null, null, null, null);
        assertNotNull(UnsafeUtil.UNSAFE);
    }
}
