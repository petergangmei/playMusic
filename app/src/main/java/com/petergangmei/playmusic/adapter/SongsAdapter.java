package com.petergangmei.playmusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.petergangmei.playmusic.ItemClickInterface;
import com.petergangmei.playmusic.MusicplayerActivity;
import com.petergangmei.playmusic.R;
import com.petergangmei.playmusic.model.Song;

import java.util.List;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.ViewHolder>  {

    List<Song> list;
    private ItemClickInterface itemClickInterface;

    public SongsAdapter(List<Song> list, ItemClickInterface itemClickInterface) {
        this.list = list;
        this.itemClickInterface = itemClickInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SongsAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Song song = list.get(position);
        holder.title.setText(song.getSongName());
        holder.singer.setText(song.getSingerName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String i = "https://firebasestorage.googleapis.com/v0/b/zianbammusic-50606.appspot.com/o/RongmeiSongs%2Faudio%3A465021588527771959?alt=media&token=d9ad13e6-28a4-4b61-b586-c4d398226147";
                itemClickInterface.onItemClick(position, song.getId(), "http://music.zianbam.com/m1.mp3", song.getSongName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title, singer;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            singer = itemView.findViewById(R.id.singer);
        }
    }
}
