package com.petergangmei.playmusic.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.petergangmei.playmusic.R;
import com.petergangmei.playmusic.adapter.SongsAdapter;
import com.petergangmei.playmusic.model.Song;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    JcPlayerView jcPlayerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_home, container, false);
        final ArrayList<JcAudio> jcAudios = new ArrayList<>();
        final ArrayList<String> arrayListSongsName = new ArrayList<>();
        final ArrayList<String> arrayListSongsUrl = new ArrayList<>();
        final ArrayAdapter<String> arrayAdapter;


        final JcPlayerView jcplayerView = (JcPlayerView) root.findViewById(R.id.jcplayerView);

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

                final SongsAdapter[] songsAdapter = new SongsAdapter[1];
                final List<Song> songList;
                final RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
                songList = new ArrayList<>();
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference refSongs = db.collection("Songs");
                refSongs.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        jcAudios.clear();
                        songList.clear();
                     for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                         Song song = documentSnapshot.toObject(Song.class);
                         songList.add(song);
                         jcAudios.add(JcAudio.createFromURL(song.getSingerName(),song.getSongUrl()));
                     }
//                     songsAdapter = new SongsAdapter(getContext(), songList);
//                     recyclerView.setAdapter(songsAdapter);
                     jcplayerView.initAnonPlaylist(jcAudios);
                        jcplayerView.createNotification();

                    }
                });


            }
        });
        return root;
    }
}
