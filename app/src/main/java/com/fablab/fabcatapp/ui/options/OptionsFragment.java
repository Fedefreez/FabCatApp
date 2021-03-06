package com.fablab.fabcatapp.ui.options;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.fablab.fabcatapp.MainActivity;
import com.fablab.fabcatapp.R;
import com.fablab.fabcatapp.ui.bluetooth.BluetoothFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class OptionsFragment extends Fragment {
    public static SharedPreferences preferences;
    public static int pitchRollDelay;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_options, container, false);

        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener((view) -> BluetoothFragment.sendCustomCommand(view, getContext())); //is equal to (view) -> BluetoothConnect.sendCustomCommand(view);

        EditText pitchRollDelay = root.findViewById(R.id.pitchRollDelayEditText);
        pitchRollDelay.setText(getString(R.string.empty_string_int, getPreferencesInt("pitchRollDelay", getContext())));
        pitchRollDelay.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                try {
                    setPreferencesInt("pitchRollDelay", Integer.parseInt(pitchRollDelay.getText().toString()), getContext());
                } catch (Exception e) {
                    MainActivity.createAlert("Insert a valid number!", root, false);
                    hideOptionsFragmentKeyboard(root);
                }
            }
        });

        EditText bluetoothDiscoveryCountdown = root.findViewById(R.id.bluetoothDiscoveryCountdown);
        bluetoothDiscoveryCountdown.setText(getString(R.string.empty_string_int, getPreferencesInt("discoveryCountdown", getContext())));
        bluetoothDiscoveryCountdown.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                try {
                    setPreferencesInt("discoveryCountdown", Integer.parseInt(bluetoothDiscoveryCountdown.getText().toString()), getContext());
                } catch (Exception e) {
                    MainActivity.createAlert("Insert a valid number!", root, false);
                    hideOptionsFragmentKeyboard(root);
                }
            }
        });

        Switch debugSwitch = root.findViewById(R.id.debugSwitch);
        debugSwitch.setChecked(getPreferencesBoolean("debug", getContext()));
        debugSwitch.setOnCheckedChangeListener((v, checked) -> setPreferencesBoolean("debug", checked, getContext()));

        Switch darkThemeSwitch = root.findViewById(R.id.darkThemeSwitch);
        darkThemeSwitch.setChecked(getPreferencesBoolean("DarkTheme", getContext()));
        darkThemeSwitch.setOnCheckedChangeListener((v, checked) -> {
            setPreferencesBoolean("DarkTheme", checked, getContext());
            MainActivity.createCriticalErrorAlert("Warning", "You need to restart the app to apply any theme change.", getContext());
        });

        return root;
    }

    public static void fetchSettings(Context applicationContext) {
        preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);

        if (isAppFirstRun(applicationContext)) {
            setPreferencesInt("pitchRollDelay", 1000, applicationContext);
            setPreferencesInt("discoveryCountdown", 5, applicationContext);
            setPreferencesBoolean("debug", false, applicationContext);
            setPreferencesInt("motorIncrementMultiplier", 1, applicationContext);
        }
    }

    public static int getPreferencesInt(String key, Context applicationContext) {
        int obtainedValue;
        if (!preferences.contains(key)) {
            MainActivity.createOverlayAlert("Error", "Error while reading: " + key + ". It is recommended to restart the app. Error code 4x03", applicationContext);
            obtainedValue = -1;
        } else {
            obtainedValue = preferences.getInt(key, -1);
        }
        return obtainedValue;
    }

    public static void setPreferencesInt(String key, int value, Context applicationContext) {
        if (preferences != null) {
            if (!preferences.edit().putInt(key, value).commit()) {
                MainActivity.createOverlayAlert("Error", "Error while writing: " + key + ". It is recommended to restart the app. Error code 4x02", applicationContext);
            }
        }
    }

    public static boolean isAppFirstRun(Context applicationContext) {
        if (preferences != null) {
            return preferences.getBoolean("isAppFirstRun", true);
        } else {
            MainActivity.createOverlayAlert("Error", "Error while fetching preferences. It is recommended to restart the app. Error code 4x03", applicationContext);
            return false;
        }
    }

    public static void setPreferencesBoolean(String key, boolean value, Context applicationContext) {
        if (preferences != null) {
            if (!preferences.edit().putBoolean(key, value).commit()) {
                MainActivity.createOverlayAlert("Error", "Error while writing: " + key + ". It is recommended to restart the app. Error code 4x02", applicationContext);
            }
        } else {
            MainActivity.createPreferencesErrorAlert("Error", "It isn't possible for the app to save or read settings anymore due to an unknown error, that occurred during the app startup. Error code 4x01", applicationContext);
        }
    }

    public static boolean getPreferencesBoolean(String key, Context applicationContext) {
        if (preferences == null) {
            MainActivity.createPreferencesErrorAlert("Error", "It isn't possible for the app to save or read settings anymore due to an unknown error, that occurred during the app startup. Error code 4x01", applicationContext);
            return false;
        } else {
            return preferences.getBoolean(key, false);
        }

    }

    private void hideOptionsFragmentKeyboard(View root) {
        Context context = getContext();
        if (context != null) {
            MainActivity.hideKeyboardFrom(context, root, context);
        } else {
            MainActivity.createOverlayAlert("Error", "Couldn't get the context of this view. It is recommended to restart the app. Error code 5x01", requireActivity().getApplicationContext());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}