package smshacks;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.util.Log;
@TargetApi(Build.VERSION_CODES.KITKAT)
public class DeleteSMSFromProvider extends BroadcastReceiver{

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		Log.i("Anuj","broadcast received");
		Bundle b = arg1.getExtras();
		String destAddress = b.getString("destinationAddress");
		String message = b.getString("message");
		Log.i("Anuj","dest:"+destAddress+":message"+message);
		arg0.getContentResolver().delete(Telephony.Sms.Sent.CONTENT_URI, Telephony.TextBasedSmsColumns.ADDRESS+"=? AND "+Telephony.TextBasedSmsColumns.BODY+"=?", new String[]{destAddress,message});
	}

}
