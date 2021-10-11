package trifork.messagereceiver.data;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.ConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import trifork.common.core.Result;

public class TriforkDataContext {
    private static final Logger Log = LoggerFactory.getLogger(TriforkDataContext.class);
    
    private static final String MESSAGES_TABLENAME = "Messages";
    
    private final String _databaseFileName;

    private TriforkDataContext(String databaseFileName) {
        super();

        _databaseFileName = databaseFileName;
    }

    public static TriforkDataContext init(String databaseFileName) throws ConfigurationException {
        if (new File(databaseFileName).exists()) {
            Log.info("Database file found. Setup completed.");

            return new TriforkDataContext(databaseFileName);
        }

        Log.info("Database file not found. It will be created.");
        if (createDatabase(databaseFileName) && createMessageTable(databaseFileName))
            return new TriforkDataContext(databaseFileName);

        throw new ConfigurationException("Database initilization unsuccessful.");
    }

    public Result executeSql(String sql, Object[] params) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = openConnection();
            conn.setAutoCommit(false);

            stmt = conn.prepareStatement(sql);

            for (int i = 0; i < params.length; i++) {
                Object param = params[i];

                if (param instanceof Integer)
                    stmt.setInt(i + 1, (int)param);
                else if (param instanceof Long)
                    stmt.setLong(i + 1, (long)param);
                else if (param instanceof Boolean)
                    stmt.setBoolean(i + 1, (boolean)param);
                else
                    stmt.setString(i + 1, param.toString());
            }

            stmt.execute();

            conn.commit();
            
            return Result.Success();
        }
        catch (SQLException e) {
            try {
                if (conn != null)
                    conn.rollback();
            }
            catch (SQLException e2) {
                Log.error("Unexpected error when attempting to roll back transaction.", e2);
            }
            
            Log.error("Unexpected error when committing transaction.", e);
            return Result.Fail("SQL execution failed", e);
        }
        finally {
            try {
                if (conn != null)
                    conn.close();
                
                if (stmt != null)
                    stmt.close();
            }
            catch (SQLException e) {
                Log.error("Unexpected error when closing connection.", e);
            }
        }
    }

    private Connection openConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + _databaseFileName);
    }

    private static Boolean createDatabase(String databaseFileName) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + databaseFileName)) {
            conn.getMetaData();
            Log.info("Database file '" + databaseFileName + "' created.");
            return true;
        } 
        catch (Exception e) {
            Log.error("Unexpected error when creating database file '" + databaseFileName + "'", e);
            return false;
        }
    }

    private static Boolean createMessageTable(String databaseFileName) {
        String sql = new StringBuilder()
            .append("CREATE TABLE IF NOT EXISTS " + MESSAGES_TABLENAME + " (\n")
            .append("    id integer PRIMARY KEY,\n")
            .append("    content text NOT NULL,\n")
            .append("    timestamp integer NOT NULL\n")
            .append(");")
            .toString();
            
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + databaseFileName); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            Log.info("Table '" + MESSAGES_TABLENAME + "' created.");
            return true;
        }
        catch (Exception e) {
            Log.error("Unexpected error when creating table '" + MESSAGES_TABLENAME + "'.", e);
            return false;
        }
    }
    
}
