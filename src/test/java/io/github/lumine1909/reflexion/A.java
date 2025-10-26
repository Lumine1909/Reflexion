package io.github.lumine1909.reflexion;

public class A {

    private final byte b = 42;
    private final int i = 42;
    private String str = "42";

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public static int test() {
        return 42;
    }
}