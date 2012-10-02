package com.fap.framework.db;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.dajo.framework.db.DatabaseConfig;
import org.dajo.framework.db.SelectQueryInterface;
import org.dajo.framework.db.SelectQueryResult;
import org.dajo.framework.db.SelectQueryResultAdapter;
import org.dajo.framework.db.SingleConnectionQueryExecutor;

public class DbConnectionPool {

    private final List<DbPooledConnection> connections = new ArrayList<DbPooledConnection>();
    private final DatabaseConfig dbConfig;

    public DbConnectionPool(final DatabaseConfig dbConfig, final int poolSize) {

        this.dbConfig = dbConfig;

        for(int i=0; i < poolSize; ++i) {
            final DbPooledConnection c = new DbPooledConnection("c"+i);
            connections.add(c);
        }

    }

    private final Lock lock = new ReentrantLock(true);

    public DbPooledConnection getAvailableConnection() {

        lock.lock();

        try {

            DbPooledConnection selected = null;
            do {
                for(DbPooledConnection currentConnection : connections) {
                    if (currentConnection.internalTryToGet() == true) {
                        selected = currentConnection;
                        break;
                    }
                }
            } while (selected == null);

            return selected;

        }
        finally {
            lock.unlock();
        }

    }

    public <T> SelectQueryResult<T> executeSelectQuery(final SelectQueryInterface selectQuery, final SelectQueryResultAdapter<T> resultAdapter) {
        SingleConnectionQueryExecutor simpleQueryExecutor = new SingleConnectionQueryExecutor(dbConfig);
        final SelectQueryResult<T> result = simpleQueryExecutor.executeSelectQuery(selectQuery, resultAdapter);
        simpleQueryExecutor.close();
        return result;
    }

}// class
