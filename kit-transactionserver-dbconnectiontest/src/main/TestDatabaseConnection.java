package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public final class TestDatabaseConnection {


    static final double NANO_TO_SECONDS = 1000000000.0d;

	public static void main(final String[] args) throws ClassNotFoundException, SQLException {

		Properties properties = ResourcesLoader.loadExternalPropertyFile("config/database.joseflavio-mira-srvkit-tinet.properties");

		DatabaseConfig storageConfiguration = new DatabaseConfig(properties);

		System.out.println(storageConfiguration);

		// final String driverClassName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		Class.forName(storageConfiguration.getDbDriver());

		System.out.println("JDBC Driver loaded: " + storageConfiguration.getDbDriver());

		long startTime = System.nanoTime();

		long connectStartTime = System.nanoTime();

		Connection connection = DriverManager
				.getConnection(storageConfiguration.getDbUrl(), storageConfiguration.getDbUser(), storageConfiguration.getDbPassword());

		long connectEndTime = System.nanoTime();

		PreparedStatement st = connection.prepareStatement("SELECT KTClientId, KTPassword, name, KTClientStatus FROM dbo.Authenticate");

		System.out.print("Executing query...");

		long executeStartTime = System.nanoTime();
		ResultSet rs = st.executeQuery();
		long executeEndTime = System.nanoTime();

		System.out.println("Query executed with success.");

		long resultRetrieveStartTime = System.nanoTime();
		final List<String> resultList = new LinkedList<String>();
		while (rs.next()) {
			String ktClientId = rs.getString("KTClientId");
			String ktPassword = rs.getString("KTPassword");
			String ktClientStatus = rs.getString("KTClientStatus");
			String name = rs.getString("name");

			final String line = ktClientId + "\t" + ktPassword + "\t" + ktClientStatus + "\t" + name;
			resultList.add(line);

		}
		long resultRetrieveEndTime = System.nanoTime();

		long closeStartTime = System.nanoTime();
		st.close();
		connection.close();
		long closeEndTime = System.nanoTime();

		long endTime = System.nanoTime();

		System.out.println("Numero linhas retornadas: " + resultList.size());

	    double connectTime = (connectEndTime - connectStartTime) / NANO_TO_SECONDS;
	    System.out.println("Tempo para se conectar ao DB: " + connectTime);

		double executeTime = (executeEndTime - executeStartTime) / NANO_TO_SECONDS;
		System.out.println("Tempo para executar o query: " + executeTime);

		double resultRetrieveTime = (resultRetrieveEndTime - resultRetrieveStartTime) / NANO_TO_SECONDS;
		double resultRetrieveTimePerLine = resultRetrieveTime / resultList.size();
		System.out.println("Tempo para retornar o resultado: " + resultRetrieveTime + " ( " + resultRetrieveTimePerLine + " por linha )");

        double closeTime = (closeEndTime - closeStartTime) / NANO_TO_SECONDS;
        System.out.println("Tempo para fechar a conexao com o DB: " + closeTime);

        double databaseTotalTime = connectTime + executeTime + resultRetrieveTime + closeTime;
        System.out.println("Tempo total do DB: " + databaseTotalTime);

        double totalTime = (endTime -startTime) / NANO_TO_SECONDS;
        System.out.println("Tempo total: " + totalTime);

	}

}// Class
