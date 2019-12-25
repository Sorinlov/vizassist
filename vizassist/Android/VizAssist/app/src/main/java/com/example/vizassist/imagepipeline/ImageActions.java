package com.example.vizassist.imagepipeline;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Actions to get images from either the device back camera or photo gallery
 */
public class ImageActions {

    /**
     * Start the built-in back camera to capture a still image.
     * @param activity origin activity in which the intent will be from.
     * @param requestCode request code to get result when the camera activity is dismissed.
     */
    public static Uri startCameraActivity(Activity activity, int requestCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile(activity);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(activity,
                        activity.getPackageName() + ".fileprovider",
                        photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                activity.startActivityForResult(intent, requestCode);
                return photoURI;
            }
        }
        return null;
    }

    /**
     * Start photo gallery image picker to select a saved image.
     * @param activity origin activity in which the intent will be from.
     * @param requestCode request code to get result when the gallery activity is dismissed.
     */
    public static void startGalleryActivity(Activity activity, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        activity.startActivityForResult(intent, requestCode);
    }

    private static File createImageFile(Activity activity) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        return image;
    }
}
