package edu.csulb.android.photonotes;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.io.File;
import java.util.Date;
import android.os.Environment;


import java.util.Random;

public class AddPhotoActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String mCurrentPhotoPath;
    PhotoData photoData;
    static EditText photoCaption;
    //static final int REQUEST_TAKE_PHOTO = 1;
     public static String DEBUG_TAG = "AddPhotoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);

        //Retrieve Photo Caption Entered by User
        photoCaption = (EditText) findViewById(R.id.caption);

        //Launch Camera with Click Photo button
        Button clickPhoto = (Button) findViewById(R.id.clickPictureButton);
        clickPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

        Button saveData = (Button) findViewById(R.id.saveButton);
        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoData = new PhotoData(getApplicationContext());
                photoData.insert(photoCaption.getText().toString(),mCurrentPhotoPath);
                finish();
            }
        });

    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try{
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.d(DEBUG_TAG, "Error occured while creating the file for photo with value of: " + ex);
            }
            if(photoFile != null){
                Uri photoURI = FileProvider.getUriForFile(this,"edu.csulb.android.fileprovider", photoFile);
                Log.d("PhotoUri","photo uri: " + photoURI);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            }
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    //Saving Full Size Photo
    //This method returns a unique file name for a new photo using a date-time stamp

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.d("CreateImageFile:", "Image path " + mCurrentPhotoPath);
        return image;
    }

    @Override
    public void finish() {
        photoData.close();
        super.finish();
    }
}
