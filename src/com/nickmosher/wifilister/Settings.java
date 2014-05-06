package com.nickmosher.wifilister;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
//import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

public class Settings extends Activity {
	
	private SharedPreferences sharedPreferences;

	private ListView settingsListView;
	private ArrayList<String> list;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		sharedPreferences = getSharedPreferences("com.nickmosher.wifilister", Context.MODE_PRIVATE);
		settingsListView = (ListView) findViewById(R.id.settingsListView);
		list = new ArrayList<String>();
		
		list.add("Terse");
		list.add("Verbose");
		
		final StableArrayAdapter adapter = new StableArrayAdapter(this,
				android.R.layout.simple_list_item_1, list);
		
		settingsListView.setAdapter(adapter);
		settingsListView.setOnItemClickListener(new SettingsItemClickListener(list));
	}
	
	private class StableArrayAdapter extends ArrayAdapter<String> {
		HashMap<String, Integer> idMap = new HashMap<String, Integer>();
		
		public StableArrayAdapter(Context context, int resId, List<String> objects) {
			super(context, resId, objects);
			for(int i = 0; i < objects.size(); ++i) {
				idMap.put(objects.get(i), i);
			}
		}
		
		public long getItemId(int position) {
			String item = getItem(position);
			return idMap.get(item);
		}
		
		public boolean hasStableIds() {
			return true;
		}
	}
	
	private class SettingsItemClickListener implements AdapterView.OnItemClickListener {

		ArrayList<String> list;
		
		public SettingsItemClickListener(ArrayList<String> list) {
			this.list = list;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if(list.get(position) == "Terse") {
				sharedPreferences.edit().putBoolean(MainActivity.PREF_KEY_VERBOSE, false).commit();
				System.out.println("Pressed terse item");
			} else if(list.get(position) == "Verbose") {
				sharedPreferences.edit().putBoolean(MainActivity.PREF_KEY_VERBOSE, true).commit();
			}
			System.out.println("Preference verbose: " + sharedPreferences.getBoolean(MainActivity.PREF_KEY_VERBOSE,  false));
		}
	}
}
