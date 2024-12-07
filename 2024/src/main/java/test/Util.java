package test;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public final class Util {
    private Util() {}

    public static  <V> V time(Callable<V> callable, String msg) throws Exception {
        long start = System.nanoTime();
        try {
            return callable.call();
        } finally {
            long end = System.nanoTime();
            System.out.println(msg + " took: " + TimeUnit.NANOSECONDS.toMillis(end - start) + " ms");
        }
    }

    public static void time(Runnable runnable, String msg) {
        long start = System.nanoTime();
        try {
            runnable.run();
        } finally {
            long end = System.nanoTime();
            System.out.println(msg + " took: " + TimeUnit.NANOSECONDS.toMillis(end - start) + " ms");
        }
    }
}
