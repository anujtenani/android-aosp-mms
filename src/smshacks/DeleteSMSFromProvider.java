package smshacks;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.util.Log;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class DeleteSMSFromProvider extends BroadcastReceiver{

	ExecutorService exec = Executors.newFixedThreadPool(1);
	static Handler handler = new Handler();
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		Log.i("Anuj","broadcast received");

		Bundle b = arg1.getExtras();
		final String destAddress = b.getString("destinationAddress");
		final String message = b.getString("message");
		Log.i("Anuj","dest:"+destAddress+":message:"+message);
		final ContentResolver cr = arg0.getContentResolver();

		exec.execute(new Runnable(){

			@Override
			public void run() {
				handler.postDelayed(new Runnable(){

					@Override
					public void run() {
						
						cr.delete(Telephony.Sms.CONTENT_URI, Telephony.Sms.Sent.ADDRESS+"=? AND "+Telephony.Sms.Sent.BODY+"=?", new String[]{destAddress,message});											
						ContentValues v = new ContentValues();
						v.put(Telephony.Sms.Conversations.SNIPPET, "");
						cr.update(Telephony.Sms.Conversations.CONTENT_URI,v , Telephony.Sms.Conversations.ADDRESS+"=?",new String[]{Telephony.Sms.Conversations.ADDRESS});
					}
				}, 1000);
				//delete one last message for this destination address;
			}
			
		});
		
		
//		cr.delete(Telephony.Sms.CONTENT_URI, Telephony.Sms.Sent.ADDRESS+"=? AND "+Telephony.Sms.Sent.BODY+"=?", new String[]{destAddress,message});
/*		

		Cursor c = cr.query(Telephony.Sms.CONTENT_URI, new String[]{Telephony.Sms._ID}, Telephony.Sms.Sent.ADDRESS+"=? AND "+Telephony.Sms.Sent.BODY+"=?", new String[]{destAddress,message},Telephony.Sms.DEFAULT_SORT_ORDER);
		
		if(c != null && c.getCount()>0){
			c.moveToFirst();
			int id = c.getInt(c.getColumnIndex(Telephony.Sms._ID));
			c.close();			
			if(id>0){
//				cr.delete(ContentUris.withAppendedId(Telephony.Sms.CONTENT_URI, id), null, null);
				cr.delete(Uri.parse(Telephony.Sms.CONTENT_URI+"/#/"+id), null,null);
			}
		}
		/*
		cr.delete();		
		Log.i("Anuj","deleted from message list");
		
		
		Cursor c= cr.query(Telephony.Sms.CONTENT_URI, new String[]{Telephony.Sms.BODY,Telephony.Sms.READ,Telephony.Sms.THREAD_ID}, Telephony.Sms.ADDRESS+"=?",	new String[]{destAddress}, Telephony.Sms.DEFAULT_SORT_ORDER);
		if(c != null && c.getCount()>0){
			int count = c.getCount();
			Log.i("Anuj","count="+count);

			c.moveToFirst();
			String body = c.getString(c.getColumnIndex(Telephony.Sms.BODY));
			boolean read = c.getInt(c.getColumnIndex(Telephony.Sms.READ)) == 0 ? false :true;
			int threadId = c.getInt(c.getColumnIndex(Telephony.Sms.THREAD_ID));
			ContentValues cv = new ContentValues();
			cv.put(Telephony.Threads.MESSAGE_COUNT, count);
			cv.put(Telephony.Threads.SNIPPET, body);
			cv.put(Telephony.Threads.READ, read);
			cr.update(Telephony.Threads.CONTENT_URI, cv, Telephony.Threads._ID+"=?", new String[]{String.valueOf(threadId)});
			Log.i("Anuj","updated threads");
			c.close();
		}
		*/
		
	}

}
