package com.kit.lightserver.services.be.gps;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.dajo.framework.configuration.ConfigAccessor;
import org.dajo.framework.db.DatabaseConfig;
import org.dajo.framework.db.InsertQueryResult;
import org.dajo.framework.db.SingleConnectionQueryExecutor;

import com.kit.lightserver.domain.types.ConnectionInfoVO;
import com.kit.lightserver.domain.types.CoordenadaGpsSTY;
import com.kit.lightserver.domain.types.InstallationIdAbVO;
import com.kit.lightserver.services.be.common.DatabaseAliases;

public class GpsService {

    static private final Logger LOGGER = LoggerFactory.getLogger(GpsService.class);

    static public GpsService getInstance(final ConfigAccessor configAccessor) {
        return new GpsService(configAccessor);
    }

    private final ConfigAccessor configAccessor;
    private final DatabaseConfig dbgConfig;

    private GpsService(final ConfigAccessor configAccessor) {
        this.configAccessor = configAccessor;
        this.dbgConfig = DatabaseConfig.getInstance(configAccessor, DatabaseAliases.DBG, true);
    }

    public void logGpsActivities(final ConnectionInfoVO connectionInfo, final String clientUserId, final InstallationIdAbVO installationId,
            final List<CoordenadaGpsSTY> coordenadasReceived) {

        SingleConnectionQueryExecutor dbgQueryExecutor = new SingleConnectionQueryExecutor(dbgConfig);

        boolean gpsDataAvailable = true;
        for(CoordenadaGpsSTY coordenadaGps:coordenadasReceived) {
            InsertActivityGpsHistoryQuery insertQuery = new InsertActivityGpsHistoryQuery(installationId, clientUserId, connectionInfo, gpsDataAvailable, coordenadaGps);
            InsertQueryResult insertResult = dbgQueryExecutor.executeInsertQuery(insertQuery);
            LOGGER.info("insertResult=" + insertResult);
        }


    }

}// class
