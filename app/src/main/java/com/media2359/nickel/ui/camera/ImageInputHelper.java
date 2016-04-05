package com.media2359.nickel.ui.camera;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.media2359.nickel.ui.CaptureActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Calendar;

/**
 * Class that helps, for Android App, selecting image from gallery, getting image from camera and
 * cropping image.
 * <p>
 * <p>
 * IMPORTANT: The Activity, that contains this object or that contains the fragment that uses this
 * object, must handle the orientation change during taking photo. Either lock the orientation of
 * the Activity or handle orientation changes. Otherwise taking photo feature will not work since
 * the new instance of this object will be created when device rotates.
 */
public class ImageInputHelper {

    public static final int REQUEST_PICTURE_FROM_GALLERY_FRONT = 231;
    public static final int REQUEST_PICTURE_FROM_GALLERY_BACK = 232;
    public static final int REQUEST_PICTURE_FROM_CAMERA_FRONT = 241;
    public static final int REQUEST_PICTURE_FROM_CAMERA_BACK = 242;
//    public static final int REQUEST_CROP_PICTURE = 25;
    private static final String TAG = "ImageInputHelper";

    public static final String DATA_PHOTO_FILE = "Extra_photo_camera";

    private File tempFileFromSource = null;
    private Uri tempUriFromSource = null;

    private File tempFileFromCrop = null;
    private Uri tempUriFromCrop = null;

    /**
     * Activity object that will be used while calling startActivityForResult(). Activity then will
     * receive the callbacks to its own onActivityResult() and is responsible of calling the
     * onActivityResult() of the ImageInputHelper for handling result and being notified.
     */
    private Activity activity;

    /**
     * Fragment object that will be used while calling startActivityForResult(). Fragment then will
     * receive the callbacks to its own onActivityResult() and is responsible of calling the
     * onActivityResult() of the ImageInputHelper for handling result and being notified.
     */
    private Fragment fragment;

    /**
     * Listener instance for callbacks on user events. It must be set to be able to use
     * the ImageInputHelper object.
     */
    private ImageActionListener imageActionListener;

    public ImageInputHelper(Activity activity) {
        this.activity = activity;
    }

    public ImageInputHelper(Fragment fragment) {
        this.fragment = fragment;
        this.activity = fragment.getActivity();
    }

    public void setImageActionListener(ImageActionListener imageActionListener) {
        this.imageActionListener = imageActionListener;
    }

    /**
     * Handles the result of events that the Activity or Fragment receives on its own
     * onActivityResult(). This method must be called inside the onActivityResult()
     * of the container Activity or Fragment.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if ((requestCode == REQUEST_PICTURE_FROM_GALLERY_FRONT) && (resultCode == Activity.RESULT_OK)) {

            Log.d(TAG, "Image selected from gallery");
            copyImageToApplicationFolder(data.getData());
            imageActionListener.onImageSelectedFromGallery(Uri.fromFile(tempFileFromSource), tempFileFromSource, true);

        } else if ((requestCode == REQUEST_PICTURE_FROM_GALLERY_BACK) && (resultCode == Activity.RESULT_OK)){
            Log.d(TAG, "Image selected from gallery");
            copyImageToApplicationFolder(data.getData());
            imageActionListener.onImageSelectedFromGallery(Uri.fromFile(tempFileFromSource), tempFileFromSource, false);
        } else if((requestCode == REQUEST_PICTURE_FROM_CAMERA_FRONT) && (resultCode == Activity.RESULT_OK)) {

            Log.d(TAG, "Image selected from camera");
            String filePath = data.getStringExtra(DATA_PHOTO_FILE);
            File result = new File(filePath);
            tempFileFromSource = result;
            tempUriFromSource = Uri.fromFile(result);
            imageActionListener.onImageTakenFromCamera(tempUriFromSource, tempFileFromSource, true);

        } else if((requestCode == REQUEST_PICTURE_FROM_CAMERA_BACK) && (resultCode == Activity.RESULT_OK)) {

            Log.d(TAG, "Image selected from camera");
            String filePath = data.getStringExtra(DATA_PHOTO_FILE);
            File result = new File(filePath);
            tempFileFromSource = result;
            tempUriFromSource = Uri.fromFile(result);
            imageActionListener.onImageTakenFromCamera(tempUriFromSource, tempFileFromSource, false);
        }

//        } else if ((requestCode == REQUEST_CROP_PICTURE) && (resultCode == Activity.RESULT_OK)) {
//
//            Log.d(TAG, "Image returned from crop");
//            imageActionListener.onImageCropped(tempUriFromCrop, tempFileFromCrop);
//        }
    }

    private void copyImageToApplicationFolder(Uri data) {

        String root = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
        File createDir = new File(root + "Nickel Profile" + File.separator);
        if (!createDir.exists()) {
            createDir.mkdir();
        }

        FileChannel source = null;
        FileChannel destination = null;
        File sourceFile = new File(getRealPathFromURI(data));
        Log.d(TAG, "sourceFile path is" + data.getPath());
        Calendar today = Calendar.getInstance();
        File destFile = new File(root + today.get(Calendar.DATE) + today.get(Calendar.SECOND) + "_nickel.png");
        try {
            destFile.createNewFile();

            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            if (destination != null && source != null) {
                destination.transferFrom(source, 0, source.size());
            }
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }

            tempFileFromSource = destFile;

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getRealPathFromURI(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = activity.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    /**
     * Starts an intent for selecting image from gallery. The result is returned to the
     * onImageSelectedFromGallery() method of the ImageSelectionListener interface.
     */
    public void selectImageFromGallery(boolean isFront) {
        checkListener();

        if (tempFileFromSource == null) {
            try {
                tempFileFromSource = File.createTempFile("choose", "png", activity.getExternalCacheDir());
                tempUriFromSource = Uri.fromFile(tempFileFromSource);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUriFromSource);

//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setType("image/*");

        int requestCode;

        if (isFront)
            requestCode = REQUEST_PICTURE_FROM_GALLERY_FRONT;
        else
            requestCode = REQUEST_PICTURE_FROM_GALLERY_BACK;

        if (fragment == null) {
            activity.startActivityForResult(intent, requestCode);
        } else {
            fragment.startActivityForResult(intent, requestCode);
        }
    }

    /**
     * Starts an intent for taking photo with camera. The result is returned to the
     * onImageTakenFromCamera() method of the ImageSelectionListener interface.
     */
    public void takePhotoWithCamera(boolean isFront) {
        checkListener();

        if (tempFileFromSource == null) {
            try {
                tempFileFromSource = File.createTempFile("choose", "png", activity.getExternalCacheDir());
                tempUriFromSource = Uri.fromFile(tempFileFromSource);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

//        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUriFromSource);

        int requestCode;

        if (isFront)
            requestCode = REQUEST_PICTURE_FROM_CAMERA_FRONT;
        else
            requestCode = REQUEST_PICTURE_FROM_CAMERA_BACK;

        if (fragment == null) {
//            Intent intent = new Intent(activity, CaptureActivity.class);
//            activity.startActivityForResult(intent, requestCode);
            CaptureActivity.startCapturingIDCard(activity,requestCode);
        } else {
//            Intent intent = new Intent(fragment.getActivity(),CaptureActivity.class);
//            fragment.startActivityForResult(intent, requestCode);
            CaptureActivity.startCapturingIDCard(fragment,requestCode);
        }
    }

//    /**
//     * Starts an intent for cropping an image that is saved in the uri. The result is
//     * returned to the onImageCropped() method of the ImageSelectionListener interface.
//     *
//     * @param uri     uri that contains the data of the image to crop
//     * @param outputX width of the result image
//     * @param outputY height of the result image
//     * @param aspectX horizontal ratio value while cutting the image
//     * @param aspectY vertical ratio value of while cutting the image
//     */
//    public void requestCropImage(Uri uri, int outputX, int outputY, int aspectX, int aspectY) {
//        checkListener();
//
//        if (tempFileFromCrop == null) {
//            try {
//                tempFileFromCrop = File.createTempFile("crop", "png", activity.getExternalCacheDir());
//                tempUriFromCrop = Uri.fromFile(tempFileFromCrop);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        // open crop intent when user selects image
//        final Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType(uri, "image/*");
//        intent.putExtra("output", tempUriFromCrop);
//        intent.putExtra("outputX", outputX);
//        intent.putExtra("outputY", outputY);
//        intent.putExtra("aspectX", aspectX);
//        intent.putExtra("aspectY", aspectY);
//        intent.putExtra("scale", true);
//        intent.putExtra("noFaceDetection", true);
////        intent.putExtra("crop", true);
////        intent.putExtra("return-data", true);
//
//        if (fragment == null) {
//            activity.startActivityForResult(intent, REQUEST_CROP_PICTURE);
//        } else {
//            fragment.startActivityForResult(intent, REQUEST_CROP_PICTURE);
//        }
//    }

    private void checkListener() {
        if (imageActionListener == null) {
            throw new RuntimeException("ImageSelectionListener must be set before calling openGalleryIntent(), openCameraIntent() or requestCropImage().");
        }
    }

    /**
     * Listener interface for receiving callbacks from the ImageInputHelper.
     */
    public interface ImageActionListener {
        void onImageSelectedFromGallery(Uri uri, File imageFile, boolean isFront);

        void onImageTakenFromCamera(Uri uri, File imageFile, boolean isFront);

        //void onImageCropped(Uri uri, File imageFile);
    }
}
