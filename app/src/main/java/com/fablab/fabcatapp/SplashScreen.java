package com.fablab.fabcatapp;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    public static String compactAppVersion;
    public static String compactAppBuild;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("DarkTheme", false)) {
            setTheme(R.style.DarkTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        TextView versionTextView = findViewById(R.id.versionTextView);
        TextView buildTextView = findViewById(R.id.buildTextView);
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            compactAppVersion = "v"+pInfo.versionName+" beta";
            compactAppBuild = "Build: " + getResources().getString(R.string.build_version);

            versionTextView.setText(getString(R.string.version_string, pInfo.versionName));
            buildTextView.setText(compactAppBuild);
        } catch (PackageManager.NameNotFoundException e) {
            MainActivity.createOverlayAlert("Error", "Couldn't get the app version. It is recommended to reinstall the app.", getApplicationContext());
        }

        Thread init = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        init.start();
    }
}
