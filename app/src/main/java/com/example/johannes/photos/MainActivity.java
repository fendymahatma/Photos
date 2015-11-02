package com.example.johannes.photos;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 0;
    private final static int RESULT_SELECT_IMAGE = 100;
    public String imgDecodableString;
    public ImageView imgProfile;
    public ImageView backgroundProfile;
    public Bitmap photo;
    public Bitmap newPhoto;
    public Bitmap newBackground;
    public Button btnCapture;
    public int change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgProfile = (ImageView) findViewById(R.id.imageProfile);
        backgroundProfile = (ImageView) findViewById(R.id.backgroundProfile);

        if (!hasCamera())
            btnCapture.setEnabled(false);

        Intent intent = getIntent();
        newPhoto = intent.getParcelableExtra("newPhoto");
        newBackground = intent.getParcelableExtra("newBackground");

        if (newPhoto == null) {
            backgroundProfile.setImageBitmap(newBackground);
        } else {
            imgProfile.setImageBitmap(newPhoto);
        }
    }

    //cek if user has cammera
    private boolean hasCamera() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    //launch the camera
    public void changeProfile(View view) {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //take picture and pass along to the activity
        startActivityForResult(i, REQUEST_IMAGE_CAPTURE);
    }

    //launch the camera
    public void changeBackground(View view) {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //take picture and pass along to the activity
        startActivityForResult(i, RESULT_SELECT_IMAGE);
    }

    //return image

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {
                    //get the photo
                    Bundle extras = data.getExtras();
                    photo = (Bitmap) extras.get("data");

                    finish();
                    Intent in = new Intent(this, CropPhoto.class);
                    change = 0;
                    in.putExtra("change", change);
                    in.putExtra("photo", photo);
                    startActivity(in);
                }
                break;
            case RESULT_SELECT_IMAGE:
                if (resultCode == RESULT_OK && null != data) {
                    // Get the Image from data

                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };

                    // Get the cursor
                    Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imgDecodableString = cursor.getString(columnIndex);
                    cursor.close();

                    photo = BitmapFactory.decodeFile(imgDecodableString);
                    change = 1;
                    finish();
                    Intent in = new Intent(this, CropPhoto.class);
                    in.putExtra("change", change);
                    in.putExtra("photo", photo);
                    startActivity(in);
                }
                break;
        }
    }
}
