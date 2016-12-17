package com.mastergray;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	TextView consoleView;
	static Button onButton;
	static Button offButton;
	static TextView noteView;
	Button deviceButton;
	UsbManager deviceManager;
	
	private static PermissionRequest usbReceiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//	Initialize UI:
		consoleView = (TextView) findViewById(R.id.consoleView);
		deviceButton = (Button) findViewById(R.id.deviceButton);
		onButton = (Button) findViewById(R.id.onButton);
		offButton = (Button) findViewById(R.id.offButton);
		
		//	Enable scrolling for text view:
		consoleView.setMovementMethod(new ScrollingMovementMethod());
		
		// Initialize device:
		deviceButton.setOnClickListener( new View.OnClickListener() {
			
			@Override
            public void onClick(View v) {
				
				//	Get devices
				deviceManager = (UsbManager) getSystemService(Context.USB_SERVICE);
				
				//	Find midi device
				UsbDevice device = midiDevice.get(deviceManager.getDeviceList());
				
				if (device != null) {
					
					//	Request permission to use that MIDI device
					usbReceiver = new PermissionRequest(deviceManager);
			 		String usbPermission = usbReceiver.getAction();
			 	
			 		//	Register broadcast for making permission request:
			 		PendingIntent PermissionIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(usbPermission), 0);
			 		IntentFilter filter = new IntentFilter(usbPermission);
			 		registerReceiver(usbReceiver, filter);
			 		
			 		// Launch prompt
			 		deviceManager.requestPermission(device, PermissionIntent);
					
				} else {
					
					Toast.makeText(getApplicationContext(), "No MIDI Device Found", Toast.LENGTH_SHORT).show();
				
				}
					
            	
            }
		});
		
		
		// Turn MIDI capture on:
		onButton.setOnClickListener( new View.OnClickListener() {
			
			@Override
            public void onClick(View v) {
			
				// Update UI:
				onButton.setEnabled(false);
				deviceButton.setEnabled(false);
				offButton.setEnabled(true);
				consoleView.setEnabled(true);
				consoleView.setText("Running...");
				consoleView.setGravity(Gravity.START);
				
				//	Creates connection and claims interface:
            	midiDevice.setConnection(deviceManager, midiDevice.getDevice());
				
				ThreadManager.start(consoleView);
				System.out.println("Started MIDI capture...");
				
			}
		});
		
		// Turn MIDI capture off:
		offButton.setOnClickListener( new View.OnClickListener() {
			
			@Override
            public void onClick(View v) {
			
				// Update UI:
				onButton.setEnabled(true);
				deviceButton.setEnabled(true);
				offButton.setEnabled(false);
				consoleView.setEnabled(false);
				
				ThreadManager.stop();
				System.out.println("Stopped MIDI capture");
				
				
				
			}
		});
		
		
	}
	
	protected void onPause(Bundle savedInstanceState) {
	
		 unregisterReceiver(usbReceiver);
		
	}
	
	protected void onDestroy(Bundle savedInstanceState) {
		
		 unregisterReceiver(usbReceiver);
		
	}
	
	protected void onResume(Bundle savedInstanceState) {
		
		IntentFilter filter = new IntentFilter();
	    filter.addAction(usbReceiver.getAction());
	    registerReceiver(usbReceiver, filter);  
		
	}

	protected void onRestart(Bundle savedInstanceState) {
		
		//		Get devices
		deviceManager = (UsbManager) getSystemService(Context.USB_SERVICE);
		
		//	Find midi device
		UsbDevice device = midiDevice.get(deviceManager.getDeviceList());
		
		if (device != null) {
			
			//	Request permission to use that MIDI device
			usbReceiver = new PermissionRequest(deviceManager);
	 		String usbPermission = usbReceiver.getAction();
	 	
	 		//	Register broadcast for making permission request:
	 		PendingIntent PermissionIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(usbPermission), 0);
	 		IntentFilter filter = new IntentFilter(usbPermission);
	 		registerReceiver(usbReceiver, filter);
	 		
	 		// Launch prompt
	 		deviceManager.requestPermission(device, PermissionIntent);
			
		} else {
			
			Toast.makeText(getApplicationContext(), "No MIDI Device Found", Toast.LENGTH_SHORT).show();
		
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
