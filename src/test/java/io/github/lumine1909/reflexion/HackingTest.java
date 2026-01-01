package io.github.lumine1909.reflexion;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

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
}