package com.dmss.dmssevents;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dmss.dmssevents.common.CircleTransform;
import com.dmss.dmssevents.common.DmsEventsAppController;
import com.dmss.dmssevents.common.DmsEventsBaseActivity;
import com.dmss.dmssevents.common.DmsSharedPreferences;
import com.dmss.dmssevents.common.RoundedCornersTransform;
import com.dmss.dmssevents.common.TouchImageView;
import com.dmss.dmssevents.models.UserProfileModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class ProfileActivity extends DmsEventsBaseActivity {

    TextView userNameTextView, userRoleTextView, textViewEmployeeId, userEmailTextView, userDepartmentTextView, userPhoneTextView;
    UserProfileModel userProfileModel;
    ImageView imageView2, addProfilePicImageView;
    TouchImageView profilePictureOverViewImageView;
    RelativeLayout overViewLayout;
    ImageView overViewClose;

    Uri filePath;
    final private int PICK_IMAGE = 1;
    public static final int MEDIA_TYPE_IMAGE = 5;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;

    final private int REQUEST_CODE_ASK_CAMERA_PERMISSIONS = 123;
    final private int REQUEST_CODE_ASK_EXTERNAL_STORAGE_PERMISSIONS = 321;
    boolean cameraPermissionAvailable = false, galleryPermissionAvailable = false;
    public static File mediaFile;

    Context thisClassContext;

    ProgressDialog progressDialog;
    DmsEventsAppController controller;


    String uploadedImageUrl;
    boolean overViewVisible = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Profile");
        initializeUIElements();
    }

    @Override
    public void initializeUIElements() {
        controller = (DmsEventsAppController) getApplicationContext();
        thisClassContext = ProfileActivity.this;
        progressDialog = new ProgressDialog(ProfileActivity.this);
        progressDialog.setMessage("Loading please wait....");
        progressDialog.setIndeterminate(false);
        progressDialog.setCanceledOnTouchOutside(false);
        userProfileModel = DmsSharedPreferences.getUserDetails(ProfileActivity.this);
        userNameTextView = (TextView) findViewById(R.id.userNameTextView);
        textViewEmployeeId = (TextView) findViewById(R.id.textViewEmployeeId);
        userEmailTextView = (TextView) findViewById(R.id.userEmailTextView);
        userDepartmentTextView = (TextView) findViewById(R.id.userDepartmentTextView);
        userPhoneTextView = (TextView) findViewById(R.id.userPhoneTextView);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        userRoleTextView = (TextView) findViewById(R.id.userRoleTextView);
        addProfilePicImageView = (ImageView) findViewById(R.id.addProfilePicImageView);
        overViewClose = (ImageView) findViewById(R.id.overViewClose);
        profilePictureOverViewImageView = (TouchImageView) findViewById(R.id.profilePictureOverViewImageView);
        profilePictureOverViewImageView.bringToFront();
        overViewLayout = (RelativeLayout) findViewById(R.id.overViewLayout);
        overViewLayout.setVisibility(View.GONE);
        uploadedImageUrl = DmsSharedPreferences.getProfilePicUrl(ProfileActivity.this);
        if (uploadedImageUrl != null && uploadedImageUrl.length() > 0) {
            Picasso.with(thisClassContext).load(uploadedImageUrl).transform(new CircleTransform()).into(imageView2);
            Picasso.with(thisClassContext).load(uploadedImageUrl).into(profilePictureOverViewImageView);
        }
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overViewLayout.setVisibility(View.VISIBLE);
                overViewLayout.bringToFront();
                profilePictureOverViewImageView.bringToFront();
                overViewVisible = true;
            }
        });
        overViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overViewLayout.setVisibility(View.GONE);
                overViewVisible = false;
            }
        });
        addProfilePicImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissions();
            }
        });
        if (userProfileModel != null) {
            userNameTextView.setText(userProfileModel.getDisplayName());
            textViewEmployeeId.setText(userProfileModel.getEmpID());
            userEmailTextView.setText(userProfileModel.getEmailID());
            userDepartmentTextView.setText(userProfileModel.getDepartment());
            if (userProfileModel.getMobile().length() > 0) {
                userPhoneTextView.setText(userProfileModel.getMobile());
            } else {
                userPhoneTextView.setText("Not Available");
            }
            userRoleTextView.setText(userProfileModel.getJobTittle().trim());
            /*if((userProfileModel.getProfilePhoto()!=null)) {
                String displayName=userProfileModel.getDisplayName();
                displayName = displayName.replace(" ", ".");
                String profileUrl="http://applicationserver4/sis/StaffPhotos//"+displayName+".jpg";
                Picasso.with(this).load(profileUrl).transform(new CircleTransform()).into(imageView2);
            }else{
                imageView2.setImageResource(R.drawable.profilepic);
            }*/
            //Picasso.with(this).load("http://applicationserver4/sis/StaffPhotos//Sandeep.Kumar.jpg").placeholder(R.drawable.loading).error(R.drawable.appicon).transform(new CircleTransform()).into(imageView2);
            /*Glide.with(this)
                    .load("http://applicationserver4/sis/StaffPhotos/Sandeep.Kumar.jpg")
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.loading)
                    .into(imageView2);*/
            //Picasso.with(this).load("http://www.dmss.co.in/Images/homepage.jpg").transform(new CircleTransform()).into(imageView2);
        } else {
            userNameTextView.setText("User");
            textViewEmployeeId.setText("Employee Id");
            userEmailTextView.setText("Email Id");
            userDepartmentTextView.setText("Department");
            userPhoneTextView.setText("Phone Number");
            userRoleTextView.setText("Role");
        }
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void actionBarSettings() {

    }

    /**
     * Toolbar widgets on-click actions.
     *
     * @param item A variable of type MenuItem.
     **/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //finishing activity up on click of back arrow button
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                String imgPick = filePath.getPath();
                //filePath = Uri.parse(compressImage(filePath.getPath()));

                upLoadImage();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(thisClassContext, "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(thisClassContext, "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }

        } else if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && null != data) {
            // Get the Image from data
            filePath = /*Uri.parse(compressImage(*/data.getData()/*.toString()))*/;

            upLoadImage();

            /*String[] filePathColumn = {MediaStore.Images.Media.DATA};

            // Get the cursor
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            // Move to first row
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String imgPick = cursor.getString(columnIndex);
            cursor.close();
            */
            //Toast.makeText(thisClassContext,filePath.getPath(),Toast.LENGTH_SHORT).show();
        }
    }

    public void upLoadImage() {
        if (filePath != null) {
            progressDialog.show();
            java.util.Date date = new java.util.Date();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date.getTime());
            String userName = DmsSharedPreferences.getUserDetails(thisClassContext).getFirstName();
            UserProfileModel userProfileModel = DmsSharedPreferences.getUserDetails(ProfileActivity.this);
            final String imageName = "Profile_pics/" + userProfileModel.getEmpID() + "_" + userProfileModel.getDisplayName() + ".jpg";
            StorageReference childRef = controller.getStorageRef().child(imageName);
            //uploading the image
            //compressImage(filePath.getPath());
            //UploadTask uploadTask = childRef.putFile(filePath);
            String newPath = compressImage(filePath.toString());
            Uri uri = getImageContentUri(thisClassContext, new File(newPath));
            UploadTask uploadTask = childRef.putFile(uri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    uploadedImageUrl = taskSnapshot.getDownloadUrl().toString();
                    if (uploadedImageUrl != null && uploadedImageUrl.length() > 0) {
                        Picasso.with(thisClassContext).load(uploadedImageUrl).transform(new CircleTransform()).into(imageView2);
                        Picasso.with(thisClassContext).load(uploadedImageUrl).into(profilePictureOverViewImageView);
                    }
                    DmsSharedPreferences.saveProfilePicUrl(ProfileActivity.this, uploadedImageUrl);
                    //getUrlOfUploadedImage(imageName);
                    progressDialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(thisClassContext, "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(thisClassContext, "Select an image", Toast.LENGTH_SHORT).show();
        }
    }

    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            cursor.close();
            return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    /**
     * Checks that the application is having the camera and gallery permissions or not.
     * if not asks for the permission.
     **/
    public void checkPermissions() {
        int hasPermission = ActivityCompat.checkSelfPermission(thisClassContext, android.Manifest.permission.CAMERA);
        int hasWritePermission = ActivityCompat.checkSelfPermission(thisClassContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int hasReadPermission = ActivityCompat.checkSelfPermission(thisClassContext, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (hasPermission != PackageManager.PERMISSION_GRANTED && hasWritePermission != PackageManager.PERMISSION_GRANTED && hasReadPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_CAMERA_PERMISSIONS);
        } else {
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{android.Manifest.permission.CAMERA}, REQUEST_CODE_ASK_CAMERA_PERMISSIONS);
            } else {
                cameraPermissionAvailable = true;
            }

            if (hasWritePermission != PackageManager.PERMISSION_GRANTED || hasReadPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_EXTERNAL_STORAGE_PERMISSIONS);
            } else {
                galleryPermissionAvailable = true;
            }
        }
        captureImage();
    }

    public void captureImage() {
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(thisClassContext);
        alert.setMessage("Please Select");
        alert.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                if (galleryPermissionAvailable) {
                    callGallery();

                } else {
                    Toast.makeText(thisClassContext,
                            "gallery permission denied", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        alert.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Checking device has camera hardware or not
                if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                    if (cameraPermissionAvailable) {
                        callCamera();
                    } else {
                        Toast.makeText(thisClassContext,
                                "camera permission denied", Toast.LENGTH_LONG)
                                .show();
                    }
                } else {
                    Toast.makeText(thisClassContext,
                            "Sorry! Your device doesn't support camera", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });
        alert.show();
    }

    public void callCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        filePath = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, filePath);
        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    public void callGallery() {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, PICK_IMAGE);
    }

    private Uri getOutputMediaFileUri(int type) {
        if (Build.VERSION.SDK_INT > 23) {
            Uri photoURI = FileProvider.getUriForFile(thisClassContext, ProfileActivity.this.getApplicationContext().getPackageName() + ".provider", getOutputMediaFile(type));
            return photoURI;
        } else {
            return Uri.fromFile(getOutputMediaFile(type));
        }
    }

    private static File getOutputMediaFile(int type) {
        // Check that the SDCard is mounted
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "DmssImages");

        // Create the storage directory(MyCameraVideo) if it does not exist
        if (!mediaStorageDir.exists()) {

            if (!mediaStorageDir.mkdirs()) {

                Log.d("DmssImages", "Failed to create directory BurbankImages.");
                return null;
            }
        }

        java.util.Date date = new java.util.Date();
        String timeStamp = new SimpleDateFormat("dd:MM:yyyy_HH:mm:ss").format(date.getTime());


        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);
        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;
//      max Height and width values of the compressed image is taken as 816x612
        float maxHeight = 600.0f;
        float maxWidth = 900.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            if (index != -1) {
                return cursor.getString(index);
            } else {
                return mediaFile.getAbsolutePath();
            }
        }
    }

    public String getFilename() {
        // Check that the SDCard is mounted
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "DmssImages");

        // Create the storage directory(MyCameraVideo) if it does not exist
        if (!mediaStorageDir.exists()) {

            if (!mediaStorageDir.mkdirs()) {

                Log.d("DmssImages", "Failed to create directory BurbankImages.");
                return null;
            }
        }

        java.util.Date date = new java.util.Date();
        String timeStamp = new SimpleDateFormat("dd:MM:yyyy_HH:mm:ss").format(date.getTime());

        /*File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        //return mediaFile;


        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }*/
        String uriSting = (mediaStorageDir.getAbsolutePath() + File.separator + "IMG_" + timeStamp + ".jpg");
        return uriSting;

    }

    @Override
    public void onBackPressed() {
        if (overViewVisible) {
            overViewLayout.setVisibility(View.GONE);
            overViewVisible = false;
        } else {
            finish();
        }
    }
}
