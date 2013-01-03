package com.csmpls.twirp2;

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
import android.widget.TimePicker;
import android.widget.Toast;


public class AlarmSetActivity extends FragmentActivity {
	
	public static final String PREFS_NAME = "PrefsFile";
	public static final String ALARM_HOUR = "AlarmHour";
	public static final String ALARM_MINUTE = "AlarmMinute";
	public static final String ALARM_ENABLED = "IsAlarmEnabled";

	TimePicker timePicker;
	Button goToTweet;
	CheckBox enableCheckBox;
	boolean AlarmEnabled;

	int AlarmHour, AlarmMin;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_alarm_set);

		timePicker = (TimePicker) findViewById(R.id.timePicker);
		timePicker.setIs24HourView(true); // should adjust to user's clock
		goToTweet = (Button) findViewById(R.id.goToTweet);
		enableCheckBox = (CheckBox) findViewById(R.id.enableCheckBox);

		// Shared Preferences
	    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	    AlarmHour = settings.getInt(ALARM_HOUR, 12);
	    AlarmMin = settings.getInt(ALARM_MINUTE, 0);
	    AlarmEnabled = settings.getBoolean(ALARM_ENABLED, false);

	    //Set the UI to settings
	    timePicker.setCurrentHour(AlarmHour);
	    timePicker.setCurrentMinute(AlarmMin);
	    enableCheckBox.setChecked(AlarmEnabled);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_alarm_set, menu);
		return true;
	}

	public void setAlarm(View view) {

		 AlarmEnabled = ((CheckBox) view).isChecked();

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
			Toast.makeText(AlarmSetActivity.this, "alarm set.", Toast.LENGTH_SHORT).show();
		
		} else {
			//cancel alarm
			alarmManager.cancel(pendingIntent);
			Toast.makeText(AlarmSetActivity.this, "alarm canceled.", Toast.LENGTH_SHORT).show();
		}
		
		saveAlarmSettings();
	
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
		AlarmHour = timePicker.getCurrentHour();
		AlarmMin = timePicker.getCurrentMinute();
		
		Calendar nextAlarm = Calendar.getInstance();
		nextAlarm.set(Calendar.HOUR_OF_DAY, AlarmHour);
		nextAlarm.set(Calendar.MINUTE, AlarmMin);
		nextAlarm.set(Calendar.SECOND, 0);
		nextAlarm.set(Calendar.MILLISECOND, 0);

		if (AlarmHour <= CurHour) { if (AlarmMin <= CurMin) { nextAlarm.add(Calendar.DATE, 1); } }
		return nextAlarm.getTimeInMillis();

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
    
    public static class TimePickerFragment extends DialogFragment
                            implements TimePickerDialog.OnTimeSetListener {

	    @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        // Use the current time as the default values for the picker
	        final Calendar c = Calendar.getInstance();
	        int hour = c.get(Calendar.HOUR_OF_DAY);
	        int minute = c.get(Calendar.MINUTE);

	        // Create a new instance of TimePickerDialog and return it
	        return new TimePickerDialog(getActivity(), this, hour, minute,
	                DateFormat.is24HourFormat(getActivity()));
	    }

	    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
	    	
	    	// cancel past alarm
	    	
	    	// set new alarm
	    	
	    	// save settings to preferences
	    	
	    	// set displayed time
	    		
	    }
}

}

