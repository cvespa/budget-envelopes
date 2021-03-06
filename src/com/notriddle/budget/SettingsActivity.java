/*
 * This file is a part of Budget with Envelopes.
 * Copyright 2013 Anatolij Zelenin <az@azapps.de>
 *
 * Budget is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Budget is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Budget. If not, see <http://www.gnu.org/licenses/>.
 */

package com.notriddle.budget;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.text.InputType;
import android.view.MenuItem;

public class SettingsActivity extends PreferenceActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        if (!PinActivity.ensureUnlocked(this)) {
            finish(); return;
        }
        getActionBar().setDisplayHomeAsUpEnabled(true);
		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, new SettingsFragment()).commit();
	}

    @Override public void onResume() {
        super.onResume();
        if (!PinActivity.ensureUnlocked(this)) {
            finish();
        }
    }

	public static class SettingsFragment extends PreferenceFragment
                                         implements Preference
                                                    .OnPreferenceClickListener {
		@Override
		public void onCreate(final Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.settings);

			EditTextPreference pref
             = (EditTextPreference) findPreference("com.notriddle.budget.pin");
			pref.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);

            Preference p = findPreference("com.notriddle.budget.export");
            p.setOnPreferenceClickListener(this);
            p = findPreference("com.notriddle.budget.import");
            p.setOnPreferenceClickListener(this);
		}

        @Override
        public boolean onPreferenceClick(Preference p) {
            DialogFragment f = (p == findPreference("com.notriddle.budget.import")) ? ImportFragment.newInstance() : ExportFragment.newInstance();
            f.show(getFragmentManager(), "dialog");
            return true;
        }
	}

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(this, EnvelopesActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
