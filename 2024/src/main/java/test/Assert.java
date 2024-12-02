package test;


public class Assert {

    public static void assertTrue(boolean b) {
      if (!b) {
        throw new AssertionError();
      }
    }

    public static void assertTrue(boolean b, String msg) {
        if (!b) {
            throw new AssertionError(msg);
        }
    }
}
