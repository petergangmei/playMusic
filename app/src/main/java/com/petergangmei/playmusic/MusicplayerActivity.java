package com.petergangmei.playmusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.petergangmei.playmusic.adapter.SongsAdapter;
import com.petergangmei.playmusic.model.Song;

import java.util.ArrayList;
import java.util.List;

public class MusicplayerActivity extends AppCompatActivity implements ItemClickInterface {
    private RecyclerView recyclerView;
    private SongsAdapter songsAdapter;
    private List<Song> songList;
    private JcPlayerView jcPlayerView;
    ArrayList<JcAudio> jcAudios;
    private TextView upload;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference refSongs = db.collection("Songs");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musicplayer);
        recyclerView =findViewById(R.id.recyclerView);
        jcPlayerView = (JcPlayerView) findViewById(R.id.jcplayerView);
        upload = findViewById(R.id.upload);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UploadActivity.class));
            }
        });
    }

    public void payThisSong(String id){
        Toast.makeText(this, "Play this song id:"+id, Toast.LENGTH_SHORT).show();
    }
    private void loadSongs() {
        songList = new ArrayList<>();
        jcAudios = new ArrayList<>();


        refSongs.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                songList.clear();
                if (!queryDocumentSnapshots.isEmpty()){
                    for (DocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                        Song song = documentSnapshot.toObject(Song.class);
                        songList.add(song);
                        jcAudios.add(JcAudio.createFromURL(song.getSongName(),song.getSongUrl()));

                    }
                    songsAdapter = new SongsAdapter(songList, MusicplayerActivity.this);
                    recyclerView.setAdapter(songsAdapter);
                    jcPlayerView.initPlaylist(jcAudios, null);
                    jcPlayerView.createNotification();
                }else {
                    Toast.makeText(MusicplayerActivity.this, "No music!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onItemClick(int position, String id, String songURL, String title) {
//        Toast.makeText(this, "play:"+title, Toast.LENGTH_LONG).show();
//        MediaPlayer em2 = MediaPlayer.create(this, Uri.parse(songURL));
//        em2.setAudioStreamType(AudioManager.STREAM_MUSIC);
//        em2.start();
        jcPlayerView.playAudio(jcAudios.get(position));
    }

    @Override
    protected void onResume() {
        loadSongs();
        super.onResume();

    }
}
