package com.example.hoang.mymediaplayer.Utilities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.hoang.mymediaplayer.Activity.MainActivity;
import com.example.hoang.mymediaplayer.Fragment.LyricFragment;
import com.example.hoang.mymediaplayer.Model.SongsModel;
import com.example.hoang.mymediaplayer.R;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by hoang on 2/18/2016.
 */
public class PlayAudioService extends Service implements MediaPlayer.OnCompletionListener,MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener {

    private MediaPlayer mp;
    private SongsManager songManager;
    private Utilities utils;
    private int seekForwardTime = 5000; // 5000 milliseconds
    private int seekBackwardTime = 5000; // 5000 milliseconds
    private int currentSongIndex = -1;
    private boolean isRepeat = false;
    private ArrayList<SongsModel> songsList = new ArrayList<SongsModel>();
    private static boolean isRunning = false;
    Notification status;
    private final String LOG_TAG = "NotificationService";

    RemoteViews views;
    RemoteViews bigViews;

    private final IBinder mBinder = new LocalBinder();

    CallBacks activity;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    public class LocalBinder extends Binder {
        public PlayAudioService getServiceInstance(){
            return PlayAudioService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mp = new MediaPlayer();
        songManager = new SongsManager();
        utils = new Utilities();
//        songsList = songManager.getPlayList();
        mp.setOnCompletionListener(this);
        isRunning = true;
        views = new RemoteViews(getPackageName(), R.layout.status_bar);
        bigViews = new RemoteViews(getPackageName(), R.layout.status_bar_expanded);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {
            showNotification();
            Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();

        } else if (intent.getAction().equals(Constants.ACTION.PREV_ACTION)) {
            prevAudio();
            Toast.makeText(this, "Clicked Previous", Toast.LENGTH_SHORT).show();
            Log.i(LOG_TAG, "Clicked Previous");
        } else if (intent.getAction().equals(Constants.ACTION.PLAY_ACTION)) {
            playAudio();
            Toast.makeText(this, "Clicked Play", Toast.LENGTH_SHORT).show();
            Log.i(LOG_TAG, "Clicked Play");
        } else if (intent.getAction().equals(Constants.ACTION.NEXT_ACTION)) {
            nextAudio();
            Toast.makeText(this, "Clicked Next", Toast.LENGTH_SHORT).show();
            Log.i(LOG_TAG, "Clicked Next");
        } else if (intent.getAction().equals(
                Constants.ACTION.STOPFOREGROUND_ACTION)) {
            Log.i(LOG_TAG, "Received Stop Foreground Intent");
            Toast.makeText(this, "Service Stoped", Toast.LENGTH_SHORT).show();
            stopForeground(true);
            stopSelf();
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
    }

    public void registerClient(Activity activity){
        this.activity = (CallBacks)activity;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    public void playAudio(){
        if(mp.isPlaying()){
            if(mp!=null){
                mp.pause();
                // Changing button image to play button
                activity.changePlayButtonIcon(mp.isPlaying());
            }
        }else{
            // Resume song
            if(mp!=null){
                mp.start();
                // Changing button image to pause button
                activity.changePlayButtonIcon(mp.isPlaying());
            }
        }
    }
    public void nextAudio(){
        if(currentSongIndex < (songsList.size() - 1)){
            try {
                playSong(currentSongIndex + 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            currentSongIndex = currentSongIndex + 1;
        }else{
            // play first song
            try {
                playSong(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            currentSongIndex = 0;
        }
    }
    public void prevAudio(){
        if(currentSongIndex > 0){
            try {
                playSong(currentSongIndex);
            } catch (Exception e) {
                e.printStackTrace();
            }
            currentSongIndex = currentSongIndex - 1;
        }else{
            // play last song
            try {
                playSong(songsList.size() - 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            currentSongIndex = songsList.size() - 1;
        }
    }
    public void stopAudio(){

    }

    public void playSong(int songIndex)throws Exception{
        // Play song
        try {
            mp.reset();
            Log.d("MUSICSERVICEATSERVICE", "co chay nhe");
            String url = songsList.get(songIndex).getPath();
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mp.setDataSource(url);
//            mp.setDataSource(songsList.get(songIndex).getPath());
            mp.setOnErrorListener(this);
            mp.setOnPreparedListener(this);
            mp.prepareAsync();
            currentSongIndex = songIndex;
            // Displaying Song title
            String songTitle = songsList.get(songIndex).getTitle();
//            updateLyric1(songIndex);

            // Changing Button Image to pause image
            activity.changePlayButtonIcon(mp.isPlaying());
            views.setTextViewText(R.id.status_bar_track_name, songsList.get(songIndex).getTitle());
            bigViews.setTextViewText(R.id.status_bar_track_name, songsList.get(songIndex).getTitle());

//            activity.updateInterface(mp);

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getListSongs(ArrayList<SongsModel> arr){
        songsList.clear();
        songsList.addAll(arr);
    }

    public void getCurrentIndex(int index){
        this.currentSongIndex = index;
    }

    public interface CallBacks{
        public void updateInterface(MediaPlayer mp);
        public void changePlayButtonIcon(boolean isPlaying);
    }

    public static boolean isRunning()
    {
        return isRunning;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void showNotification(){

        views.setViewVisibility(R.id.status_bar_icon, View.VISIBLE);
        views.setViewVisibility(R.id.status_bar_album_art, View.GONE);
        bigViews.setImageViewBitmap(R.id.status_bar_album_art,
                Constants.getDefaultAlbumArt(this));

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Intent previousIntent = new Intent(this, PlayAudioService.class);
        previousIntent.setAction(Constants.ACTION.PREV_ACTION);
        PendingIntent ppreviousIntent = PendingIntent.getService(this, 0,
                previousIntent, 0);

        Intent playIntent = new Intent(this, PlayAudioService.class);
        playIntent.setAction(Constants.ACTION.PLAY_ACTION);
        PendingIntent pplayIntent = PendingIntent.getService(this, 0,
                playIntent, 0);

        Intent nextIntent = new Intent(this, PlayAudioService.class);
        nextIntent.setAction(Constants.ACTION.NEXT_ACTION);
        PendingIntent pnextIntent = PendingIntent.getService(this, 0,
                nextIntent, 0);

        Intent closeIntent = new Intent(this, PlayAudioService.class);
        closeIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
        PendingIntent pcloseIntent = PendingIntent.getService(this, 0,
                closeIntent, 0);

        views.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent);

        views.setOnClickPendingIntent(R.id.status_bar_next, pnextIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_next, pnextIntent);

        views.setOnClickPendingIntent(R.id.status_bar_prev, ppreviousIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_prev, ppreviousIntent);

        views.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent);

        views.setTextViewText(R.id.status_bar_artist_name, "Artist Name");
        bigViews.setTextViewText(R.id.status_bar_artist_name, "Artist Name");
        views.setTextViewText(R.id.status_bar_track_name, "Song Name");
        bigViews.setTextViewText(R.id.status_bar_track_name, "Song Name");

        bigViews.setTextViewText(R.id.status_bar_album_name, "Album Name");


        views.setImageViewResource(R.id.status_bar_play,
                R.drawable.apollo_holo_dark_pause);
        bigViews.setImageViewResource(R.id.status_bar_play,
                R.drawable.apollo_holo_dark_pause);

        status = new Notification.Builder(this).build();
        status.contentView = views;
        status.bigContentView = bigViews;
        status.flags = Notification.FLAG_ONGOING_EVENT;
        status.icon = R.drawable.ic_launcher;
        status.contentIntent = pendingIntent;
        startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, status);
    }

//    private void updateLyric1(int index){
//        String TabOfFragmentB = ((MainActivity)getApplicationContext()).getTabFragmentB();
//        LyricFragment fragmentB = (LyricFragment)((MainActivity) getApplicationContext())
//                .getSupportFragmentManager()
//                .findFragmentByTag(TabOfFragmentB);
//        fragmentB.updateLyric(songsList.get(index).getLyric());
//    }
}
