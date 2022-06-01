package com.thukuma.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.thukuma.youtube_playlist.GetPlaylist;
import com.thukuma.youtube_playlist.Models.PlayListModels;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AndroidNetworking.initialize(this);
        new GetPlaylist(this,
                "https://www.youtube.com/watch?v=IXp3UXm0O0Y&list=PLmQKoPepuLJ_N5OGkaG01PhLyp24JhpDm&ab_channel=MSquareProgramming",
                new GetPlaylist.onComplete() {
                    @Override
                    public void onComplete(PlayListModels models) {
                        Log.d("my-test", "Title is: "+models.getTitle());
                        Log.d("my-test", "Url is: "+models.getUrl());
                        Log.d("my-test", "Image is: "+models.getImage());
                    }
                },
                new GetPlaylist.onError() {
                    @Override
                    public void onError(String onError) {
                        Log.d("my-test", "onError: "+onError.toString());
                    }
                }
        );


    }

}