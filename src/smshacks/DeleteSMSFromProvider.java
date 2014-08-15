package smshacks;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class DeleteSMSFromProvider extends BroadcastReceiver{

	@Override
	public void onReceive(Context arg0, Intent arg1) {
			
		Bundle b = arg1.getExtras();
		String destAddress = b.getString("destinationAddress");
		String message = b.getString("message");
		ContentResolver cr = arg0.getContentResolver();
		cr.delete(Telephony.Sms.CONTENT_URI, Telephony.Sms.Sent.ADDRESS+"=? AND "+Telephony.Sms.Sent.BODY+"=?", new String[]{destAddress,message});
		
		
		Cursor c= cr.query(Telephony.Sms.CONTENT_URI, new String[]{Telephony.Sms.BODY,Telephony.Sms.READ,Telephony.Sms.THREAD_ID}, Telephony.Sms.ADDRESS+"=?",	new String[]{destAddress}, Telephony.Sms.DEFAULT_SORT_ORDER);
		if(c != null && c.getCount()>0){
			int count = c.getCount();
			c.moveToFirst();
			String body = c.getString(c.getColumnIndex(Telephony.Sms.BODY));
			boolean read = c.getInt(c.getColumnIndex(Telephony.Sms.READ)) == 0 ? false :true;
			int threadId = c.getInt(c.getColumnIndex(Telephony.Sms.THREAD_ID));
			ContentValues cv = new ContentValues();
			cv.put(Telephony.Threads.MESSAGE_COUNT, count);
			cv.put(Telephony.Threads.SNIPPET, body);
			cv.put(Telephony.Threads.READ, read);
			cr.update(Telephony.Threads.CONTENT_URI, cv, Telephony.Threads._ID+"=?", new String[]{String.valueOf(threadId)});
		}
		
	}

}
