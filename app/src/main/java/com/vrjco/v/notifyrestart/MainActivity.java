package com.vrjco.v.notifyrestart;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "notify_restart_app";
    public static final String NOTIFY_NAME = "notify_restart_app_notify";
    //App prefs
    private static SharedPreferences prefs;
    private Boolean isChecked = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setting layout.
        setContentView(R.layout.activity_main);

        //dismiss last notification if system tray still contains in it.
        NotificationManager notificationmanager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationmanager.cancel(0);

        //switch from layout.
        Switch notify = (Switch) findViewById(R.id.notifiy_switch);
        initPrefs(this);
        notify.setChecked(prefs.getBoolean(NOTIFY_NAME, true));
        notify.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isChecked = b;
                //save switch state.
                setPrefsNotify(b);
            }
        });


    }

    //initializing shared prefs with context
    public static SharedPreferences initPrefs(Context context) {
        if (prefs == null) {
            try {
                //open prefs in private mode
                prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                return prefs;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        return prefs;
    }

    //set switch state into prefs
    private void setPrefsNotify(boolean b) {
        SharedPreferences.Editor editor = prefs.edit();
        //saving switch state.
        editor.putBoolean(NOTIFY_NAME, b);
        editor.apply();
    }

    //get switch state from prefs
    public static boolean getPrefsNotify(Context context) {
        initPrefs(context);
        try {
            return prefs.getBoolean(NOTIFY_NAME, true);
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;

    }

    @Override
    protected void onPause() {
        super.onPause();
        initPrefs(this);
        setPrefsNotify(isChecked);
    }
}
