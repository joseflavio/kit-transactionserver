package com.kit.lightserver.services.db.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfap.chronometer.Chronometer;
import com.kit.lightserver.services.db.InsertQueryInterface;
import com.kit.lightserver.services.db.InsertQueryResult;
import com.kit.lightserver.services.db.QueryPrinter;
import com.kit.lightserver.services.db.SelectQueryInterface;
import com.kit.lightserver.services.db.SelectQueryResult;
import com.kit.lightserver.services.db.UpdateQueryInterface;
import com.kit.lightserver.services.db.UpdateQueryResult;

public final class DatabaseLogger {

    static private final Logger LOGGER = LoggerFactory.getLogger(DatabaseLogger.class);

    static public void logConnectionOpen(final int openConnectionsCount) {
        LOGGER.trace("[DB CONNE. OPENED] [openConnectionsCount="+openConnectionsCount+"]");
    }

    static public void logConnectionClosed(final int openConnectionsCount) {
        LOGGER.trace("[DB CONNE. CLOSED] [openConnectionsCount="+openConnectionsCount+"]");
    }

    static public void logSelectQuery(final SelectQueryInterface selectQuery) {
        final String queryName = selectQuery.getClass().getCanonicalName();
        final String printedSelectQuery = QueryPrinter.printSelectQuery(selectQuery);
        LOGGER.trace("[DB SELECT QUERY ] [queryName="+ queryName + "] query=" + printedSelectQuery);
    }

    static public void logUpdateQuery(final UpdateQueryInterface updateQuery) {
        final String queryName = updateQuery.getClass().getCanonicalName();
        final String printedSelectQuery = QueryPrinter.printUpdateQuery(updateQuery);
        LOGGER.trace("[DB UPDATE QUERY ] [queryName="+ queryName + "] query=" + printedSelectQuery);
    }

    static public void logInsertQuery(final InsertQueryInterface insertQuery) {
        final String queryName = insertQuery.getClass().getCanonicalName();
        final String printedSelectQuery = QueryPrinter.printInsertQuery(insertQuery);
        LOGGER.trace("[DB INSERT QUERY ] [queryName="+ queryName + "] query=" + printedSelectQuery);
    }

    static public void logUpdateResult(final Chronometer chronometer, final UpdateQueryResult result) {
        LOGGER.trace("[DB UPDATE RESULT] " + chronometer.getLogString() + ", result="+result);
    }

    static public void logSelectResult(final Chronometer chronometer, final SelectQueryResult<?> result) {
        LOGGER.trace("[DB SELECT RESULT] " + chronometer.getLogString() + ", result="+result);
    }

    static public void logInsertResult(final Chronometer chronometer, final InsertQueryResult result) {
        LOGGER.trace("[DB INSERT RESULT] " + chronometer.getLogString() + ", result="+result);
    }

}// class
