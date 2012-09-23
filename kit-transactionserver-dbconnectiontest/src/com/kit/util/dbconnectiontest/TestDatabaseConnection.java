package com.kit.util.dbconnectiontest;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.dajo.framework.configuration.ConfigAccessor;
import org.dajo.framework.configuration.ConfigurationReader;
import org.dajo.framework.db.DatabaseConfig;

public final class TestDatabaseConnection {

    static public void main(final String[] args) throws ClassNotFoundException, SQLException {

		ConfigAccessor configAccessor = ConfigurationReader.loadExternalProperties("config/database.keepin_v01_demo01.at.kitdev01_win.properties");
		DatabaseConfig dbConfig = DatabaseConfig.getInstance(configAccessor, true);
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
