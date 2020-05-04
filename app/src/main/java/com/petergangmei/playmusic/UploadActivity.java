package com.petergangmei.playmusic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.petergangmei.playmusic.model.Song;

import java.security.Permission;
import java.util.List;
import java.util.UUID;

public class UploadActivity extends AppCompatActivity {
    private boolean checkPermission = false;
    private Button btnUpload,selectAudio;
    EditText editedSinger, editedTitle, singerId;

    Uri uri;
    String songName, editedSongNameV, songUrl, singerNameV;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        editedTitle = findViewById(R.id.title);
        editedSinger = findViewById(R.id.singerName);
        singerId = findViewById(R.id.singerId);



        btnUpload =findViewById(R.id.btnUpload);
        selectAudio =findViewById(R.id.selectAudio);




        BtnClick();

    }

    private void BtnClick() {
        selectAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validatePermision()){
                    pickSong();
                }else {
                    Toast.makeText(UploadActivity.this, "Permission not granted", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //btn upload
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadSong();
            }
        });
    }

    private void uploadSong() {
        checkFields();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child("RongmeiSongs").child(uri.getLastPathSegment()+System.currentTimeMillis());
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.show();
        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri urlSong = uriTask.getResult();
                songUrl = urlSong.toString();
                UploadDetailToDB();
                Toast.makeText(UploadActivity.this, "success", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadActivity.this, "error:"+ e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
             double progress = (100.00*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
            int curretnProgress = (int)progress;
            progressDialog.setMessage("Uploaded: "+curretnProgress + "%");
            }
        });
    }

    private void UploadDetailToDB() {


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference refSongs = db.collection("Songs");
        String id = UUID.randomUUID().toString();
        Song song = new Song(id, editedSongNameV, singerNameV,singerId.getText().toString(), songUrl,0,0,0,System.currentTimeMillis() );
        refSongs.document(id).set(song).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressDialog.dismiss();
                Toast.makeText(UploadActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadActivity.this, e.getMessage().toLowerCase(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==1){
            if (resultCode ==RESULT_OK){
                uri = data.getData();
                Cursor mcursor = getApplicationContext().getContentResolver()
                        .query(uri, null, null, null, null);
                int indexedname = mcursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                mcursor.moveToFirst();
                songName = mcursor.getString(indexedname);
                mcursor.close();
                editedTitle.setText(songName);
                editedSinger.setText("Unknown");
                singerId.setText("null");

                checkFields();


            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void checkFields() {
        editedSongNameV = editedTitle.getText().toString();
        singerNameV = editedSinger.getText().toString();
    }

    private void pickSong() {
        Intent intent = new Intent();
        intent.setType("audio/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);

    }


    private boolean validatePermision() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) { checkPermission= true;/*  */}
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {checkPermission=false;/* ... */}
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();}
                }).check();

        return checkPermission;
    }


}
