package com.nickmosher.wifilister;

import java.util.Iterator;
import java.util.List;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	private Context context;
	private LayoutInflater inflater;
	private WifiManager wifiManager;
	private List<WifiConfiguration>networksList;
	
	private LinearLayout linearLayout0;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        context = this;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        networksList = wifiManager.getConfiguredNetworks();
        
        linearLayout0 = (LinearLayout)findViewById(R.id.linearLayout0);
        
        View aView;
        
        /*
         * Loop that generates 10 View items to try to fill up some of the scrolling space
         */
        for(int i = 0; i < 10; i++) {
        	aView = inflater.inflate(R.layout.list_item, null, false);
        	((TextView) aView).setText("Filler TextView");
        	linearLayout0.addView(aView);
        }
        
        /*
         * Retrieves the names for configured SSIDs and sets them for the text of TextViews
         */
        Iterator<WifiConfiguration> iterator = networksList.iterator();
        WifiConfiguration config;
        while(iterator.hasNext()) {
        	config = (WifiConfiguration) iterator.next();
        	aView = inflater.inflate(R.layout.list_item, null, false);
        	((TextView) aView).setText(config.SSID);
        	linearLayout0.addView(aView);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
