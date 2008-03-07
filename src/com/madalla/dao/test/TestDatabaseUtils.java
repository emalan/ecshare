package com.madalla.dao.test;

import javax.sql.DataSource;

import org.apache.ddlutils.Platform;
import org.apache.ddlutils.PlatformFactory;
import org.apache.ddlutils.io.DatabaseDataIO;
import org.apache.ddlutils.io.DatabaseIO;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.platform.derby.DerbyPlatform;

public class TestDatabaseUtils {

    public static void setupDatabase(DataSource dataSource) {
        try {
            Database database = new DatabaseIO().read("resources/db-schema.xml");
            String[] dataScripts = new String[]{"resources/db-data.xml"};
            changeDatabase(dataSource, database, true);
            
            Platform platform = new DerbyPlatform();
            platform.setDataSource(dataSource);
            DatabaseDataIO dataIO = new DatabaseDataIO();
            dataIO.writeDataToDatabase(platform, database, dataScripts);
        } catch (Exception e) {
            System.out.println("Could not populate database with schema." + e);
        }

    }

    private static void changeDatabase(DataSource dataSource,
            Database targetModel, boolean alterDb) {

        Platform platform = PlatformFactory.createNewPlatformInstance(dataSource);

        if (alterDb) {
            platform.alterTables(targetModel, false);
        } else {
            platform.createTables(targetModel, true, false);
        }
    }
    
}
