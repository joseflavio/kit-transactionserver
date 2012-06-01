package com.kit.lightserver.services.db.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fap.framework.db.InsertQueryPrinter;
import com.fap.framework.db.InsertQueryResult;
import com.fap.framework.db.QueryExecutor;
import com.fap.thread.NamedRunnable;

public final class LogConexoesFinalizadasTask implements NamedRunnable {

    static private final Logger LOGGER = LoggerFactory.getLogger(LogConexoesFinalizadasTask.class);

    private final QueryExecutor dataSource;

    private final String clientUserId;

    public LogConexoesFinalizadasTask(final QueryExecutor dataSource, final String clientUserId) {

        this.dataSource = dataSource;
        this.clientUserId = clientUserId;

    }

    @Override
    public String getThreadNamePrefix() {
        return "T4";
    }

    @Override
    public void run() {

        final InsertLogConexoesFinalizadasQuery query = new InsertLogConexoesFinalizadasQuery(clientUserId);
        final InsertQueryResult result = dataSource.executeInsertQuery(query);

        if (result.isInsertQuerySuccessfull() == false) {
            LOGGER.error("Unexpected error. result={}, query={}", result, new InsertQueryPrinter(query));
        }

    }

}// class
