package com.kit.util.dbconnectiontest;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.dajo.chronometer.Chronometer;
import org.dajo.framework.configuration.ConfigAccessor;
import org.dajo.framework.configuration.ConfigurationReader;
import org.dajo.framework.db.DatabaseConfig;
import org.dajo.framework.db.QueryExecutor;
import org.dajo.framework.db.QueryParameter;
import org.dajo.framework.db.SelectQueryInterface;
import org.dajo.framework.db.SelectQueryResult;
import org.dajo.framework.db.SelectQueryResultAdapter;
import org.dajo.framework.db.SimpleQueryExecutor;

public class TestDabaseConnectionPool {

    static public void main(final String[] args) throws ClassNotFoundException, SQLException {

        ConfigAccessor configAccessor = ConfigurationReader.loadExternalProperties("config/database.joseflavio-mira-srvkit-tinet.properties");
        DatabaseConfig dbConfig = DatabaseConfig.getInstance(configAccessor, true);

        QueryExecutor dataSource = new SimpleQueryExecutor(dbConfig);

        List<Thread> threads = new LinkedList<Thread>();
        for (int i = 0; i < 3; ++i) { // 8 seems to be the magical number
            Thread t = new Thread(new TestPoolThread(dataSource));
            threads.add(t);
        }

        Chronometer totalTime = new Chronometer("Total time");
        totalTime.start();

        for (Thread t : threads) {
            t.start();
        }

        boolean allFinished;
        do {
            allFinished = true;
            for (Thread t : threads) {
                if (t.isAlive()) {
                    allFinished = false;
                }
            }
        } while (allFinished == false);

        totalTime.stop();

    }

    static final class TestPoolThread implements Runnable {

        private final SelectUsers selectUsers = new SelectUsers();

        private final SelectUsersAdapter selectUsersAdapter = new SelectUsersAdapter();

        private final QueryExecutor dataSource;

        public TestPoolThread(final QueryExecutor dataSource) {
            this.dataSource = dataSource;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(4000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }

            SelectQueryResult<List<String>> result = dataSource.executeSelectQuery(selectUsers, selectUsersAdapter);

            System.out.println("result="+result);


        }
    }

}

final class SelectUsersAdapter implements SelectQueryResultAdapter<List<String>> {

    @Override
    public List<String> adaptResultSet(final ResultSet rs) throws SQLException {
        List<String> result = new LinkedList<String>();
        while(rs.next()) {
            String clientId = rs.getString("KTClientId");
            result.add(clientId);
        }
        return result;
    }

}

final class SelectUsers implements SelectQueryInterface {

    @Override
    public String getPreparedSelectQueryString() {
        return "SELECT KTClientId, KTPassword, name, KTClientStatus FROM dbo.Authenticate";
    }

    @Override
    public List<QueryParameter> getSelectQueryParameters() {
        return new LinkedList<QueryParameter>();
    }

}// class

