package com.smartanuj.dbnew;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

import com.smartanuj.fileutils.FileUtils;
import com.smartanuj.sms.obj.BlackListObj;
import com.smartanuj.sms.obj.CallObj;
import com.smartanuj.sms.obj.SMSObj;

import android.app.AlarmManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Environment;
import android.telephony.PhoneNumberUtils;

public class PrimaryDb extends SQLiteOpenHelper{

	
	private static final int DATABASE_VERSION = 3;
	public static final String DATABASE_NAME = "aqua";
    private static final String BLACKLIST_TABLE = "blacklist";
    private static final String SMS_TABLE = "sms";
    private static final String CALLS_TABLE = "calls";

    
	private static final String ID = "id";    
    private static final String LAST_SMS_RECEIVED = "last_sms_received";
    private static final String PHONE = "phone";
    private static final String PHONEFORMATTED = "phoneFormatted";

    private static final String NAME = "name";
    private static final String HIDE_SMS = "hide_sms";
    private static final String HIDE_CALL_LOG = "hide_call_log";
    private static final String BLOCK_INCOMING_CALL = "block_incoming_calls";
    private static final String BLOCK_OUTGOING_CALL = "block_outgoing_calls";
    private static final String SMS_NOTIFICATION = "showSMSNotification";
    private static final String CALL_NOTIFICATION = "showCallNotification";
    private static final String ADDED = "added";
    
    
    private static final String UID = "uid";
    private static final String TYPE = "type";
    private static final String DURATION = "duration";
    private static final String SEEN = "seen";

    
    private static final String SUBJECT = "sms_subject";
    private static final String BODY = "sms_body";
    
    public PrimaryDb(Context context,String dbPath) {
		super(getContext(context,dbPath),DATABASE_NAME,null,DATABASE_VERSION);
	}

	
	public static Context getContext(Context context,String dbPath){
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
			return new DatabaseContextHoneyComb(context, dbPath);
		}else{
			return new DatabaseContext(context, dbPath);
		}
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
	//	Log.i("Anuj","OnCreate Called");
		final String CREATE_SMS_TABLE = "CREATE TABLE IF NOT EXISTS sms (" +
	    		"id INTEGER NOT NULL," +
	    		"uid INTEGER NOT NULL," +
	    		"sms_subject TEXT NOT NULL," +
	    		"sms_body TEXT NOT NULL," +
	    		"type INTEGER NOT NULL," +
	    		"seen INTEGER NOT NULL," +
	    		"added INTEGER NOT NULL," +
	    		"PRIMARY KEY(id));";
		 final String CREATE_CALLS_TABLE = "CREATE TABLE IF NOT EXISTS "+CALLS_TABLE+" (" +
		    		""+ID+" INTEGER NOT NULL," +
		    		""+UID+" INTEGER NOT NULL, " +
		    		""+TYPE+" INTEGER NOT NULL," +
		    		""+DURATION+" INTEGER NOT NULL," +
		    		""+ADDED+" INTEGER NOT NULL," +
		    		""+SEEN+" INTEGER NOT NULL," +
		    		"PRIMARY KEY(id));";
		 String CREATE_BLACKLIST_TABLE = 	"CREATE TABLE IF NOT EXISTS "+BLACKLIST_TABLE+ "(" +
		    		""+PHONEFORMATTED+" TEXT, " +
		    		""+ID+" INTEGER PRIMARY KEY, " +
		    		""+PHONE+" TEXT, " +
		    		""+NAME+" TEXT, " +
		    		""+HIDE_SMS+" INTEGER, " +
		    				""+HIDE_CALL_LOG+" INTEGER, " +
		    				""+BLOCK_INCOMING_CALL+" INTEGER, " +
		    				""+BLOCK_OUTGOING_CALL+" INTEGER, " +
		    				""+SMS_NOTIFICATION+" INTEGER, " +
		    				""+CALL_NOTIFICATION+" INTEGER, " +
		    				""+LAST_SMS_RECEIVED+" INTEGER, " +
		    				""+ADDED+" INTEGER" +
		    				");";
		db.execSQL(CREATE_BLACKLIST_TABLE);
		db.execSQL(CREATE_CALLS_TABLE);
		db.execSQL(CREATE_SMS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
	}
	
	
	/** DB HELPER_FUNCTIONS */
	 /** searches for a phone number in blacklist and returns the blacklistObj if it is */
    public boolean isBlackList(String phone){
    	boolean ret = false;
		try{
		    String sql = "SELECT * FROM blacklist";
		    Cursor c = getWritableDatabase().rawQuery(sql,null);
		    if(c!=null){
				int count = c.getCount();
				c.moveToFirst();
				int PHONE_INDEX = c.getColumnIndexOrThrow(PHONE);
			for(int i=0;i<count;i++){
				String ph = c.getString(PHONE_INDEX);
				if(PhoneNumberUtils.compare(phone, ph)){
						ret = true;
				    	break;
					}
				    c.moveToNext();
				}
		    	c.close();
		    }
		}catch(Exception e){
		    e.printStackTrace();
		}
		return ret;
    }

}
