package com.kit.lightserver.services.db.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fap.framework.db.InsertQueryPrinter;
import com.fap.framework.db.InsertQueryResult;
import com.fap.framework.db.KitDataSource;
import com.fap.thread.NamedRunnable;
import com.kit.lightserver.domain.types.ConnectionInfoVO;
import com.kit.lightserver.domain.types.InstallationIdAbVO;

public final class LogConexoesIniciadasTask implements NamedRunnable {

    static private final Logger LOGGER = LoggerFactory.getLogger(LogConexoesIniciadasTask.class);

    private final KitDataSource dataSource;

    private final ConnectionInfoVO connectionInfo;
    private final InstallationIdAbVO installationId;
    private final String clientUserId;
    private final int status;

    public LogConexoesIniciadasTask(
            final KitDataSource dataSource, final ConnectionInfoVO connectionInfo, final InstallationIdAbVO installationId,
            final String clientUserId, final int status) {

        this.dataSource = dataSource;
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
        final InsertQueryResult result = dataSource.executeInsertQuery(query);

        if (result.isInsertQuerySuccessfull() == false) {
            LOGGER.error("Unexpected error. result={}, query={}", result, new InsertQueryPrinter(query));
        }

    }


}// class
