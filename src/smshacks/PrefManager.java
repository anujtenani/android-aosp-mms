package com.smartanuj.dbnew;


import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import android.app.AlarmManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

public class PrefManager {

	private SharedPreferences prefs;
	
	private static PrefManager mpref;
	private static Object syncObj = new Object();
	private PrefManager(Context ctx){
		prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
	}
	
	public static PrefManager getInstance(Context ctx){
		synchronized(syncObj){
			if(mpref == null){
				mpref = new PrefManager(ctx);
			}
			return mpref;
		}
	}
	public boolean setVaultLocation(String vaultLoc){
		if(!getVaultLocation().equals(vaultLoc)){
			return prefs.edit().putString("vaultLoc", vaultLoc).commit();
		}
		return false;
	}
	public String getVaultLocation(){
		return prefs.getString("vaultLoc", Environment.getExternalStorageDirectory()+"/ProgramData/Android/Language/.fr");
	}

}