package com.kit.lightserver.services.db.authenticate;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.kit.lightserver.services.db.InsertQueryExecuter;
import com.kit.lightserver.services.db.InsertQueryResult;
import com.kit.lightserver.services.types.ConnectionId;
import com.kit.lightserver.services.types.InstallationIdSTY;

public final class TableLogConexoesOperations {

    static private final DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS (ZZ)"); // Nao deve ser usado com LocalDate pois tem hora

    static public InsertQueryResult registerConnection(final ConnectionId connectionId, final InstallationIdSTY installationIdSTY, final String ktClientId,
            final int status) {

        final DateTime dateTime = new DateTime();

        final int year = dateTime.getYear();
        final int month = dateTime.getMonthOfYear();
        final int day = dateTime.getDayOfMonth();

        final String dateTimeStr = fmt.print(dateTime);

        final InsertLogConexoesQuery insertQuery = new InsertLogConexoesQuery(connectionId, installationIdSTY, ktClientId, status, year, month,
                day, dateTime,
                dateTimeStr);

        final InsertQueryResult result = InsertQueryExecuter.executeInsertQuery(insertQuery);

        return result;

    }

}// class
