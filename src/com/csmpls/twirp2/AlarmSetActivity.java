package com.csmpls.twirp2;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


public class AlarmSetActivity extends FragmentActivity {
	
	public static final String PREFS_NAME = "PrefsFile";
	public static final String ALARM_HOUR = "AlarmHour";
	public static final String ALARM_MINUTE = "AlarmMinute";
	public static final String ALARM_ENABLED = "IsAlarmEnabled";

	TextView textView;
	Button goToTweet;
	CheckBox enableCheckBox;
	boolean AlarmEnabled;

	int AlarmHour;
	int AlarmMin;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_alarm_set);

		textView = (TextView) findViewById(R.id.textView);
		goToTweet = (Button) findViewById(R.id.goToTweet);
		enableCheckBox = (CheckBox) findViewById(R.id.enableCheckBox);

		// Shared Preferences
	    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	    AlarmHour = settings.getInt(ALARM_HOUR, 0);
	    AlarmMin = settings.getInt(ALARM_MINUTE, 0);
	    AlarmEnabled = settings.getBoolean(ALARM_ENABLED, false);

	    //Set the UI to settings
	    displaySavedTime();
	    enableCheckBox.setChecked(AlarmEnabled);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_alarm_set, menu);
		return true;
	}

	public void setAlarm() {

		 AlarmEnabled = enableCheckBox.isChecked();

		// set intent
		Intent myIntent = new Intent(getBaseContext(), AlarmReceiver.class);
    	PendingIntent pendingIntent = PendingIntent.getBroadcast(
    		getBaseContext(), 0, myIntent, 0);
		AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

		if (AlarmEnabled) {

			//set alarm 
			alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, 
				findNextAlarmInMillis(),
				86400000,
				pendingIntent);

			// display notice alarm was set
			Toast.makeText(AlarmSetActivity.this, "alarm set.", Toast.LENGTH_SHORT).show();
		
		} else {
			//cancel alarm
			alarmManager.cancel(pendingIntent);
		}
		
		saveAlarmSettings();

		displaySavedTime();
	
	}


	public void saveAlarmSettings() {

		// save current settings 
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit(); 
		//editor.putBoolean(ALARM_ENABLED, true); 
		editor.putInt(ALARM_HOUR, AlarmHour); 
		editor.putInt(ALARM_MINUTE, AlarmMin); 
		editor.putBoolean(ALARM_ENABLED, AlarmEnabled); 
		editor.commit();
	}

	public long findNextAlarmInMillis() {

		// set alarm
		Calendar calendar = Calendar.getInstance();
		int CurHour = calendar.get(Calendar.HOUR_OF_DAY);
		int CurMin = calendar.get(Calendar.MINUTE);
		
		Calendar nextAlarm = Calendar.getInstance();
		nextAlarm.set(Calendar.HOUR_OF_DAY, AlarmHour);
		nextAlarm.set(Calendar.MINUTE, AlarmMin);
		nextAlarm.set(Calendar.SECOND, 0);
		nextAlarm.set(Calendar.MILLISECOND, 0);

		if (AlarmHour <= CurHour) { if (AlarmMin <= CurMin) { nextAlarm.add(Calendar.DATE, 1); } }
		return nextAlarm.getTimeInMillis();

	}

	public void displaySavedTime() {

		Calendar cc = Calendar.getInstance();
		cc.set(Calendar.HOUR_OF_DAY, AlarmHour);
		cc.set(Calendar.MINUTE, AlarmMin);
		cc.set(Calendar.SECOND, 0);
		cc.set(Calendar.MILLISECOND, 0);

		SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
		String displayedTime =  sdf.format(cc.getTime());

		textView.setText(displayedTime);

	}
	

	public void goToTweetActivity(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void showTimePickerDialog(View v) {
	    DialogFragment newFragment = new TimePickerFragment();
	    newFragment.show(getSupportFragmentManager(), "timePicker");
	}

    @Override
    protected void onStop(){
      	super.onStop();

      	saveAlarmSettings();
      }
    
    public class TimePickerFragment extends DialogFragment
                            implements TimePickerDialog.OnTimeSetListener {

	    @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {

	        // Create a new instance of TimePickerDialog and return it
	        return new TimePickerDialog(getActivity(), this, AlarmHour, AlarmMin,
	                DateFormat.is24HourFormat(getActivity()));
	    }

	    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
	    	
	    	AlarmHour = hourOfDay;
	    	AlarmMin = minute;
	    	
	    	setAlarm();
	

	    }
	}
}

