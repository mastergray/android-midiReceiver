package com.mastergray;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.widget.Toast;

public class PermissionRequest extends BroadcastReceiver {

	private String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";	
	private UsbManager manager;
	
	PermissionRequest (UsbManager usbManager) {
		
		this.manager = usbManager;
		
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		String action = intent.getAction();
        if (ACTION_USB_PERMISSION.equals(action)) {
            synchronized (this) {
            	
            	//	Returns authorized device from intent:
                UsbDevice device = (UsbDevice)intent.getParcelableExtra(manager.EXTRA_DEVICE);
                
                if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                    if(device != null){
                      
                    	// Saves device:
                    	midiDevice.setDevice(device);
                    	
                    	// Updates UI:
                    	Toast.makeText(context, "Device Ready!", Toast.LENGTH_SHORT).show();
                    	MainActivity.onButton.setEnabled(true);
                    	
                   } else {
                	   
                	   Toast.makeText(context, "Device Could Not Be Authorized", Toast.LENGTH_SHORT).show();
                	   
                   }
                } else {
                	Toast.makeText(context, "Permission For This Device Has been Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
		
	}
	
	public String getAction() {
		
		return ACTION_USB_PERMISSION;
		
	}
	
}
