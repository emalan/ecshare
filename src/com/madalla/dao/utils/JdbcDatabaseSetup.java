package com.madalla.dao.utils;

import java.io.InputStream;

import javax.sql.DataSource;

import org.apache.ddlutils.Platform;
import org.apache.ddlutils.PlatformFactory;
import org.apache.ddlutils.io.DatabaseDataIO;
import org.apache.ddlutils.io.DatabaseIO;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.platform.derby.DerbyPlatform;
import org.xml.sax.InputSource;

public class JdbcDatabaseSetup {
	private DataSource dataSource;
	private InputStream schema;
	private InputStream data;
	
	public void setData(InputStream data) {
		this.data = data;
	}

	public void setSchema(InputStream schema) {
		this.schema = schema;
	}

	public void init(){
		setupDatabase(dataSource);
	}
	
	public void setupDatabase(DataSource dataSource) {
        try {
        	Database database = new DatabaseIO().read(new InputSource(schema));
            changeDatabase(dataSource, database, true);
            
            Platform platform = new DerbyPlatform();
            platform.setDataSource(dataSource);
            DatabaseDataIO dataIO = new DatabaseDataIO();
            InputStream[] dataSources = new InputStream[]{data};
            dataIO.writeDataToDatabase(platform, database, dataSources);
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

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}
