package com.kevin_mic.aqua.service.gpio;


import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

public class PinLock {
    private static Lock lock = new ReentrantLock();

    public static <T> T lockSupplier(Supplier<T> supplier) {
        try {
            lock.lock();
            return supplier.get();
        }
        finally {
            lock.unlock();
        }
    }
}
