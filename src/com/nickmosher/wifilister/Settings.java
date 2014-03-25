package com.nickmosher.wifilister;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
//import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;

public class Settings extends Activity {
	
	private SharedPreferences sharedPreferences;
	private LinearLayout settingsLinearLayout;
	private SettingsOnClickListener clickListener;
	private Button terseButton;
	private Button verboseButton;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		sharedPreferences = getSharedPreferences("com.nickmosher.wifilister", Context.MODE_PRIVATE);
		settingsLinearLayout = (LinearLayout) findViewById(R.id.settingsLinearLayout);
		clickListener = new SettingsOnClickListener();
		terseButton = new Button(getApplicationContext());
		verboseButton = new Button(getApplicationContext());
		
		terseButton.setText(R.string.terse);
		verboseButton.setText(R.string.verbose);
		
		terseButton.setOnClickListener(clickListener);
		verboseButton.setOnClickListener(clickListener);
		
		settingsLinearLayout.addView(terseButton);
		settingsLinearLayout.addView(verboseButton);
	}
	
	class SettingsOnClickListener implements View.OnClickListener {

		public SettingsOnClickListener() {
			
		}
		
		@Override
		public void onClick(View v) {
			if(v == terseButton) {
				sharedPreferences.edit().putBoolean(MainActivity.PREF_KEY_VERBOSE, false).commit();
				System.out.print("Pressed terse button");
				
			} else if(v == verboseButton) {
				sharedPreferences.edit().putBoolean(MainActivity.PREF_KEY_VERBOSE, true).commit();
				System.out.print("Pressed verbose button");
			}
			System.out.println("Preference verbose: " + sharedPreferences.getBoolean(MainActivity.PREF_KEY_VERBOSE, false));
		}
	}
}
