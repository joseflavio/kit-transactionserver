package com.fap.framework.db;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DbConnectionPool {

    public DbConnectionPool(final DatabaseConfig dbConfig) {
        // TODO Auto-generated constructor stub
    }

    private final ConnectionLock c1 = new ConnectionLock("c1");
    private final ConnectionLock c2 = new ConnectionLock("c2");

    private final Lock lock = new ReentrantLock(true);

    public DbPooledConnection getAvailableConnection() {

        lock.lock();

        try {

            ConnectionLock selected = null;
            while (true) {
                System.out.print(".");
                if (c1.isAvailable() == true) {
                    selected = c1;
                    break;
                }

                if (c2.isAvailable() == true) {
                    selected = c2;
                    break;
                }

            }

            return selected.getConnection();

        }
        finally {
            lock.unlock();
        }

    }

    static final class ConnectionLock {

        private final DbPooledConnection connection;
        private final Lock lock = new ReentrantLock();

        public ConnectionLock(final String name) {
            this.connection = new DbPooledConnection(name);
        }

        public boolean isAvailable() {
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

        public DbPooledConnection getConnection() {
            return connection;
        }
    }// class

}// class
