package com.rickhuisman.musicapp.adapter;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rickhuisman.musicapp.R;
import com.rickhuisman.musicapp.model.Song;

import java.util.List;

public class SongsListAdapter extends RecyclerView.Adapter<SongsListAdapter.MyViewHolder> {

    private Context context;

    private List<Song> Song;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, artist;
        public ImageView cover, options;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.text_view_title);
            artist = (TextView) view.findViewById(R.id.text_view_artist);
            cover = (ImageView) view.findViewById(R.id.image_view_cover);
            options = (ImageView) view.findViewById(R.id.image_view_options);
        }
    }

    public SongsListAdapter(List<Song> Song, Context context) {
        this.context = context;
        this.Song = Song;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_list_row, parent, false);
        itemView.setOnClickListener(clickListener);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.title.setText(Song.get(position).getTitle());
        holder.artist.setText(Song.get(position).getArtist());
        Glide.with(context).load(Song.get(position).getImageUrl()).into(holder.cover);
        holder.options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, holder.options);
                popupMenu.getMenuInflater().inflate(R.menu.menu_popup_song_options, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return Song.size();
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // TODO play the song
        }
    };
}
