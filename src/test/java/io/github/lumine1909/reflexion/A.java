package io.github.lumine1909.reflexion;

public class A {

    private static final int i = 42;
    private final byte b = 42;
    private String str = "42";

    A() {
    }

    A(int a, String b) {
    }

    private static int testStatic0() {
        return 42;
    }

    private static void testStatic4(int arg0, int arg1, int arg2, Object arg3) {
    }

    public static void testStatic5(int arg0, int arg1, int arg2, int arg3, Object arg4) {
    }

    private void test0() {
    }

    private int test4(int arg0, int arg1, int arg2, Object arg3) {
        return 42;
    }

    public void test5(int arg0, int arg1, int arg2, int arg3, Object arg4) {
    }

    private String getStr() {
        return str;
    }

    private void setStr(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return "A";
    }

    private static class B extends A {

        private final String str = "str";

        @Override
        public String toString() {
            return "B";
        }
    }

    record R(String str) {

    }
}