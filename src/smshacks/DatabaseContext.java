package smshacks;

import java.io.File;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseContext extends ContextWrapper {

	private String _database_path = null;
	public DatabaseContext(Context base,String dbPath) {
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
	
	private File getSDDatabaseFile(String name){
		File f = new File(_database_path);
		if(!f.exists() && !f.isDirectory()){
			f.mkdirs();
		}
		return new File(f,name);
	}

}
