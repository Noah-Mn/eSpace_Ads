package com.example.espace_ads.activityClasses;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.espace_ads.databinding.ActivitySongTrimmerBinding;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class SongTrimmer extends AppCompatActivity {
    Uri uri;
    ActivitySongTrimmerBinding binding;
    private static int oTime = 0, sTime = 0, eTime = 0, fTime = 5000, bTime = 5000;
    private final Handler handler = new Handler();

    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySongTrimmerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.sBar.setClickable(false);
        binding.btnPause.setEnabled(false);
        mediaPlayer = new MediaPlayer();


        try {
            listeners();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listeners() throws IOException {
        Intent intent = getIntent();
        if (intent != null) {
            String audioUri = intent.getStringExtra("audioUri");
            uri = Uri.parse(audioUri);
            mediaPlayer.setDataSource(getApplicationContext(), uri);
            mediaPlayer.prepare();

            binding.btnPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(SongTrimmer.this, "Playing Audio", Toast.LENGTH_SHORT).show();
                    mediaPlayer.start();
                    eTime = mediaPlayer.getDuration();
                    sTime = mediaPlayer.getCurrentPosition();
                    if (oTime == 0) {
                        binding.sBar.setMax(eTime);
                        oTime = 1;
                    }
//                    binding.txtSongTime.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(eTime),
//                            TimeUnit.MILLISECONDS.toSeconds(eTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(eTime))));
                    binding.txtStartTime.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(sTime),
                            TimeUnit.MILLISECONDS.toSeconds(sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(sTime))));
                    binding.sBar.setProgress(sTime);
                    handler.postDelayed(UpdateSongTime, 100);
                    binding.btnPause.setEnabled(true);
                    binding.btnPlay.setEnabled(false);
                }
            });
            binding.btnPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mediaPlayer.pause();
                    binding.btnPause.setEnabled(false);
                    binding.btnPlay.setEnabled(true);
                    Toast.makeText(getApplicationContext(), "Pausing Audio", Toast.LENGTH_SHORT).show();
                }
            });
            binding.btnForward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((sTime + fTime) <= eTime) {
                        sTime = sTime + fTime;
                        mediaPlayer.seekTo(sTime);
                    } else {
                        Toast.makeText(getApplicationContext(), "Cannot jump forward 5 seconds", Toast.LENGTH_SHORT).show();
                    }
                    if (!binding.btnPlay.isEnabled()) {
                        binding.btnPlay.setEnabled(true);
                    }
                }
            });
            binding.btnBackward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((sTime - bTime) > 0) {
                        sTime = sTime - bTime;
                        mediaPlayer.seekTo(sTime);
                    } else {
                        Toast.makeText(getApplicationContext(), "Cannot jump backward 5 seconds", Toast.LENGTH_SHORT).show();
                    }
                    if (!binding.btnPlay.isEnabled()) {
                        binding.btnPlay.setEnabled(true);
                    }
                }
            });
        }
    }

    private final Runnable UpdateSongTime = new Runnable() {
        @Override
        public void run() {
            sTime = mediaPlayer.getCurrentPosition();
            binding.txtStartTime.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(sTime),
                    TimeUnit.MILLISECONDS.toSeconds(sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(sTime))));
            binding.sBar.setProgress(sTime);
            handler.postDelayed(this, 100);
        }
    };

}