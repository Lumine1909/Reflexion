package io.github.lumine1909.reflexion;

public class A {

    private final byte b = 42;
    private final int i = 42;
    private String str = "42";

    private String getStr() {
        return str;
    }

    private void setStr(String str) {
        this.str = str;
    }

    private static int test() {
        return 42;
    }

    record R(String str) {
    }
}