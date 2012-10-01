package com.kit.lightserver.services.be.gps;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.dajo.chronometer.Chronometer7;
import org.dajo.chronometer.Chronometer7.ChronometerResource;
import org.dajo.framework.configuration.ConfigAccessor;
import org.dajo.framework.db.BatchInsertQueryParameters;
import org.dajo.framework.db.BatchInsertQueryResult;
import org.dajo.framework.db.DatabaseConfig;
import org.dajo.framework.db.SingleConnectionQueryExecutor7;

import com.kit.lightserver.domain.types.ConnectionInfoVO;
import com.kit.lightserver.domain.types.CoordenadaGpsSTY;
import com.kit.lightserver.domain.types.InstallationIdAbVO;
import com.kit.lightserver.services.be.common.DatabaseAliases;

public class GpsService {

    static private final Logger LOGGER = LoggerFactory.getLogger(GpsService.class);

    static private ExecutorService pool = Executors.newCachedThreadPool();

    static public GpsService getInstance(final ConfigAccessor configAccessor) {
        return new GpsService(configAccessor);
    }

    //private final ConfigAccessor configAccessor;
    private final DatabaseConfig dbgConfig;

    private GpsService(final ConfigAccessor configAccessor) {
        //this.configAccessor = configAccessor;
        this.dbgConfig = DatabaseConfig.getInstance(configAccessor, DatabaseAliases.DBG, true);
    }

    public void logGpsActivities(final ConnectionInfoVO connectionInfo, final String clientUserId, final InstallationIdAbVO installationId,
            final List<CoordenadaGpsSTY> coordenadasReceived) {
        LogGpsActivitiesTask logGpsActivitiesTask = new LogGpsActivitiesTask(dbgConfig, connectionInfo, clientUserId, installationId, coordenadasReceived);
        pool.execute(logGpsActivitiesTask);
    }

    static final class LogGpsActivitiesTask implements Runnable {

        private final DatabaseConfig dbgConfig;
        private final ConnectionInfoVO connectionInfo;
        private final String clientUserId;
        private final InstallationIdAbVO installationId;
        private final List<CoordenadaGpsSTY> coordenadasReceived;

        public LogGpsActivitiesTask(final DatabaseConfig dbgConfig, final ConnectionInfoVO connectionInfo, final String clientUserId,
                final InstallationIdAbVO installationId, final List<CoordenadaGpsSTY> coordenadasReceived) {

            this.dbgConfig = dbgConfig;
            this.connectionInfo = connectionInfo;
            this.clientUserId = clientUserId;
            this.installationId = installationId;
            this.coordenadasReceived = coordenadasReceived;
        }

        @Override
        public void run() {
            LOGGER.info("LogGpsActivitiesTask - started");
            Chronometer7 serviceChrono = new Chronometer7("GpsService.logGpsActivities");
            try( ChronometerResource oi = serviceChrono.getAsResource() ) {
                serviceChrono.start();

                final boolean gpsDataAvailable = true;
                List<BatchInsertQueryParameters> paramList = new LinkedList<>();
                for(CoordenadaGpsSTY coordenadaGps:coordenadasReceived) {
                    BatchInsertQueryParameters params =
                            new BatchInsertActivityGpsHistoryParams(installationId, clientUserId, connectionInfo, gpsDataAvailable, coordenadaGps);
                    paramList.add(params);
                }

                try( SingleConnectionQueryExecutor7 dbgQueryExecutor = new SingleConnectionQueryExecutor7(dbgConfig) ) {
                    BatchInsertActivityGpsHistoryQuery batchInsertQuery = new BatchInsertActivityGpsHistoryQuery(paramList);
                    BatchInsertQueryResult insertResult = dbgQueryExecutor.executeBatchInsertQuery(batchInsertQuery);
                    LOGGER.info("insertResult=" + insertResult);
                }
            }

            LOGGER.info(serviceChrono.toString(coordenadasReceived.size()));

        }
    }

}// class
