package com.madalla.db.setup;

import java.io.InputStream;

import javax.sql.DataSource;

import org.apache.ddlutils.Platform;
import org.apache.ddlutils.PlatformFactory;
import org.apache.ddlutils.io.DatabaseIO;
import org.apache.ddlutils.model.Database;
import org.xml.sax.InputSource;

/**
 * Creates database and schema.
 * @author Eugene Malan
 *
 */
public class JdbcDatabaseSetup {
	private DataSource dataSource;
	private InputStream schema;
	private boolean alterDb = true;
	
	public void setSchema(InputStream schema) {
		this.schema = schema;
	}

	public void init(){
		setupDatabase(dataSource);
	}
	
	public void setupDatabase(DataSource dataSource) {
        try {
        	Database database = new DatabaseIO().read(new InputSource(schema));
            changeDatabase(dataSource, database, alterDb);
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

	public void setAlterDb(boolean alterDb) {
		this.alterDb = alterDb;
	}

}
