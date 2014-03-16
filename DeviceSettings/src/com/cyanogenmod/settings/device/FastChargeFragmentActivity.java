/*
 * Copyright (C) 2012 The CyanogenMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cyanogenmod.settings.device;

import java.io.File;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;

public class FastChargeFragmentActivity extends PreferenceFragment {

    private static final String TAG = "GalaxyS4Parts_General";

    private static final String FILE = "/sys/kernel/fast_charge/force_fast_charge";
  

    private static final boolean sHasFastCharge = Utils.fileExists(FILE);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.charge_preferences);

        PreferenceCategory prefs = (PreferenceCategory) findPreference(DeviceSettings.CATEGORY_FASTCHARGE);
        if (!sHasFastCharge) {
            prefs.removePreference(findPreference(DeviceSettings.KEY_FASTCHARGE));
        }
      

    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {

        String boxValue;
        String key = preference.getKey();

        Log.w(TAG, "key: " + key);

        if (key.compareTo(DeviceSettings.KEY_FASTCHARGE) == 0) {
            boxValue = (((CheckBoxPreference)preference).isChecked() ? "1" : "0");
            Utils.writeValue(FILE, boxValue);

        }

        return true;
    }

    public static void restore(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        // When use gyro calibration value is set to 1, calibration is done at the same time, which
        // means it is reset at each boot, providing wrong calibration most of the time at each reboot.
        // So we only set it to "0" if user wants it, as it defaults to 1 at boot
        if (!sharedPrefs.getBoolean(DeviceSettings.KEY_FASTCHARGE, true))
            Utils.writeValue(FILE, "0");
    }
}
