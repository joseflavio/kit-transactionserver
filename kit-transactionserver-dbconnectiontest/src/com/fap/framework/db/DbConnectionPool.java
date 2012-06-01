package com.fap.framework.db;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
        final Connection connection = DatabaseConnectionUtil.getInstance().getConnection(dbConfig);
        if (connection == null) {
            return new SelectQueryResult<T>();
        }
        final SelectQueryResult<T> result = SelectQueryExecuter.executeSelectQuery(connection, selectQuery, resultAdapter);
        DatabaseConnectionUtil.getInstance().closeConnection(connection);
        return result;
    }

}// class
