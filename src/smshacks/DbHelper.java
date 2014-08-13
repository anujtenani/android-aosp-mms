package com.smartanuj.dbnew;

import android.content.Context;
import android.os.Build;

public class DbHelper {
	
	private static PrimaryDb primaryDb;
	private static String path;
	private static Object dbLock = new Object();
	public static PrimaryDb getPrimaryDb(Context ctx) {
		synchronized(dbLock){
			String dbPath = PrefManager.getInstance(ctx).getVaultLocation();
			if(primaryDb == null || !path.equals(dbPath)){
				path = dbPath;
				primaryDb = new PrimaryDb(ctx,dbPath);
			}
			return primaryDb;
		}
	}
	
	public static void closePrimaryDb(){
		if(primaryDb != null){
			primaryDb.close();
		}
	}
	
	@Override
	public void finalize(){
		if(primaryDb != null){
			primaryDb.close();
		}
	}
	
}
