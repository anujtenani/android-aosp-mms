package com.smartanuj.dbnew;

import java.io.File;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseContextHoneyComb extends ContextWrapper {

	private String _database_path;
	public DatabaseContextHoneyComb(Context base,String dbPath) {
	    super(base);
	    _database_path = dbPath;
	}

	@Override
	public File getDatabasePath(String name) {
			return getSDDatabaseFile(name);    
	}

	@Override
	public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
		return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
	}
	
	@Override
	public SQLiteDatabase openOrCreateDatabase(String name,int mode,SQLiteDatabase.CursorFactory factory,DatabaseErrorHandler errorHandler){
		return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name).getAbsolutePath(), factory, errorHandler);
	}
	
	private  File getSDDatabaseFile(String name){
		File f = new File(_database_path);
		if(!f.exists() && !f.isDirectory()){
			f.mkdirs();
		}
		return new File(f,name);
	}
}
