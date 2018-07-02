package com.example.user.dictionarytry;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Helper extends AppCompatActivity {

    Button button;
    MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper);

        button=findViewById(R.id.button4);
        mp=MediaPlayer.create(this,R.raw.switchsound);
        if(isMyServiceRunning(CopyService.class))
        {
            button.setBackgroundResource(R.mipmap.on);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.start();
                if(isMyServiceRunning(CopyService.class))
                {
                    Intent i=new Intent(Helper.this,CopyService.class);
                    stopService(i);
                    button.setBackgroundResource(R.mipmap.off);
                    Toast.makeText(Helper.this, "Background Service stopped.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent i=new Intent(Helper.this,CopyService.class);
                    startService(i);
                    button.setBackgroundResource(R.mipmap.on);
                    Toast.makeText(Helper.this, "Background Service started.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
