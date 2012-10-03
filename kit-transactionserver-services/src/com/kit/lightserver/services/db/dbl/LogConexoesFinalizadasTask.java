package com.kit.lightserver.services.db.dbl;

import org.dajo.configuration.ConfigAccessor;
import org.dajo.framework.db.DatabaseConfig;
import org.dajo.framework.db.InsertQueryPrinter;
import org.dajo.framework.db.InsertQueryResult;
import org.dajo.framework.db.QueryExecutor;
import org.dajo.framework.db.SimpleQueryExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fap.thread.NamedRunnable;
import com.kit.lightserver.domain.types.ConnectionInfoVO;
import com.kit.lightserver.services.be.common.DatabaseAliases;

public final class LogConexoesFinalizadasTask implements NamedRunnable {

    static private final Logger LOGGER = LoggerFactory.getLogger(LogConexoesFinalizadasTask.class);

    private final QueryExecutor dataSource;

    private final String clientUserId;

    private final ConnectionInfoVO connectionInfo;

    public LogConexoesFinalizadasTask(final ConfigAccessor configAccessor, final String clientUserId, final ConnectionInfoVO connectionInfo) {

        final DatabaseConfig dblConfig = DatabaseConfig.getInstance(configAccessor, DatabaseAliases.DBL, false);

        this.dataSource = new SimpleQueryExecutor(dblConfig);
        this.clientUserId = clientUserId;
        this.connectionInfo = connectionInfo;

    }

    @Override
    public String getThreadNamePrefix() {
        return "T4";
    }

    @Override
    public void run() {

        final InsertLogConexoesFinalizadasQuery query = new InsertLogConexoesFinalizadasQuery(clientUserId, connectionInfo);
        final InsertQueryResult result = dataSource.executeInsertQuery(query);

        if (result.isInsertQuerySuccessfull() == false) {
            LOGGER.error("Unexpected error. result={}, query={}", result, new InsertQueryPrinter(query));
        }

    }

}// class
