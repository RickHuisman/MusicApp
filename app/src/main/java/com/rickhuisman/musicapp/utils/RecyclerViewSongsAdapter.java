package com.rickhuisman.musicapp.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rickhuisman.musicapp.R;
import com.rickhuisman.musicapp.model.LocalSong;

import java.util.List;

/**
 * Created by rickh on 1/7/2018.
 */

public class RecyclerViewSongsAdapter extends RecyclerView.Adapter<RecyclerViewSongsAdapter.MyViewHolder> {

    private Context context;

    int selected_position = RecyclerView.NO_POSITION;

    private List<LocalSong> localSongsList;

    private AdapterListener mListener;

    public interface AdapterListener {
        void onClick(int selectedPos);
    }

    public void setListener(AdapterListener listener) {
        this.mListener = listener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title, artist;
        public ImageView cover, play;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.text_view_title);
            artist = (TextView) view.findViewById(R.id.text_view_artist);
            cover = (ImageView) view.findViewById(R.id.image_view_cover);
            play = (ImageView) view.findViewById(R.id.image_view_options);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (getAdapterPosition() == RecyclerView.NO_POSITION) return;
            notifyItemChanged(selected_position);
            selected_position = getAdapterPosition();
            notifyItemChanged(selected_position);

            mListener.onClick(selected_position);
        }
    }


    public RecyclerViewSongsAdapter(List<LocalSong> localSongsList, Context context) {
        this.context = context;
        this.localSongsList = localSongsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.local_song_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        LocalSong localSong = localSongsList.get(position);
        holder.title.setText(localSong.getTitle());
        holder.artist.setText(localSong.getArtist());

        // Here I am just highlighting the background
        holder.itemView.setBackgroundColor(selected_position == position ? context.getResources().getColor(R.color.colorSelector) : Color.TRANSPARENT);
    }

    @Override
    public int getItemCount() {
        return localSongsList.size();
    }
}
