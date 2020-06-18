package com.singularitycoder.folkdatabase;

import java.util.concurrent.Callable;

import static org.junit.Assert.fail;

public abstract class Stall {

    private static final long TIME_SLICE = 50;
    private long mTimeout = 3000;

    public Stall() {
    }

    public Stall(long timeout) {
        mTimeout = timeout;
    }

    protected abstract boolean check();

    public static void check(CharSequence message, long timeout, Callable<Boolean> condition) throws Exception {
        while (timeout > 0) {
            if (condition.call()) return;
            Thread.sleep(TIME_SLICE);
            timeout -= TIME_SLICE;
        }
        fail(message.toString());
    }

    public void run() {
        if (check()) return;
        long timeout = mTimeout;
        while (timeout > 0) {
            try {
                Thread.sleep(TIME_SLICE);
            } catch (InterruptedException e) {
                fail("unexpected InterruptedException");
            }
            if (check()) return;
            timeout -= TIME_SLICE;
        }
        fail("unexpected timeout");
    }
}