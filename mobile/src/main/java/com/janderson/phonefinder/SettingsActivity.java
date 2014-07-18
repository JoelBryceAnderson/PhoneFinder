package com.janderson.phonefinder;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Messenger;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.content.IntentCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.janderson.phonefinder.R;

public class SettingsActivity extends Activity {

    private SharedPreferences sharedPreferences;
    private boolean wakeScreen;
    public static final String START_ACTIVITY_PATH = "/start/MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        getFragmentManager().beginTransaction().replace(
                android.R.id.content, new MyPreferenceFragment()).commit();
        setContentView(R.layout.activity_settings);
        RelativeLayout settingsLayout = (RelativeLayout) findViewById(R.id.settings_layout);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }

    public static class MyPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            final CheckBoxPreference hideIcon = (CheckBoxPreference) findPreference("hide");
            hideIcon.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    if (newValue.toString().equals("true")) {
                        PackageManager p = getActivity().getPackageManager();
                        p.setComponentEnabledSetting(getActivity().getComponentName(),
                                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                                PackageManager.DONT_KILL_APP);
                    } else {
                        PackageManager p = getActivity().getPackageManager();
                        p.setComponentEnabledSetting(getActivity().getComponentName(),
                                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                                PackageManager.DONT_KILL_APP);
                    }
                    return true;
                }
            });
            Preference aboutCreator = findPreference("about");
            aboutCreator.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(
                            getActivity().getApplicationContext(), AboutActivity.class);
                    startActivity(intent);
                    return true;
                }
            });
        }
    }
}
