package com.csmpls.twirp2;

import java.util.Calendar;
import java.util.Date;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TimePicker;
import android.widget.Button;
import android.view.View;
import android.widget.Toast;

public class AlarmSetActivity extends Activity {

	TimePicker timePicker;
	Button setAlarm;
	Button goToTweet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_set);

		setAlarm = (Button) findViewById(R.id.setAlarm);
		timePicker = (TimePicker) findViewById(R.id.timePicker);
		goToTweet = (Button) findViewById(R.id.goToTweet);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_alarm_set, menu);
		return true;
	}

	public void setAlarm(View view) {

		Intent myIntent = new Intent(getBaseContext(), AlarmReceiver.class);
    	PendingIntent pendingIntent = PendingIntent.getBroadcast(
    		getBaseContext(), 0, myIntent, 0);
  
		AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, 
			findNextAlarmInMillis(),
			86400000,
			pendingIntent);
     
		Toast.makeText(AlarmSetActivity.this, "alarm set.", Toast.LENGTH_LONG).show();

		finish();

	}

	public long findNextAlarmInMillis() {

		Calendar calendar = Calendar.getInstance();
		int CurHour = calendar.get(Calendar.HOUR);
		int CurMin = calendar.get(Calendar.MINUTE);
		
		Calendar nextAlarm = Calendar.getInstance();
		int AlarmHour = timePicker.getCurrentHour();
		int AlarmMin = timePicker.getCurrentMinute();
		nextAlarm.set(Calendar.HOUR_OF_DAY, AlarmHour);
		nextAlarm.set(Calendar.MINUTE, AlarmMin);
		nextAlarm.set(Calendar.SECOND, 0);
		nextAlarm.set(Calendar.MILLISECOND, 0);

		if (AlarmHour <= CurHour) { if (AlarmMin <= CurMin) { calendar.add(Calendar.HOUR, 24); } } 

		return nextAlarm.getTimeInMillis();

	}
	

	public void goToTweetActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    

}
