package com.example.hoang.mymediaplayer.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hoang.mymediaplayer.Activity.MainActivity;
import com.example.hoang.mymediaplayer.Model.SongsModel;
import com.example.hoang.mymediaplayer.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoang on 3/1/2016.
 */
public class ListSongsAdapter extends RecyclerView.Adapter<ListSongsAdapter.SongViewHolder> {

    private List<SongsModel> arrSongs = new ArrayList<SongsModel>();
    private Context mContext;
    private SongsModel songsModel;

    public ListSongsAdapter(Context mContext, List<SongsModel> arr){
        this.mContext = mContext;
        this.arrSongs = arr;
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.song_item, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {
        songsModel = arrSongs.get(position);
        if (songsModel.getImage() != null) {
            byte[] image = songsModel.getImage();
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            holder.songImage.setImageBitmap(bitmap);
        }
        holder.textImage.setText(songsModel.getTitle());
    }

    @Override
    public int getItemCount() {
        return arrSongs.size();
    }

    public class SongViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private CardView container;
        private ImageView songImage;
        private TextView textImage;

        public SongViewHolder(View itemView) {
            super(itemView);
            container = (CardView) itemView.findViewById(R.id.item_container);
            songImage = (ImageView) itemView.findViewById(R.id.songImage);
            textImage = (TextView) itemView.findViewById(R.id.songName);
            container.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, MainActivity.class);
            Bundle bundle = new Bundle();
            int index = getAdapterPosition();
            bundle.putInt("INDEX", index);
            intent.putExtra("DATA", bundle);
            mContext.startActivity(intent);
        }
    }
}
