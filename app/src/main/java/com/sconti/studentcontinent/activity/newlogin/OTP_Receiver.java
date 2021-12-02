package com.sconti.studentcontinent.activity.newlogin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.EditText;

public class OTP_Receiver extends BroadcastReceiver {
    private static final String TAG = "OTP_Receiver";
    private static EditText editText1;
    private static EditText editText2;
    private static EditText editText3;
    private static EditText editText4;
    private static EditText editText5;
    private static EditText editText6;

    public void setEditText(EditText mInputCode1, EditText mInputCode2 , EditText mInputCode3 , EditText mInputCode4 , EditText mInputCode5 , EditText mInputCode6){
        OTP_Receiver.editText1 = mInputCode1;
        OTP_Receiver.editText2 = mInputCode2;
        OTP_Receiver.editText3 = mInputCode3;
        OTP_Receiver.editText4 = mInputCode4;
        OTP_Receiver.editText5 = mInputCode5;
        OTP_Receiver.editText6 = mInputCode6;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);

        for(SmsMessage sms : messages){
            String msg = sms.getMessageBody();

            Log.d(TAG, "onReceive: "+msg +" and  ");
            String numberOnly = msg.replaceAll("[^0-9]", "");
            Log.d(TAG, "onReceive: "+numberOnly +" and  ");

          /*  String otp1 = msg.split("")[;
            String otp2 = msg.split("")[1];
            String otp3 = msg.split("")[2];
            String otp4 = msg.split("")[3];
            String otp5 = msg.split("")[4];
            String otp6 = msg.split("")[5];
*/
            editText1.setText(numberOnly.charAt(0)+"");
            editText2.setText(numberOnly.charAt(1)+"");
            editText3.setText(numberOnly.charAt(2)+"");
            editText4.setText(numberOnly.charAt(3)+"");
            editText5.setText(numberOnly.charAt(4)+"");
            editText6.setText(numberOnly.charAt(5)+"");
        }
    }


}
