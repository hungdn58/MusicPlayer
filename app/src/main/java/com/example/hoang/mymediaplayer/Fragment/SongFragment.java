package com.example.hoang.mymediaplayer.Fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.hoang.mymediaplayer.R;

public class SongFragment extends Fragment {

    private ImageView image;

    public SongFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_song, container, false);
        image = (ImageView) v.findViewById(R.id.imageView);
        image.startAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.rotate_animation));
        // Inflate the layout for this fragment
        return v;
    }

}
