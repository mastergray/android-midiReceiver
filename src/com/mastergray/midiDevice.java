package com.mastergray;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.hardware.usb.UsbRequest;

public class midiDevice {
	
	public static UsbDevice device;
	public static UsbInterface intf;
	public static UsbEndpoint Midi_IN;
	public static UsbEndpoint Midi_OUT;
	public static UsbDeviceConnection connection;
	
	public static UsbDevice get(HashMap<String, UsbDevice> deviceList) {
	
		if (deviceList.size() > 0) {
		
			ArrayList<UsbDevice> devices = toArrayList(deviceList);
			
				for (int i = 0; i < devices.size(); i++) {
			       
					UsbDevice device = devices.get(i);
					UsbInterface intf = setInterface(device);
					
						// Check for MIDI interface:
						if (intf != null) {
							
							//	Check for MIDI In:
							UsbEndpoint epOut = setEndpointIn(intf);
							
								if (epOut != null) {
									
									midiDevice.device = device;
									return device;
									
								}
							
						}
			        
			    }
		
		}
		
		return null;
	}
	
	// Converts hashmap to arraylist:
	public static ArrayList<UsbDevice> toArrayList(HashMap<String, UsbDevice> deviceList) {
		
		ArrayList<UsbDevice> devices = new ArrayList<UsbDevice>();
		devices.addAll(deviceList.values()); 
		
		return devices;
		
	}
	
	public static void setDevice (UsbDevice device ) {
		
		midiDevice.device = device;
		
	}
	
	public static UsbDevice getDevice () {
		
		return device;
		
	}
	
	public static UsbInterface setInterface(UsbDevice device) {
		
		int count = device.getInterfaceCount();
		
			for (int i = 0; i < count; i++) {
		        UsbInterface intf = device.getInterface(i);
		        if (intf.getInterfaceClass() == 1 && intf.getInterfaceSubclass() == 3) {
		        	
		        	midiDevice.intf = intf;
		        	return intf;
		        	
		        }
		    }
		
		return null;
		
	}
	
	public static UsbEndpoint setEndpointIn(UsbInterface intf) {
		
		int count = intf.getEndpointCount(); 
			
			 for (int i = 0; i < count ; i++) {
		        UsbEndpoint ep = intf.getEndpoint(i);
		        if (ep.getType() == UsbConstants.USB_ENDPOINT_XFER_BULK && ep.getDirection() == UsbConstants.USB_DIR_IN) {
		        	
		        	midiDevice.Midi_IN = ep;
		        	return ep;
		        }
			}
			
		return null;
		
	}
	
	public static UsbEndpoint setEndpointOut(UsbInterface intf) {
		
		int count = intf.getEndpointCount(); 
			
			 for (int i = 0; i < count ; i++) {
		        UsbEndpoint ep = intf.getEndpoint(i);
		        if (ep.getType() == UsbConstants.USB_ENDPOINT_XFER_BULK && ep.getDirection() == UsbConstants.USB_DIR_OUT) {
		        	
		        	midiDevice.Midi_OUT = ep;
		        	return ep;
		        }
			}
			
		return null;
		
	}
	
	public static UsbInterface getMidiInterface() {
		
		return  midiDevice.intf;
		
	}
	
	public static UsbEndpoint getMidi_OUT () {
		
		return midiDevice.Midi_OUT;
		
	}
	
	public static UsbEndpoint getMidi_IN () {
		
		return midiDevice.Midi_IN;
		
	}
	
	public static void setConnection(UsbManager manager, UsbDevice device) {
		
		connection = manager.openDevice(device); 
		connection.claimInterface(intf, true);
		
	}
	
	public static void closeConnection() {
		
		connection.close();
		
	}
	
	public static UsbRequest getRequest () {
		
		
		return connection.requestWait();
		
	}
	
	public static byte[] sendRequest() {
		
		byte[] midiData = new byte[4];
		int transfer = connection.bulkTransfer(Midi_IN, midiData, 4, 0);
	
			if (transfer > 0) {
				
				return midiData;
			}
		
		
			return null;
		
	}
	
	public static String getMidiMessage(byte[] midiData) {
		
		if (midiData != null) {
		
			int command = midiData[0] & 0xFF;
	        int channel = midiData[1] & 0xFF;
	        int data1   = midiData[2] & 0xFF;
	        int data2   = midiData[3] & 0xFF;

	        
	        if (command != 15) {
	        
	        	return command + ":" + channel + ":" + data1 + ":" + data2;
	        
	        }
		
		}
		
		return null;
		
	}
	
	public static String getMidiNote(int noteValue) {
		
		switch (noteValue) {
			case 21:
				return "A0";
			case 22:
				return "A#0";
			case 23:
				return "B0";
			
			case 24:
				return "C1";
			case 25:
				return "C#1";
			case 26:
				return "D1";
			case 27:
				return "D#1";
			case 28:
				return "E1";
			case 29:
				return "F1";
			case 30:
				return "F#1";
			case 31:
				return "G1";
			case 32:
				return "G#1";
			case 33:
				return "A1";
			case 34:
				return "A#1";
			case 35:
				return "B1";
			
			case 36:
				return "C2";
			case 37:
				return "C#2";
			case 38:
				return "D2";
			case 39:
				return "D#2";
			case 40:
				return "E2";
			case 41:
				return "F2";
			case 42:
				return "F#2";
			case 43:
				return "G2";
			case 44:
				return "G#2";
			case 45:
				return "A2";
			case 46:
				return "A#2";
			case 47:
				return "B2";
				
			case 48:
				return "C3";
			case 49:
				return "C#3";
			case 50:
				return "D3";
			case 51:
				return "D#3";
			case 52:
				return "E3";
			case 53:
				return "F3";
			case 54:
				return "F#3";
			case 55:
				return "G3";
			case 56:
				return "G#3";
			case 57:
				return "A3";
			case 58:
				return "A#3";
			case 59:
				return "B3";
				
			case 60:
				return "C4 (Middle C)";
			case 61:
				return "C#4";
			case 62:
				return "D4";
			case 63:
				return "D#4";
			case 64:
				return "E4";
			case 65:
				return "F4";
			case 66:
				return "F#4";
			case 67:
				return "G4";
			case 68:
				return "G#4";
			case 69:
				return "A4";
			case 70:
				return "A#4";
			case 71:
				return "B4";
				
			case 72:
				return "C5";
			case 73:
				return "C#5";
			case 74:
				return "D5";
			case 75:
				return "D#5";
			case 76:
				return "E5";
			case 77:
				return "F5";
			case 78:
				return "F#5";
			case 79:
				return "G5";
			case 80:
				return "G#5";
			case 81:
				return "A5";
			case 82:
				return "A#5";
			case 83:
				return "B5";
				
			case 84:
				return "C6";
			case 85:
				return "C#6";
			case 86:
				return "D6";
			case 87:
				return "D#6";
			case 88:
				return "E6";
			case 89:
				return "F6";
			case 90:
				return "F#6";
			case 91:
				return "G6";
			case 92:
				return "G#6";
			case 93:
				return "A6";
			case 94:
				return "A#6";
			case 95:
				return "B6";
				
			case 96:
				return "C7";
			case 97:
				return "C#7";
			case 98:
				return "D7";
			case 99:
				return "D#7";
			case 100:
				return "E7";
			case 101:
				return "F7";
			case 102:
				return "F#7";
			case 103:
				return "G7";
			case 104:
				return "G#7";
			case 105:
				return "A7";
			case 106:
				return "A#7";
			case 107:
				return "B7";
			
			case 108:
				return "C8";
				
			default:
			return "Unknown Note";
		}
	}

}
