package com.example.user.dictionarytry;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CopyService extends Service {

    NotificationManager nm;
    ClipboardManager myClipboard;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {
        myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        nm= (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        myClipboard.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                ClipData abc = myClipboard.getPrimaryClip();
                ClipData.Item item = abc.getItemAt(0);
                String text = item.getText().toString();
                String st=search(text);
                Toast.makeText(CopyService.this,st,Toast.LENGTH_LONG).show();


            }
        });
        return super.onStartCommand(intent, flags, startId);
    }
    String search(String word)
    {
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(CopyService.this);
        databaseAccess.open();
        String meaning;
        meaning=databaseAccess.getMeaning(camelCase(word));
        databaseAccess.close();
        return meaning;
    }
    String camelCase(String word)
    {
        String a1=word.toLowerCase();
        String a2=a1.substring(0, 1).toUpperCase() + a1.substring(1);
        return a2;
    }


}
