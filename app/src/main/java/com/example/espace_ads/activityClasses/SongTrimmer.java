package com.example.espace_ads.activityClasses;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.espace_ads.R;
import com.example.espace_ads.databinding.ActivitySongTrimmerBinding;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class SongTrimmer extends AppCompatActivity {
    Uri uri;
    ActivitySongTrimmerBinding binding;
    private static int oTime = 0, sTime = 0, eTime = 0, fTime = 5000, bTime = 5000;
    private final Handler handler = new Handler();
    VideoView videoView;


    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySongTrimmerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.sBar.setClickable(true);
        binding.btnPause.setEnabled(false);
        mediaPlayer = new MediaPlayer();
        videoView = findViewById(R.id.imgLogo);


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

            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    videoView.start();
                    binding.sBar.setRangeValues(0, mediaPlayer.getDuration());
                    binding.sBar.setSelectedMinValue(0);
                    binding.sBar.setSelectedMaxValue(mediaPlayer.getDuration());
                    binding.sBar.setEnabled(true);

                    binding.sBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
                        @Override
                        public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                            videoView.seekTo((int) minValue*1000);
                        }
                    });
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (videoView.getCurrentPosition() >= binding.sBar.getSelectedMaxValue().intValue()*1000){
                                videoView.seekTo(binding.sBar.getSelectedMinValue().intValue()*1000);
                            }
                        }
                    }, 1000);
                }
            });

            binding.btnPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(SongTrimmer.this, "Playing Audio", Toast.LENGTH_SHORT).show();
                    mediaPlayer.start();
                    eTime = mediaPlayer.getDuration();
                    sTime = mediaPlayer.getCurrentPosition();
                    if (oTime == 0) {
//                        binding.sBar.setMax(eTime);
                        oTime = 1;
                    }
//                    binding.txtSongTime.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(eTime),
//                            TimeUnit.MILLISECONDS.toSeconds(eTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(eTime))));
                    binding.txtStartTime.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(sTime),
                            TimeUnit.MILLISECONDS.toSeconds(sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(sTime))));
//                    binding.sBar.setProgress(sTime);
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
//            binding.sBar.setProgress(sTime);
            handler.postDelayed(this, 100);
        }
    };

}