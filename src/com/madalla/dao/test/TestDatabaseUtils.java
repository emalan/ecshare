package com.madalla.dao.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.ddlutils.Platform;
import org.apache.ddlutils.io.DatabaseIO;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.platform.derby.DerbyPlatform;

public class TestDatabaseUtils {

    public static void createDatabase() {
        Connection connection = null;
        try {
            Database database = new DatabaseIO()
                    .read("resources/db-schema.xml");
            connection = getConnection();
            changeDatabase(connection, database, true);
        } catch (Exception e) {
            System.out.println("Could not populate database with schema." + e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    
                }
            }
        }

    }

    private static void changeDatabase(Connection connection,
            Database targetModel, boolean alterDb) {

        Platform platform = new DerbyPlatform();

        if (alterDb) {
            platform.alterTables(connection, targetModel, false);
        } else {
            platform.createTables(connection, targetModel, true, false);
        }
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:derby:database;create=true");
    }
}
