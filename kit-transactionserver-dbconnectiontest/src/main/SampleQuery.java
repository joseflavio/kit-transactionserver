package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.fap.chronometer.Chronometer;
import com.fap.framework.db.DatabaseConfig;

public class SampleQuery {

    static public void executeSampleQuery(final DatabaseConfig dbConfig) throws SQLException {

        Chronometer executionTotalTime = new Chronometer("TotalExecutionTime");
        executionTotalTime.start();

        Chronometer connectChron = new Chronometer("DriverManager.getConnection");
        connectChron.start();
        Connection connection = DriverManager.getConnection(dbConfig.getDbUrl(), dbConfig.getDbUser(), dbConfig.getDbPassword());
        connectChron.stop();

        PreparedStatement st = connection.prepareStatement("SELECT KTClientId, KTPassword, name, KTClientStatus FROM dbo.Authenticate");

        System.out.print("Executing query...");

        Chronometer queryExecuteChron = new Chronometer("st.executeQuery");
        queryExecuteChron.start();
        ResultSet rs = st.executeQuery();
        queryExecuteChron.stop();

        System.out.println("Query executed with success.");

        Chronometer queryResultGetChron = new Chronometer("rs.next");
        queryResultGetChron.start();
        final List<String> resultList = new LinkedList<String>();
        while (rs.next()) {
            String ktClientId = rs.getString("KTClientId");
            String ktPassword = rs.getString("KTPassword");
            String ktClientStatus = rs.getString("KTClientStatus");
            String name = rs.getString("name");
            final String line = ktClientId + "\t" + ktPassword + "\t" + ktClientStatus + "\t" + name;
            resultList.add(line);
        }
        queryResultGetChron.stop();

        Chronometer closeChron = new Chronometer("connection.close");
        closeChron.start();
        st.close();
        connection.close();
        closeChron.stop();

        executionTotalTime.stop();

        /*
         *
         */
        System.out.println("Numero linhas retornadas: " + resultList.size());

        System.out.println("Tempo para se conectar ao DB: " + connectChron);

        System.out.println("Tempo para executar o query: " + queryExecuteChron);

        double resultRetrieveTimePerLine = queryResultGetChron.getElapsedTime() / resultList.size();
        System.out.println("Tempo para retornar o resultado: " + queryResultGetChron + " ( " + resultRetrieveTimePerLine + " por linha )");

        System.out.println("Tempo para fechar a conexao com o DB: " + closeChron);

        double databaseTotalTime = Chronometer.addElapsedTime(connectChron, queryExecuteChron, queryResultGetChron, closeChron);
        System.out.println("Tempo total do DB: " + databaseTotalTime);

        System.out.println("Tempo total: " + executionTotalTime);

    }

}
