package com.fap.framework.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

final class PreparedParametersUtil {

    static void fillParameters(final PreparedStatement st, final List<QueryParameter> queryParameterList) throws SQLException {

        int parameterIndex = 1;
        for (final QueryParameter queryParameter : queryParameterList) {

            if (queryParameter instanceof QueryBooleanParameter) {
                final QueryBooleanParameter parameter = (QueryBooleanParameter) queryParameter;
                final Boolean parameterValue = parameter.getParameterValue();
                st.setObject(parameterIndex, parameterValue);
            }
            else if (queryParameter instanceof QueryIntegerParameter) {
                final QueryIntegerParameter parameter = (QueryIntegerParameter) queryParameter;
                final Integer parameterValue = parameter.getParameterValue();
                if( parameterValue != null ) {
                    st.setInt(parameterIndex, parameterValue.intValue());
                }
                else {
                    st.setNull(parameterIndex, java.sql.Types.INTEGER);
                }
            }
            else if (queryParameter instanceof QueryIntParameter) {
                final QueryIntParameter parameter = (QueryIntParameter) queryParameter;
                final int parameterValue = parameter.getParameterValue();
                st.setInt(parameterIndex, parameterValue);
            }
            else if (queryParameter instanceof QueryStringParameter) {
                final QueryStringParameter parameter = (QueryStringParameter) queryParameter;
                final String parameterValue = parameter.getParameterValue();
                st.setString(parameterIndex, parameterValue);
            }
            else if (queryParameter instanceof QueryDateParameter) {
                final QueryDateParameter parameter = (QueryDateParameter) queryParameter;
                final Date parameterValue = parameter.getParameterValue();
                final  java.sql.Timestamp parameterSqlDate = new  java.sql.Timestamp(parameterValue.getTime());
                final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                st.setTimestamp(parameterIndex, parameterSqlDate, calendar);
            }
            else if (queryParameter instanceof QueryJodaDateTimeParameter) {
                final QueryJodaDateTimeParameter parameter = (QueryJodaDateTimeParameter) queryParameter;
                final DateTime dateTimeLocalTimeZone = parameter.getParameterValue();
                final DateTime dateTimeUTC = dateTimeLocalTimeZone.withZone(DateTimeZone.UTC);
                final Timestamp sqlDate = new Timestamp(dateTimeUTC.getMillis());
                final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                st.setTimestamp(parameterIndex, sqlDate, calendar);
            }
            else {
                throw new RuntimeException("Invalid Parameter. queryParameter=" + queryParameter);
            }

            ++parameterIndex;

        }// for

    }

}// class
