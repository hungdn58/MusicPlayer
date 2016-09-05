package com.example.hoang.mymediaplayer.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.example.hoang.mymediaplayer.Adapter.ListSongsAdapter;
import com.example.hoang.mymediaplayer.Model.SongsModel;
import com.example.hoang.mymediaplayer.R;
import com.example.hoang.mymediaplayer.Utilities.AsyncResponse;
import com.example.hoang.mymediaplayer.Utilities.PlayAudioService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ListSongsActivity extends AppCompatActivity implements AsyncResponse{

    private RecyclerView recyclerView;
    private ListSongsAdapter adapter;
    private ArrayList<SongsModel> arrSongs = new ArrayList<SongsModel>();
    private PlayAudioService myService;
    private Intent serviceIntent;
    private FloatingActionButton fb;


    private static final String JSON_URL = "http://musicservice.hol.es/musicservice/getAllSongs.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_songs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getControls();

    }

    public void getControls(){
        recyclerView = (RecyclerView) findViewById(R.id.listSongs);
        getJSON(JSON_URL);
        adapter = new ListSongsAdapter(this, arrSongs);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        serviceIntent = new Intent(ListSongsActivity.this, PlayAudioService.class);
        fb = (FloatingActionButton) findViewById(R.id.fab);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRecyclerview();
            }
        });
    }


    @Override
    public void processFinish(String output) {
        try {
            Log.d("MUSICSERVICE", output);
            JSONObject jsonObject = new JSONObject(output);
            JSONArray songArray = jsonObject.getJSONArray("songs");
            for (int i = 0; i < songArray.length(); i++){
                JSONObject song = songArray.getJSONObject(i);
                SongsModel songsModel = new SongsModel();
                songsModel.setTitle(song.getString("title"));
                songsModel.setId(song.getInt("id"));
                songsModel.setPath(song.getString("url"));
                songsModel.setLyric(song.getString("lyric"));
                songsModel.setImage(Base64.decode(song.getString("image"), Base64.DEFAULT));
                arrSongs.add(songsModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getJSON(String url){
        class GetJSON extends AsyncTask<String, Void, String> {

            ProgressDialog loading;
            private AsyncResponse listener;

            public GetJSON(AsyncResponse listener){
                this.listener=listener;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ListSongsActivity.this, "Please Wait...",null,true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                listener.processFinish(s);
                loading.dismiss();
            }

            @Override
            protected String doInBackground(String... params) {
                String uri = params[0];
                BufferedReader bufferedReader = null;

                try{
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while((json = bufferedReader.readLine())!= null){
                        sb.append(json+"\n");
                    }

                    return sb.toString().trim();
                } catch (Exception e){
                    return null;
                }
            }
        }
        GetJSON gj = new GetJSON(this);
        gj.execute(url);
    }

    private void updateRecyclerview(){
        ArrayList<SongsModel> arr = new ArrayList<SongsModel>();
        arr.addAll(arrSongs);
        adapter = new ListSongsAdapter(this, arr);
        adapter.notifyDataSetChanged();
    }
}
