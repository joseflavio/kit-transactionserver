package main;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.fap.chronometer.Chronometer;
import com.fap.framework.db.DataSource;
import com.fap.framework.db.DatabaseConfig;
import com.jfap.framework.configuration.ConfigAccessor;
import com.jfap.framework.configuration.ConfigurationReader;

public class TestDabaseConnectionPool {

    static public void main(final String[] args) throws ClassNotFoundException, SQLException {

        final String driverClassName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        Class.forName(driverClassName);
        System.out.println("JDBC Driver loaded: " + driverClassName);

        ConfigAccessor configAccessor = ConfigurationReader.getConfiguration("config/database.joseflavio-mira-srvkit-tinet.properties");
        DatabaseConfig dbConfig = DatabaseConfig.getInstance(configAccessor);

        DataSource dataSource = new DataSource(dbConfig);

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

        private final DataSource dataSource;

        public TestPoolThread(final DataSource dataSource) {
            this.dataSource = dataSource;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(4000);
            }
            catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

}
