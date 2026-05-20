package io.github.lumine1909.reflexion;

public class A {

    private static final int i = 42;
    private final byte b = 42;
    private String str = "42";

    A() {
    }

    A(int a, String b) {

    }

    private static int test() {
        return 42;
    }

    public static void testStatic(int arg1, int arg2, int arg3, int arg4, int arg5, Object arg6) {
    }

    private String getStr() {
        return str;
    }

    private void setStr(String str) {
        this.str = str;
    }

    public void test(int arg1, int arg2, int arg3, int arg4, int arg5, Object arg6) {
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