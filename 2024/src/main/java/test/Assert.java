package test;

public final class Assert {
  private Assert() {}

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

  public static void assertEquals(long expected, long actual) {
    assertEquals(expected, actual, "");
  }

  public static void assertEquals(long expected, long actual, String part) {
    if (expected != actual) {
      throw new AssertionError(part + ": Expected " + expected + " but was " + actual);
    }
  }
}
