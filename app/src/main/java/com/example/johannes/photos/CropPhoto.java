package com.example.johannes.photos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.isseiaoki.simplecropview.CropImageView;

public class CropPhoto extends AppCompatActivity {
    public int change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_photo);

        Intent intent = getIntent();
        Bitmap photo = (Bitmap) intent.getParcelableExtra("photo");
        change = intent.getIntExtra("change", 0);
        // Set image for cropping

        final CropImageView cropImageView = (CropImageView) findViewById(R.id.cropImageView);
        if (change == 0) {
            cropImageView.setCropMode(CropImageView.CropMode.CIRCLE);
        } else {
            cropImageView.setCropMode(CropImageView.CropMode.RATIO_16_9);
        }


        cropImageView.setImageBitmap(photo);

        Button cropButton = (Button) findViewById(R.id.crop_button);
        cropButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent in = new Intent(getApplicationContext(), MainActivity.class);
                // Get cropped image, and show result.
                if (change == 0) {
                    Bitmap newPhoto = cropImageView.getCroppedBitmap();
                    in.putExtra("newPhoto", newPhoto);
                } else {
                    Bitmap newBackground = cropImageView.getCroppedBitmap();
                    in.putExtra("newBackground", newBackground);
                }
                startActivity(in);
            }
        });

    }

}
