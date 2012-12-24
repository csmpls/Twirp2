package com.csmpls.twirp2;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

public class AlarmSoundService extends Service implements MediaPlayer.OnPreparedListener {

    MediaPlayer mMediaPlayer = null;
	AudioManager am;

    @Override
    public void onCreate() {

//        // get audio focus for alarm
//        am = mContext.getSystemService(Context.AUDIO_SERVICE);
//        int result = am.requestAudioFocus(afChangeListener,
//                    // Use the alarm stream.
//                    AudioManager.STREAM_ALARM,
//                    // Request permanent focus.
//                    AudioManager.AUDIOFOCUS_GAIN);
//
//        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            // initialize media player
            mMediaPlayer = MediaPlayer.create(this, R.raw.alarm_file);
            mMediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.prepareAsync(); // prepare async to not block main thread
        //}

            Toast.makeText(this, "sound should be triggered.", Toast.LENGTH_LONG).show();
    }

    /** Called when MediaPlayer is ready */
    public void onPrepared(MediaPlayer player) {
        player.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        if (mMediaPlayer != null) mMediaPlayer.release();
    }


    //binding blocks

    public class LocalBinder extends Binder {
        AlarmSoundService getService() {
            return AlarmSoundService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();


}


