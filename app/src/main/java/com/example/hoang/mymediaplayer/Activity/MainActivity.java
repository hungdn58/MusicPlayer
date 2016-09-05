package com.example.hoang.mymediaplayer.Activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.hoang.mymediaplayer.Adapter.ViewPagerAdapter;
import com.example.hoang.mymediaplayer.Fragment.ListFragment;
import com.example.hoang.mymediaplayer.Fragment.LyricFragment;
import com.example.hoang.mymediaplayer.Fragment.SongFragment;
import com.example.hoang.mymediaplayer.Model.SongsModel;
import com.example.hoang.mymediaplayer.Utilities.Constants;
import com.example.hoang.mymediaplayer.Utilities.PlayAudioService;
import com.example.hoang.mymediaplayer.R;
import com.viewpagerindicator.PageIndicator;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener , PlayAudioService.CallBacks, ListFragment.SelectionListener{

    private ViewPager viewPager;
    private PageIndicator pageIndicator;
//    private ViewPagerAdapter viewPagerAdapter;
    private ImageView btn_play, btn_prev, btn_next, btn_stop;
    private ToggleButton btn_repeat;
    private SeekBar songProgressBar;
    private PlayAudioService myService;
    private Boolean isPlaying = false;
    private ArrayList<SongsModel> songsArray = new ArrayList<SongsModel>();
    private String TabFragmentB;
    private SongsModel songsModel;
    private Intent serviceIntent;
    private int index;

    public void setTabFragmentB(String t){
        TabFragmentB = t;
    }

    public String getTabFragmentB(){
        return TabFragmentB;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serviceIntent = new Intent(MainActivity.this, PlayAudioService.class);
        serviceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
        startService(serviceIntent);
        bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);
        try {
            getControls();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getControls(){
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        pageIndicator = (PageIndicator) findViewById(R.id.pagerindicator);
        btn_next = (ImageView) findViewById(R.id.btn_next);
        btn_play = (ImageView) findViewById(R.id.btn_play);
        btn_prev = (ImageView) findViewById(R.id.btn_prev);
        btn_stop = (ImageView) findViewById(R.id.btn_stop);
        btn_repeat = (ToggleButton) findViewById(R.id.btn_reapeat);

        songProgressBar = (SeekBar) findViewById(R.id.SeekBar);

        setupViewPager(viewPager);
        pageIndicator.setViewPager(viewPager);
        songProgressBar.setOnSeekBarChangeListener(this);
        btn_play.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        btn_prev.setOnClickListener(this);
        btn_stop.setOnClickListener(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("DATA");
        index = bundle.getInt("INDEX");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void setupViewPager(final ViewPager viewPager) {
        final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new ListFragment(), "List");
        viewPagerAdapter.addFragment(new SongFragment(), "Song");
        viewPagerAdapter.addFragment(new LyricFragment(), "Lyrics");
        viewPager.setAdapter(viewPagerAdapter);
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PlayAudioService.LocalBinder binder = (PlayAudioService.LocalBinder) service;
            myService = binder.getServiceInstance();
            myService.registerClient(MainActivity.this);
            Log.d("MUSIC", myService == null ? "chs" : "dc ma nhi");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(mConnection);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_play:
                try {
                    myService.getListSongs(songsArray);
                    myService.playSong(index);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                myService.playAudio();
                break;
            case R.id.btn_next:
                myService.nextAudio();
                break;
            case R.id.btn_prev:
                myService.prevAudio();
                break;
            case R.id.btn_stop:
                break;
        }
    }

    @Override
    public void updateInterface(MediaPlayer mp) {

    }

    @Override
    public void changePlayButtonIcon(boolean isPlaying) {
        this.isPlaying = isPlaying;
        if(isPlaying){
            btn_play.setImageResource(R.drawable.btn_pause);
        }else{
            btn_play.setImageResource(R.drawable.btn_play);
        }
    }

    @Override
    public void onItemSelected(int position) {
//        myService.getCurrentIndex(position);
        myService.getListSongs(songsArray);
        try {
            myService.playSong(position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getListSongs(ArrayList<SongsModel> songs) {
        songsArray.addAll(songs);
    }
}
