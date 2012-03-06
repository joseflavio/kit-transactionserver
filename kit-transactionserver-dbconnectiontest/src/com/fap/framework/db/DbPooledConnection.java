package com.fap.framework.db;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class DbPooledConnection {

    private final String name;

    private final Lock lock = new ReentrantLock();

    public DbPooledConnection(final String name) {
        this.name = name;
    }

    public boolean internalTryToGet() {
        try {
            boolean locked = lock.tryLock(100, TimeUnit.MILLISECONDS);
            System.out.println("locked="+locked);
            return locked;
        }
        catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    public void release() {
        lock.unlock();
    }

    @Override
    public String toString() {
        return "DbPooledConnection [name=" + name + "]";
    }

}// class
