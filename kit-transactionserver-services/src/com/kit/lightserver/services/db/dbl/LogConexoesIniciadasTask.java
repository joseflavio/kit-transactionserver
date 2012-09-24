package com.kit.lightserver.services.db.dbl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.dajo.framework.configuration.ConfigAccessor;
import org.dajo.framework.db.DatabaseConfig;
import org.dajo.framework.db.InsertQueryPrinter;
import org.dajo.framework.db.InsertQueryResult;
import org.dajo.framework.db.QueryExecutor;
import org.dajo.framework.db.SimpleQueryExecutor;

import com.fap.thread.NamedRunnable;

import com.kit.lightserver.domain.types.ConnectionInfoVO;
import com.kit.lightserver.domain.types.InstallationIdAbVO;
import com.kit.lightserver.services.be.common.DatabaseAliases;

public final class LogConexoesIniciadasTask implements NamedRunnable {

    static private final Logger LOGGER = LoggerFactory.getLogger(LogConexoesIniciadasTask.class);

    private final QueryExecutor queryExecutor;

    private final ConnectionInfoVO connectionInfo;
    private final InstallationIdAbVO installationId;
    private final String clientUserId;
    private final int status;

    public LogConexoesIniciadasTask(
            final ConfigAccessor configAccessor, final ConnectionInfoVO connectionInfo, final InstallationIdAbVO installationId,
            final String clientUserId, final int status) {

        final DatabaseConfig dblConfig = DatabaseConfig.getInstance(configAccessor, DatabaseAliases.DBL, false);

        this.queryExecutor = new SimpleQueryExecutor(dblConfig);
        this.connectionInfo = connectionInfo;
        this.installationId = installationId;
        this.clientUserId = clientUserId;
        this.status = status;

    }

    @Override
    public String getThreadNamePrefix() {
        return "T3";
    }

    @Override
    public void run() {

        final InsertLogConexoesIniciadasQuery query = new InsertLogConexoesIniciadasQuery(installationId, clientUserId, status, connectionInfo);
        final InsertQueryResult result = queryExecutor.executeInsertQuery(query);

        if (result.isInsertQuerySuccessfull() == false) {
            LOGGER.error("Unexpected error. result={}, query={}", result, new InsertQueryPrinter(query));
        }

    }


}// class
