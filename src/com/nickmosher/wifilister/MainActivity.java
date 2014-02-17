package com.nickmosher.wifilister;

import java.util.Iterator;
import java.util.List;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	private Context context;
	private WifiManager wifiManager;
	private List<WifiConfiguration>networksList;
	
	private LinearLayout linearLayout0;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        context = this;
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        networksList = wifiManager.getConfiguredNetworks();
        
        linearLayout0 = (LinearLayout)findViewById(R.id.linearLayout0);
        
        Iterator<WifiConfiguration> iterator = networksList.iterator();
        WifiConfiguration config;
        while(iterator.hasNext()) {
        	config = (WifiConfiguration) iterator.next();
        	TextView textView = new TextView(this);
        	textView.setText(config.SSID);
        	linearLayout0.addView(textView);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
