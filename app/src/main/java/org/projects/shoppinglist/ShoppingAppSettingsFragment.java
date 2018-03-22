package org.projects.shoppinglist;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

/**
 * Created by Jens Christian Rasch on 22-03-2018.
 */

public class ShoppingAppSettingsFragment extends PreferenceFragment {

    private static String SETTINGS_AUTOFILLKEY = "autofill";

    public static boolean shouldAutoFill(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(SETTINGS_AUTOFILLKEY, true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);
    }
}
