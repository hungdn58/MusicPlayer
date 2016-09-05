package com.example.hoang.mymediaplayer.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hoang.mymediaplayer.Activity.MainActivity;
import com.example.hoang.mymediaplayer.R;

public class LyricFragment extends Fragment {

    TextView lyric;
    private SharedPreferences prefs;

    public LyricFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        lyric.setText(prefs.getString("lyric", "No Lyric"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_lyric, container, false);
        lyric = (TextView) view.findViewById(R.id.lyric);

        String myTag = getTag();

        ((MainActivity)getActivity()).setTabFragmentB(myTag);
        prefs  = getActivity().getPreferences(Context.MODE_PRIVATE);
        lyric.setText(prefs.getString("lyric", "No Lyric"));
        return view;
    }

    public void updateLyric(String text){
        Log.d("MUSICSERVICE", text);
//        Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("lyric", text);
        editor.apply();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
//        if (this.isVisible()) {
//            // If we are becoming invisible, then...
////            Toast.makeText(getActivity(), prefs.getString("lyric", "No Lyric"), Toast.LENGTH_LONG).show();
//        }
    }
}
