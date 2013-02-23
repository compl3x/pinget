package com.michaelcowell.pinget;

import android.os.Bundle;
import android.app.Activity;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.widget.TextView;

public class AboutApp extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_app);
		
		TextView tv = (TextView)findViewById(R.id.textView2);
		try {
			tv.setText("Version: " + getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		TextView tv3 = (TextView)findViewById(R.id.textView3);
		tv3.setText("Extract/recover Play Store PIN\n\nCreated by @Complex360 (cyr0s on xda)\n\nThanks: zanderman112, trter10 and stericson");
		tv3.append(Html.fromHtml("<br><br><a href=\"https://github.com/compl3x/pinget\">pinget source code</a><br><br><a href=\"http://twitter.com/complex360\">@Complex360</a><br><br><a href=\"http://compl3x.wordpress.com\">Wordpress</a>"));
		tv3.setMovementMethod(LinkMovementMethod.getInstance());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_about_app, menu);
		return true;
	}

}
