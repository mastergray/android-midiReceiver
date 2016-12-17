package com.mastergray;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

public class ThreadManager {

	private static ThreadManager_Runnable runnable;
	
	public static void start(TextView outputView) {
		
		// Initializes thread:
		ThreadManager_Handler handler = new ThreadManager_Handler(outputView);
		runnable = new ThreadManager_Runnable(handler);
		Thread midiThread = new Thread(runnable);
		midiThread.start();
		
	}
	
	public static void stop() {
		
		runnable.kill();
		
	}
	
}

	class ThreadManager_Handler extends Handler {
		
		static TextView outputView;
		
		ThreadManager_Handler (TextView outputView) {
				
			ThreadManager_Handler.outputView = outputView;
				
			}
		
		 @Override
		  public void handleMessage(Message msg) {
			 
			 //	Gets thread data from message:
			 Bundle threadData = msg.getData();
			 
			 // Updates View:
			 if (threadData.getBoolean("isRunning")) {
			 
				 	outputView.setText(outputView.getText() + "\n" + threadData.get("midiOut").toString());
				 	
			 
			 }
			 
		 };
		
	}

	class ThreadManager_Runnable implements Runnable {
	
		ThreadManager_Handler handler;
		Bundle threadData = new Bundle();
		private volatile boolean isRunning = true;
		
		ThreadManager_Runnable (ThreadManager_Handler handler) {
		
			this.handler = handler;
			
		}
		
		@Override
		public void run()  {
			
			while (isRunning) {
				
					try {
				
						Thread.sleep(10);
						
						byte[] response = midiDevice.sendRequest();
						String midiMessage = midiDevice.getMidiMessage(response); 
						
						if (midiMessage != null) {
						
							//	Obtain message object from handler (more efficient than creating new instance):
							Message msg = handler.obtainMessage();
							
							//	Place data into bundle:
							threadData.putString("midiOut", midiMessage);
							threadData.putBoolean("isRunning", isRunning);
							
							//	Place bundle in message:
							msg.setData(threadData);
							
							//	Send message to handler after delay:
							handler.sendMessage(msg);
						
						} 
					
						
						try {
						
							int scrollAmount = ThreadManager_Handler.outputView .getLayout().getLineTop(ThreadManager_Handler.outputView .getLineCount()) - ThreadManager_Handler.outputView .getHeight();
					 	
					 	
							if (scrollAmount > 0) { 
						 	
						 		ThreadManager_Handler.outputView .scrollTo(0, scrollAmount); 
						 	
						 	} else {
						 		
						 		ThreadManager_Handler.outputView .scrollTo(0, 0);
						 		
						 	}
							
						} catch (Exception e) {
							
							// view not adjustable;
							
						}
					 	
						
						
						
					} catch (InterruptedException e) {
						
						System.out.println("Thread error: " + e);
						
					}
				
				
			}
		
			// Closes connection:
			midiDevice.closeConnection();
		}
		
		public void kill() {
			
			this.isRunning = false;
			
		}
	
	}
