package com.example.hoang.mymediaplayer.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.hoang.mymediaplayer.Activity.MainActivity;
import com.example.hoang.mymediaplayer.Model.SongsModel;
import com.example.hoang.mymediaplayer.R;
import com.example.hoang.mymediaplayer.Utilities.AsyncResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ListFragment extends android.support.v4.app.Fragment implements AdapterView.OnItemClickListener, AsyncResponse {

    public ArrayList<SongsModel> songsList = new ArrayList<>();
    public ArrayList<SongsModel> songsListData;

    private static final String JSON_URL = "http://musicservice.hol.es/musicservice/getAllSongs.php";

    public ListFragment(){}

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
                songsList.add(songsModel);
            }
            mCallback.getListSongs(songsList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public interface SelectionListener{
        void onItemSelected(int position);
        void getListSongs(ArrayList<SongsModel> songs);
    }

    private SelectionListener mCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        songsListData = new ArrayList<>();
        getJSON(JSON_URL);
//
//        SongsManager plm = new SongsManager();
//        // get all songs from sdcard
//        this.songsList = plm.getPlayList();
//
//        // looping through playlist
//        for (int i = 0; i < songsList.size(); i++) {
//            // creating new HashMap
//            SongsModel song = songsList.get(i);
//
//            // adding HashList to ArrayList
//            songsListData.add(song);
//        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ListView listSongs;
        View view;
        // Inflate the layout for this fragment
        view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_list, container, false);
        listSongs = (ListView) view.findViewById(R.id.listSongs);
        ArrayAdapter<SongsModel> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, songsList);
        listSongs.setAdapter(adapter);
//        Toast.makeText(getActivity(), songsList.size() + " size ", Toast.LENGTH_LONG).show();
        listSongs.setOnItemClickListener(this);
        return view;
    }


    private void getJSON(String url){
        class GetJSON extends AsyncTask<String, Void, String>{

            ProgressDialog loading;
            private AsyncResponse listener;

            public GetJSON(AsyncResponse listener){
                this.listener=listener;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(), "Please Wait...",null,true,true);
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
                BufferedReader bufferedReader ;

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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Make sure that the hosting Activity has implemented
        // the SelectionListener callback interface. We need this
        // because when an item in this ListFragment is selected,
        // the hosting Activity's onItemSelected() method will be called.

        try {

            mCallback = (SelectionListener) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement SelectionListener");
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mCallback.onItemSelected(position);
        String TabOfFragmentB = ((MainActivity)getActivity()).getTabFragmentB();
        LyricFragment fragmentB = (LyricFragment)getActivity()
                .getSupportFragmentManager()
                .findFragmentByTag(TabOfFragmentB);
        fragmentB.updateLyric(songsList.get(position).getLyric());
    }
}
