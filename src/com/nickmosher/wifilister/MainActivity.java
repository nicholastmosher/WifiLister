package com.nickmosher.wifilister;

import java.util.Iterator;
import java.util.List;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends Activity {

	public static final String TAG = "[NICK:DEBUG] ";
	public static final String PREF_KEY_VERBOSE = "verbose";
	
	private Context context;
	private WifiManager wifiManager;
	private List<WifiConfiguration>networksList;
	private LinearLayout linearLayout0;
	private SharedPreferences sharedPreferences;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        context = this;
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        sharedPreferences = getSharedPreferences("com.nickmosher.wifilister", Context.MODE_PRIVATE);
        
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
        
        linearLayout0 = (LinearLayout)findViewById(R.id.mainLinearLayout);
            
        /*
         * Retrieves the names for configured SSIDs and sets them for the text of Buttons
         */
        if(networksList != null) {
        	Iterator<WifiConfiguration> iterator = networksList.iterator();
            WifiConfiguration config;
            Button aButton;
            while(iterator.hasNext()) {
            	config = (WifiConfiguration) iterator.next();
            	aButton = new Button(context);
            	aButton.setText(config.SSID); //Name of the buttons are terse
            	aButton.setOnClickListener(new WifiConfigButtonClickListener(config));
            	linearLayout0.addView(aButton);
            }
        } else {
        	System.err.println(TAG + "'networksList' is NULL");
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
    class WifiConfigButtonClickListener implements View.OnClickListener {
		
    	WifiConfiguration config;
    	
    	public WifiConfigButtonClickListener(WifiConfiguration config) {
    		this.config = config;
    	}
    	
    	@Override
		public void onClick(View v) {
    		if(sharedPreferences.getBoolean(PREF_KEY_VERBOSE, false)) {
    			showDialog(config.toString());
    		} else {
    			showDialog(config.SSID);
    		}
    		System.out.println(sharedPreferences.getBoolean(PREF_KEY_VERBOSE, false));
		}
    }
}
