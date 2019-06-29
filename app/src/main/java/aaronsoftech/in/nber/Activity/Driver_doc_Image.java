package aaronsoftech.in.nber.Activity;

import android.Manifest;

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
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import aaronsoftech.in.nber.R;
import static aaronsoftech.in.nber.Activity.Driver_document.path_aadhar;
import static aaronsoftech.in.nber.Activity.Driver_document.path_insurense;
import static aaronsoftech.in.nber.Activity.Driver_document.path_licence;
import static aaronsoftech.in.nber.Activity.Driver_document.path_pancard;
import static aaronsoftech.in.nber.Activity.Driver_document.path_permit_a;
import static aaronsoftech.in.nber.Activity.Driver_document.path_permit_b;
import static aaronsoftech.in.nber.Activity.Driver_document.path_police_verification_file;
import static aaronsoftech.in.nber.Activity.Driver_document.path_registration;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class Driver_doc_Image extends AppCompatActivity {
    int REQUEST_CODE_LOCATIONlC=876;
    String[] locationPermissions = {"android.permission.READ_EXTERNAL_STORAGE","android.permission.WRITE_EXTERNAL_STORAGE","android.permission.CAMERA"};
    String TAG="Driver_doc_Image";
    ImageView btn_photo;
    String activity_type;


    public static final int MEDIA_TYPE_IMAGE = 1;
    File photofile = null;
    String mCurrentPhotoPath;
    public static final String IMAGE_DIRECTORY_NAME = String.valueOf((R.string.app_name));
    Uri imageUri;
    ImageView btn_share;
    private static int RESULT_LOAD_IMAGE = 1;
    private static final int REQUEST_CODE_CAMERA_ID = 106;
    String picturePath_id ="";
    Bitmap bitmapProfileImage3 = null;
    public static String lastCompressedImageFileName="";
    Uri selectedImageUri;
    File selectedImageFile3 = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_doc__image);
        btn_photo=findViewById(R.id.image);
        TextView btn_image=findViewById(R.id.camera_id);

        btn_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectimage();
            }
        });
        btn_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectimage();
            }
        });
        activity_type=getIntent().getExtras().getString("type","");
        TextView ed_message=findViewById(R.id.txt_message);
        TextView ed_title=findViewById(R.id.txt_title);

        if (activity_type.equalsIgnoreCase("1"))
        {
            ed_message.setText(getResources().getString(R.string.txt_message_license));
            ed_title.setText(getResources().getString(R.string.txt_title_license));
        }else if (activity_type.equalsIgnoreCase("3"))
        {
            ed_message.setText(getResources().getString(R.string.txt_message_pancard));
            ed_title.setText(getResources().getString(R.string.txt_title_pancard));
        }else if (activity_type.equalsIgnoreCase("4"))
        {
            ed_message.setText(getResources().getString(R.string.txt_message_permit_a));
            ed_title.setText(getResources().getString(R.string.txt_title_permit_a));
        }else if (activity_type.equalsIgnoreCase("5"))
        {
            ed_message.setText(getResources().getString(R.string.txt_message_register_cert));
            ed_title.setText(getResources().getString(R.string.txt_title_register_cert));
        }else if (activity_type.equalsIgnoreCase("6"))
        {
            ed_message.setText(getResources().getString(R.string.txt_message_permit_b));
            ed_title.setText(getResources().getString(R.string.txt_title_permit_b));
        }else if (activity_type.equalsIgnoreCase("7"))
        {
            ed_message.setText(getResources().getString(R.string.txt_message_insurense));
            ed_title.setText(getResources().getString(R.string.txt_title_insurense));
        }else if (activity_type.equalsIgnoreCase("8"))
        {
            ed_message.setText(getResources().getString(R.string.txt_message_verify));
            ed_title.setText(getResources().getString(R.string.txt_title_verify));
        }else if (activity_type.equalsIgnoreCase("9"))
        {
            ed_message.setText(getResources().getString(R.string.txt_message_aadhar));
            ed_title.setText(getResources().getString(R.string.txt_title_aadhar));
        }

        TextView btn_submit=findViewById(R.id.camera_id);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        getAllpermission();
    }


    private void getAllpermission() {
        Handler handler  = new Handler();
        Runnable runnable = new Runnable()
        {
            @Override
            public void run()
            {

                if (ActivityCompat.checkSelfPermission(Driver_doc_Image.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Driver_doc_Image.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED
                        )                    {
                    ActivityCompat.requestPermissions(Driver_doc_Image.this, locationPermissions, REQUEST_CODE_LOCATIONlC);
                    return;
                }
            }
        };
        handler.postDelayed(runnable, 2000);
    }

    private void Set_image_path(String picturePath) {

        if (activity_type.equalsIgnoreCase("1"))
        {
            path_licence=picturePath;
        }else if (activity_type.equalsIgnoreCase("3"))
        {
            path_pancard=picturePath;
        }else if (activity_type.equalsIgnoreCase("4"))
        {
            path_permit_a=picturePath;
        }else if (activity_type.equalsIgnoreCase("5"))
        {
            path_registration=picturePath;
        }else if (activity_type.equalsIgnoreCase("6"))
        {
            path_permit_b=picturePath;
        }else if (activity_type.equalsIgnoreCase("7"))
        {
            path_insurense=picturePath;
        }else if (activity_type.equalsIgnoreCase("8"))
        {
            path_police_verification_file=picturePath;
        }else if (activity_type.equalsIgnoreCase("9"))
        {
            path_aadhar=picturePath;
        }
        finish();

    }

    public static boolean checkAppVersion(){
        Boolean isM=false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            isM=true;
        }
        else
        {
            isM=false;
        }
        return isM;
    }
    private void selectimage(){

        final CharSequence[] items={"Camera","Gallery","Cancel"};
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Select Image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            //        @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int i) {
                //      try{
                if(items[i].equals("Camera"))
                {
                    if(checkAppVersion())
                    {
                        if( ContextCompat.checkSelfPermission(getApplication(), android.Manifest.permission.CAMERA)== PackageManager.PERMISSION_DENIED) {
                            Intent callcameraapplicationintent = new Intent();
                            callcameraapplicationintent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

                            photofile = createImageFile();
                            String authorities = getApplication().getPackageName() + ".fileprovider";
                            imageUri = FileProvider.getUriForFile(getApplication(), authorities, photofile);
                            // imageUri = AppUtills.getOutputMediaFileUri(MEDIA_TYPE_IMAGE, TAG);
                            callcameraapplicationintent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

                            startActivityForResult(callcameraapplicationintent, REQUEST_CODE_CAMERA_ID);
                            // overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            dialog.dismiss();
                        }else {
                            Intent callcameraapplicationintent = new Intent();
                            callcameraapplicationintent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

                            photofile = createImageFile();
                            String authorities = getApplication().getPackageName() + ".fileprovider";
                            imageUri = FileProvider.getUriForFile(getApplication(), authorities, photofile);
                            // imageUri = AppUtills.getOutputMediaFileUri(MEDIA_TYPE_IMAGE, TAG);
                            callcameraapplicationintent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

                            startActivityForResult(callcameraapplicationintent, REQUEST_CODE_CAMERA_ID);
                            // overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            dialog.dismiss();
                        }

                    }
                    else
                    {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        imageUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE, TAG);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        //       if(AppUtills.showLogs) Log.v(TAG,"captureimage...."+imageUri);
                        startActivityForResult(intent, REQUEST_CODE_CAMERA_ID);
                        // overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        dialog.dismiss();
                    }


                } else if (items[i].equals("Gallery")) {

                    Intent ir= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(ir,RESULT_LOAD_IMAGE);

                } else if (items[i].equals("Cancel")) {
                    dialog.dismiss();
                }

            }
        });
        builder.show();
    }
    public static Uri getOutputMediaFileUri(int type,String PageName){
        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists())
        {
            if (!mediaStorageDir.mkdirs())
            {
                //    if(AppUtills.showLogs)Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "+ IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String time = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE)
        {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + time + ".jpg");
        }
        else
        {
            return null;
        }
        return  Uri.fromFile(mediaFile);
    }
    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {

            e.printStackTrace();
            // String s= String.valueOf(e);
            //Toast.makeText(getApplicationContext(), String.valueOf(e), Toast.LENGTH_SHORT).show();

        }

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {

            if (data != null) {
                picturePath_id = getPath(data.getData(), getApplication());

                if (picturePath_id.length() > 0) {
                    //           if (AppUtills.showLogs) Log.d("filepath", "filepath " + picturePath_id);
                    selectedImageUri = data.getData();
                    pickImageFromGallery3(picturePath_id);
                }
            }
        }
        else if (requestCode == REQUEST_CODE_CAMERA_ID && resultCode==RESULT_OK)
        {
            if (checkAppVersion()) {
                selectedImageUri = imageUri;
                pickImageFromGallery3(photofile.getPath());
                // ScanFile so it will be appeared on Gallery
                MediaScannerConnection.scanFile(getApplication(),
                        new String[]{imageUri.getPath()}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {
                            }
                        });
            }else {
                selectedImageUri=imageUri;
                pickImageFromGallery3(imageUri.getPath());
            }
        }
    }
    public static String getPath(Uri uri, Context context){
        if (uri == null)
            return null;

        //    if(AppUtills.showLogs)Log.d("URI", uri + "");
        String[] projection = { MediaStore.Images.Media.DATA };

        Cursor cursor =context.getContentResolver().query(uri, projection,null, null, null);
        if (cursor != null)
        {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String temp = cursor.getString(column_index);
            cursor.close();
            //     if(AppUtills.showLogs)Log.v("temp",""+temp);
            return temp;
        }
        else
            return null;
    }

    public void pickImageFromGallery3(String imagePath)
    {
        try
        {
            picturePath_id=imagePath;
            Matrix matrix = new Matrix();
            int rotate = 0;

            File imageFile = new File(imagePath.toString());
            selectedImageFile3=imageFile;
            Log.e(TAG, "Image Size before comress : " + imageFile.length());

            try
            {
                ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL);

                switch(orientation)
                {
                    case  ExifInterface.ORIENTATION_ROTATE_270:
                        rotate-=90;
                        break;
                    case  ExifInterface.ORIENTATION_ROTATE_180:
                        rotate-=90;
                        break;
                    case  ExifInterface.ORIENTATION_ROTATE_90:
                        rotate-=90;
                        break;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            // Set the Image in ImageView after decoding the String
            matrix.postRotate(rotate);
            Bitmap user_image = BitmapFactory.decodeFile(imagePath);
            Log.e(TAG, "original user_image.getWidth()" + user_image.getWidth() + " user_image.getHeight()" + user_image.getHeight());

            if(imageFile.length()>10000)
            {
                Bitmap bitmap=compressImage(imagePath);
                Log.e(TAG, "compress bitmap.getWidth()" + bitmap.getWidth() + " bitmap.getHeight()" + bitmap.getHeight());

                File fileSize = new File(lastCompressedImageFileName);
                selectedImageFile3=fileSize;
                Log.e(TAG, "Image Size after in what comress : " + fileSize.length());
                if(fileSize.length()>50000)
                {
                    Toast.makeText(this, "Maximum image size limit less then 500KB.", Toast.LENGTH_SHORT).show();
                    picturePath_id=lastCompressedImageFileName;
                    bitmapProfileImage3=bitmap;
                    btn_photo.setImageBitmap(bitmap);
                    Set_image_path(picturePath_id);
                }
                else
                {
                    picturePath_id=lastCompressedImageFileName;
                    bitmapProfileImage3=bitmap;
                    btn_photo.setImageBitmap(bitmap);
                    Set_image_path(picturePath_id);
                }
            }
            else
            {
                int width=0,height=0;
                if(user_image.getWidth()>250)
                {
                    width=250;
                }
                else
                {
                    width=user_image.getWidth();
                }
                if(user_image.getHeight()>250)
                {
                    height=250;
                }
                else
                {
                    height=user_image.getHeight();
                }

                Bitmap bitmap=Bitmap.createBitmap(user_image, 0, 0, width,height, matrix, true);
                Log.e(TAG, "compress bitmap.getWidth()" + bitmap.getWidth() + " bitmap.getHeight()" + bitmap.getHeight());

                bitmapProfileImage3=bitmap;
                btn_photo.setImageBitmap(bitmap);
                Set_image_path(picturePath_id);
            }
        }
        catch (Exception e)
        {
            Toast.makeText(this, "error: "+e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public static Bitmap compressImage(String filePath)
    {
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 250.0f;
        float maxWidth = 250.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {               imgRatio = maxHeight / actualHeight;                actualWidth = (int) (imgRatio * actualWidth);               actualHeight = (int) maxHeight;             } else if (imgRatio > maxRatio) {
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
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,Bitmap.Config.ARGB_8888);
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
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        lastCompressedImageFileName = getFilename();
        try
        {
            out = new FileOutputStream(lastCompressedImageFileName);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.PNG, 30, out);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return scaledBitmap;
    }
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height/ (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;      }       final float totalPixels = width * height;       final float totalReqPixelsCap = reqWidth * reqHeight * 2;       while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public static String getFilename() {
        //File file = new File(Environment.getExternalStorageDirectory().getPath(),"RCM/Images");
        File file = new File(Environment.getExternalStorageDirectory().getPath(),IMAGE_DIRECTORY_NAME);
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;
    }



}
