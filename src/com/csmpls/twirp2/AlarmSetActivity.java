package com.csmpls.twirp2;

import java.util.Calendar;
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

	private PendingIntent pendingIntent;

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

		Intent myIntent = new Intent(AlarmSetActivity.this, MainActivity.class);
  		pendingIntent = PendingIntent.getService(AlarmSetActivity.this, 0, myIntent, 0);


		AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

		Calendar nextAlarm = Calendar.getInstance();
		nextAlarm.add(Calendar.DAY, 1);
		nextAlarm.set(Calendar.HOUR, timePicker.getCurrentHour());
		nextAlarm.set(Calendar.MINUTE, timePicker.getCurrentMinute());
		long triggerInMillis = daysBetween(Calendar.getInstance(), nextAlarm);

		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, 
			triggerInMillis(),
			86400000,
			pendingIntent);
     
		Toast.makeText(AlarmSetActivity.this, "alarm set.", Toast.LENGTH_LONG).show();

	}
	

	public void goToTweetActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public static long daysBetween(final Calendar startDate, final Calendar endDate) {  
		 
		 int MILLIS_IN_DAY = 86400000;  
		 long endInstant = endDate.getTimeInMillis();  
		 int presumedDays = (int) ((endInstant - startDate.getTimeInMillis()) / MILLIS_IN_DAY);  
		 Calendar cursor = (Calendar) startDate.clone();  
		 cursor.add(Calendar.DAY_OF_YEAR, presumedDays);  
		 long instant = cursor.getTimeInMillis();  
		 if (instant == endInstant)  
		  return presumedDays;  
		 final int step = instant < endInstant ? 1 : -1;  
		 do {  
		  cursor.add(Calendar.DAY_OF_MONTH, step);  
		  presumedDays += step;  
		 } while (cursor.getTimeInMillis() != endInstant);  
		 return presumedDays;  
}  

    

}
