package com.michaelcowell.pinget;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import com.stericson.RootTools.Command;
import com.stericson.RootTools.RootTools;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		//Check if finsky.xml exists - This is where we extract the PIN from
		File fin = new File("/data/data/com.android.vending/shared_prefs/finsky.xml");
		if (!fin.exists())
		{
		    AlertDialog.Builder builder = new AlertDialog.Builder(this);
		        builder.setTitle("FATAL ERROR");
		    builder.setMessage("You do not appear to have a finsky.xml file, this application is useless.");
		    builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					android.os.Process.killProcess(android.os.Process.myPid());
					
				}
		    });
		    builder.show();
			
		}
		
		// BusyBox is needed for the cut command, root is needed to run the cut command
		if (!RootTools.isRootAvailable())
		{
			AlertDialog ad = new AlertDialog.Builder(
                    this).create();
			
			ad.setTitle(Html.fromHtml(("<b><font color=\"red\">Error: Root Required!</b></font>")));
			ad.setMessage(Html.fromHtml("You don't seem to be on a rooted device. This application <b>REQUIRES</b> root."));
			ad.show();
			
			
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		

		
		//Bind asynctask to button
		Button bt = (Button)findViewById(R.id.button1);
		bt.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				 new ExtractTask().execute("");
				 
			}
		});
	}
	private class ExtractTask extends AsyncTask<String, Integer, Long> {
		//Compared so that "None" can be outputted if no PIN exists
		String pin = "PIN Code: None";
		
		@Override
		protected Long doInBackground(String... params) {
			//Old command ripped straight from the bash script - Using it would require BusyBox (cut)
			//Command command = new Command(0, "grep '<string name=\"pin_code\"' /data/data/com.android.vending/shared_prefs/finsky.xml | cut -f2 -d\">\"|cut -f1 -d\"<\"")
			Command command = new Command(0, "cat /data/data/com.android.vending/shared_prefs/finsky.xml")
			{
			        @Override
			        public void output(int id, String line)
			        {
			        	//Assign pin to later print
			        	pin += line;
			        	
			        }
			};
			try {
				//Run command
				RootTools.getShell(true).add(command).waitForFinish();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (TimeoutException e) {
				e.printStackTrace();
			}
			return null;

		
		}
		protected void onPostExecute(Long result) {
			
			   //Display the result of PIN extraction
			   TextView tv = (TextView)findViewById(R.id.textView1);
			   //Don't print if it doesn't exist
			   if (pin.contains("pin_code"))
			   {
				   //Parsing pin
				  String[] derp = pin.split("pin_code\">");
				  tv.setText("PIN Code: " + derp[1].substring(0,derp[1].indexOf("</string>")));
			   }
			   else
			   {
				   tv.setText("PIN Code: None");
			   }
		}
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		//Clear and add menu item
		menu.clear();
		menu.add("About");
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item)
    {
		Intent about = new Intent(getApplicationContext(), AboutApp.class);
		startActivity(about);
		
		return false;
    }
}
