package main;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.fap.framework.db.DatabaseConfig;
import com.jfap.framework.configuration.ConfigAccessor;
import com.jfap.framework.configuration.ConfigurationReader;

public final class TestDatabaseConnection {

    static public void main(final String[] args) throws ClassNotFoundException, SQLException {

	    final String driverClassName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        Class.forName(driverClassName);
        System.out.println("JDBC Driver loaded: " + driverClassName);

		ConfigAccessor configAccessor = ConfigurationReader.getConfiguration("config/database.joseflavio-mira-srvkit-tinet.properties");
		DatabaseConfig dbConfig = DatabaseConfig.getInstance(configAccessor);
		System.out.println(dbConfig);

		List<Thread> threads = new LinkedList<Thread>();
		for(int i=0; i < 60; ++i) { // 8 seems to be the magical number
		    Thread t = new Thread(new TestDatabaseConnectionThread(dbConfig));
		    threads.add(t);
		}

		for (Thread t : threads) {
		    t.start();
        }

	}

    static final class TestDatabaseConnectionThread implements Runnable {

        private final DatabaseConfig dbConfig;

        public TestDatabaseConnectionThread(final DatabaseConfig dbConfig) {
            this.dbConfig = dbConfig;
        }

        @Override
        public void run() {
            try {
                SampleQuery.executeSampleQuery(dbConfig);
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

}// class
