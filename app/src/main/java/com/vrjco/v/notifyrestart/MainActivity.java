package com.vrjco.v.notifyrestart;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "notify_restart_app";
    public static final String NOTIFY_NAME = "notify_restart_app_notify";
    public static final String NOTIFY_TITLE = "notify_restart_app_title";
    public static final String NOTIFY_TEXT = "notify_restart_app_text";
    //App prefs
    private static SharedPreferences prefs;
    private Boolean isChecked = true;
    private EditText title, text;
    private String Stitle, Stext;


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

        title = (EditText) findViewById(R.id.title_et);
        text = (EditText) findViewById(R.id.text_et);

        title.setText(getPrefsTitle(this));
        text.setText(getPrefsText(this));


        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().trim().isEmpty()) {
                    saveTitleInPrefs(editable.toString().trim());
                    Stitle = editable.toString().trim();
                }
            }
        });

        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().trim().isEmpty()) {
                    saveTextInPrefs(editable.toString().trim());
                    Stext = editable.toString().trim();
                }
            }
        });


    }

    private void saveTextInPrefs(String stitle) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(NOTIFY_TEXT, stitle.trim());
        editor.apply();
    }

    private void saveTitleInPrefs(String stext) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(NOTIFY_TITLE, stext.trim());
        editor.apply();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static String getPrefsTitle(Context context) {
        initPrefs(context);
        try {
            return prefs.getString(NOTIFY_TITLE, "Phone Restarted!");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Phone Restarted!";
    }

    public static String getPrefsText(Context context) {
        initPrefs(context);
        try {
            return prefs.getString(NOTIFY_TEXT, "Time: " + getDate());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Time: " + getDate();
    }


    private static String getDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss - dd-MM-yyyy", Locale.ENGLISH);
        Date now = new Date();
        return formatter.format(now);
    }

    @Override
    protected void onPause() {
        super.onPause();
        initPrefs(this);
        setPrefsNotify(isChecked);
    }
}
