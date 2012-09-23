package main;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.dajo.framework.configuration.SimpleConfigAccessor;
import org.dajo.framework.db.DatabaseConfig;
import org.dajo.framework.db.QueryParameter;
import org.dajo.framework.db.SelectQueryInterface;
import org.dajo.framework.db.SelectQueryResult;
import org.dajo.framework.db.SelectQueryResultAdapter;
import org.dajo.framework.db.SimpleQueryExecutor;

public class CommandLineSelect {

    /**
     * @param args
     */
    public static void main(final String[] args) {

        String connectionStr = args[0]; // sa/chicabom2012!@107.22.162.94:1433/KEEPIN_V01_DEMO01
        System.out.println("connectionStr="+connectionStr);

        String selectQueryStr = args[1];
        System.out.println("selectQueryStr="+selectQueryStr);

        /*
         * Build the configuration
         */
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("database.default.host", "107.22.162.94");
        map.put("database.default.port", "1433");
        map.put("database.default.name", "KEEPIN_V01_DEMO01");
        map.put("database.default.user", "sa");
        map.put("database.default.password", "chicabom2012!");
        SimpleConfigAccessor configAccessor = SimpleConfigAccessor.getInstance(map);
        DatabaseConfig dbConfig = DatabaseConfig.getInstance(configAccessor, true);
        SimpleQueryExecutor executor = new SimpleQueryExecutor(dbConfig);

        PlainSelectQuery plainSelectQuery = new PlainSelectQuery(selectQueryStr);
        SelectQueryResult<List<String>> queryResult = executor.executeSelectQuery(plainSelectQuery, new PlainSelectQueryAdapter() );

        if( queryResult.isSelectQuerySuccessful() == false ) {
            System.err.println("Query failed.");
        }
        else {
            System.out.println("Query successfully executed. result:");
            List<String> result = queryResult.getResult();
            int i = 1;
            for (String currentLine : result) {
                System.out.println(i +". " + currentLine);
                ++i;
            }
        }



    }

    static class PlainSelectQuery implements SelectQueryInterface {
        private final String selectQuery;
        PlainSelectQuery(final String queryText) {
            this.selectQuery = queryText;
        }
        @Override
        public String getPreparedSelectQueryString() {
            return selectQuery;
        }
        @Override
        public List<QueryParameter> getSelectQueryParameters() {
            return new ArrayList<QueryParameter>();
        }
    }

    static class PlainSelectQueryAdapter implements SelectQueryResultAdapter<List<String>> {
        @Override
        public List<String> adaptResultSet(final ResultSet rs) throws SQLException {
            List<String> result = new LinkedList<String>();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columns = rsmd.getColumnCount();
            while( rs.next() ) {
               String currentLine = "";
               for (int i = 0; i < columns; i++) {
                   String currentColumn = rs.getString(i);
                   currentLine += currentColumn + " | ";
               }
               result.add(currentLine);
            }
            return result;
        }

    }

}

