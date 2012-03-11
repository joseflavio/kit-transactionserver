package com.kit.lightserver.services.db.logconnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fap.framework.db.InsertQueryPrinter;
import com.fap.framework.db.InsertQueryResult;
import com.fap.framework.db.KitDataSource;
import com.kit.lightserver.domain.types.ConnectionInfoVO;
import com.kit.lightserver.domain.types.InstallationIdSTY;

public final class LogConectionTask implements Runnable {

    static private final Logger LOGGER = LoggerFactory.getLogger(LogConectionTask.class);

    private final KitDataSource dataSource;

    private final ConnectionInfoVO connectionInfo;
    private final InstallationIdSTY installationId;
    private final String userClientId;
    private final int status;

    public LogConectionTask(
            final KitDataSource dataSource, final ConnectionInfoVO connectionInfo, final InstallationIdSTY installationId,
            final String userClientId, final int status) {

        this.dataSource = dataSource;
        this.connectionInfo = connectionInfo;
        this.installationId = installationId;
        this.userClientId = userClientId;
        this.status = status;

    }


    @Override
    public void run() {

        InsertLogConexoesQuery query = new InsertLogConexoesQuery(installationId, userClientId, status, connectionInfo);
        InsertQueryResult result = dataSource.executeInsertQuery(query);

        if (result.isQuerySuccessfullyExecuted() == false) {
            LOGGER.error("Unexpected error. result={}, query={}", result, new InsertQueryPrinter(query));
        }

    }

}// class
