package com.nickmosher.wifilister;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

public class MainActivity extends Activity {

	public static final String TAG = "[NICK:DEBUG] ";
	public static final String PREF_KEY_VERBOSE = "verbose";
	
	private Context context;
	private WifiManager wifiManager;
	private List<WifiConfiguration>networksList;
	private SharedPreferences sharedPreferences;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        context = this;
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        sharedPreferences = getSharedPreferences("com.nickmosher.wifilister", Context.MODE_PRIVATE);
        final ListView listView = (ListView) findViewById(R.id.mainListView);
        final ArrayList<String> list = new ArrayList<String>();
        final ArrayList<WifiConfiguration> configs = new ArrayList<WifiConfiguration>();
        
        if(wifiManager.isWifiEnabled()) {
        	networksList = wifiManager.getConfiguredNetworks();
        	System.out.println(TAG + "Wifi enabled. networksList created.");
        	
        } else {
        	if(wifiManager.setWifiEnabled(true)) {
        		System.out.println(TAG + "Set Wifi Enabled");
        		
        		while(networksList == null) {
    				System.out.println(TAG + "'networksList' is NULL");
    				networksList = wifiManager.getConfiguredNetworks();
    			}
    			System.out.println(TAG + "'networksList' is not NULL");
    			
    			wifiManager.setWifiEnabled(false);
    			System.out.println(TAG + "Set Wifi Disabled");
        		
        	} else {
        		System.err.println(TAG + "Failed to enable Wifi");
        	}
        }
            
        /*
         * Retrieves the names for configured SSIDs and sets them for the text of Buttons
         */
        if(networksList != null) {
        	Iterator<WifiConfiguration> iterator = networksList.iterator();
            WifiConfiguration config;
            while(iterator.hasNext()) {
            	config = (WifiConfiguration) iterator.next();
            	list.add(config.SSID);
            	configs.add(config);
            }
        } else {
        	System.err.println(TAG + "'networksList' is NULL");
        }
        
        final StableArrayAdapter adapter = new StableArrayAdapter(this,
        		android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
        
        listView.setOnItemClickListener(new WifiConfigItemClickListener(configs));
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

    private void showDialog(String message) {
    	new AlertDialog.Builder(this)
    	.setMessage(message)
    	.setPositiveButton(android.R.string.ok, 
    			new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
    	if(item.getItemId() == R.id.action_settings) {
    		Intent intent = new Intent(this, Settings.class);
    		startActivity(intent);
    	}
    	return super.onOptionsItemSelected(item);
    }
    
    /**
     * Custom listener class with custom constructor to pass a WifiConfiguration
     * so that at the time of the button press, it can determine whether to
     * use terse or verbose mode to show the dialog.
     * @author Nick
     *
     */
    class WifiConfigItemClickListener implements AdapterView.OnItemClickListener {
		
    	ArrayList<WifiConfiguration> list;
    	
    	public WifiConfigItemClickListener(ArrayList<WifiConfiguration> wifiList) {
    		this.list = wifiList;
    	}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
    		
    		if(sharedPreferences.getBoolean(PREF_KEY_VERBOSE, false)) {
    			showDialog(list.get(position).toString());
    		} else {
    			showDialog(list.get(position).SSID);
    		}
    		System.out.println(sharedPreferences.getBoolean(PREF_KEY_VERBOSE, false));
		}
    }
}
