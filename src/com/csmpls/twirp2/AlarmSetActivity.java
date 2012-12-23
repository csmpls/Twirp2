package com.csmpls.twirp2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TimePicker;
import android.widget.Button;
import android.view.View;

public class AlarmSetActivity extends Activity {

	TimePicker timePicker;
	Button goToTweet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_set);

		timePicker = (TimePicker) findViewById(R.id.timePicker);
		goToTweet = (Button) findViewById(R.id.goToTweet);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_alarm_set, menu);
		return true;
	}

	public void goToTweetActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    

}
