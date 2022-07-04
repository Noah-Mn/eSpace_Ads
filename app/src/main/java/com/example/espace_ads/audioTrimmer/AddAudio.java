package com.example.espace_ads.audioTrimmer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.espace_ads.R;

public class AddAudio extends AppCompatActivity implements View.OnClickListener {
    private static final int ADD_AUDIO = 1001;
    private Button btnAudioTrim;
    private static final int REQUEST_ID_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_audio);

        btnAudioTrim = (Button) findViewById(R.id.btnAudioTrim);
        btnAudioTrim.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == btnAudioTrim) {
            //check storage permission before start trimming
            if (checkStoragePermission()) {
                startActivityForResult(new Intent(getApplicationContext(), AudioTrimmer.class), ADD_AUDIO);
                overridePendingTransition(0, 0);

            } else {
                requestStoragePermission();
            }
        }
    }

    Intent intent = new Intent(getApplicationContext(), AudioTrimmer.class);

//    ActivityResultLauncher<Intent> startActivityForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
//        @Override
//        public void onActivityResult(ActivityResult result) {
//            int requestCode = 0, resultCode;
//            Intent data;
//          if (requestCode == ADD_AUDIO){
//
//            }
//        }
//    }{
//
//
//    })


    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(AddAudio.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO},
                REQUEST_ID_PERMISSIONS);
    }

    private boolean checkStoragePermission() {
        return (ActivityCompat.checkSelfPermission(AddAudio.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(AddAudio.this,
                        Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_ID_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(AddAudio.this, "Permission granted, Click again", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_AUDIO) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    //audio trim result will be saved at below path
                    String path = data.getExtras().getString("INTENT_AUDIO_FILE");
                    Toast.makeText(AddAudio.this, "Audio stored at " + path, Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}