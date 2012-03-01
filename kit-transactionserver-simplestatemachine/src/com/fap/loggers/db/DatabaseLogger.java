package com.fap.loggers.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fap.chronometer.Chronometer;
import com.fap.framework.db.InsertQueryInterface;
import com.fap.framework.db.InsertQueryPrinter;
import com.fap.framework.db.InsertQueryResult;
import com.fap.framework.db.SelectQueryInterface;
import com.fap.framework.db.SelectQueryPrinter;
import com.fap.framework.db.SelectQueryResult;
import com.fap.framework.db.UpdateQueryInterface;
import com.fap.framework.db.UpdateQueryPrinter;
import com.fap.framework.db.UpdateQueryResult;

public final class DatabaseLogger {

    static private final Logger LOGGER = LoggerFactory.getLogger(DatabaseLogger.class);

    static public void logConnectionOpen(final int openConnectionsCount) {
        LOGGER.trace("[DB CONNE. OPENED] [openConnectionsCount={}]", Integer.valueOf(openConnectionsCount));
    }

    static public void logConnectionClosed(final int openConnectionsCount) {
        LOGGER.trace("[DB CONNE. CLOSED] [openConnectionsCount={}]", Integer.valueOf(openConnectionsCount));
    }

    static public void logSelectQuery(final SelectQueryInterface query) {
        LOGGER.trace("[DB SELECT QUERY ] selectQuery={}", new SelectQueryPrinter(query));
    }

    static public void logUpdateQuery(final UpdateQueryInterface query) {
        LOGGER.trace("[DB UPDATE QUERY ] updateQuery={}", new UpdateQueryPrinter(query));
    }

    static public void logInsertQuery(final InsertQueryInterface query) {
        LOGGER.trace("[DB INSERT QUERY ] insertQuery={}", new InsertQueryPrinter(query));
    }

    static public void logUpdateResult(final Chronometer chronometer, final UpdateQueryResult result) {
        LOGGER.trace("[DB UPDATE RESULT] chronometer={}, result={}", chronometer, result);
    }

    static public void logSelectResult(final Chronometer chronometer, final SelectQueryResult<?> result) {
        LOGGER.trace("[DB SELECT RESULT] chronometer={}, result={}", chronometer, result);
    }

    static public void logInsertResult(final Chronometer chronometer, final InsertQueryResult result) {
        LOGGER.trace("[DB INSERT RESULT] chronometer={}, result={}", chronometer, result);
    }

}// class
