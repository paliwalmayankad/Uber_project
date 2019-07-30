package aaronsoftech.in.unber.Activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import aaronsoftech.in.unber.R;

import static aaronsoftech.in.unber.Activity.Acc_edit.PATH_IMAGE;
import static aaronsoftech.in.unber.Activity.Acc_edit.profile_img;

public class Driver_photo extends AppCompatActivity {
    public  static final int RequestPermissionCode2  = 51 ;
    String[] locationPermissions = {"android.permission.READ_EXTERNAL_STORAGE","android.permission.WRITE_EXTERNAL_STORAGE","android.permission.CAMERA"};
    public  static final int REQUEST_CODE_LOCATIONlC  = 1 ;
    File photofile = null;
    Uri selectedImageUri;
    Uri imageUri;
    File selectedImageFile = null;
    Bitmap bitmapProfileImage = null;
    String mCurrentPhotoPath;
    static final int MEDIA_TYPE_IMAGE = 1;

    private static final int REQUEST_CODE_CAMERA = 106;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int REQUEST_CODE_IMAGE_FINAL = 14;
    String picturePath;
    Uri uri;
    String TAG="Driver_photo";
    Intent CropIntent ;
    ImageView btn_photo;
    String activity_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_photo);

        btn_photo=findViewById(R.id.image);
        TextView btn_image=findViewById(R.id.camera_id);
        getAllpermission();
        btn_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectimage();
            }
        });
        ImageView btn_back=findViewById(R.id.btn_ic_back);

        btn_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectimage();
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    private void selectimage() {

        final CharSequence[] items={"Camera","Gallery","Cancel"};
        AlertDialog.Builder builder=new AlertDialog.Builder(Driver_photo.this);
        builder.setTitle("Select Image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if(items[i].equals("Camera"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CODE_CAMERA);

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


    private void getAllpermission() {
        Handler handler  = new Handler();
        Runnable runnable = new Runnable()
        {
            @Override
            public void run()
            {

                if (ActivityCompat.checkSelfPermission(Driver_photo.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Driver_photo.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED
                        )                    {
                    ActivityCompat.requestPermissions(Driver_photo.this, locationPermissions, REQUEST_CODE_LOCATIONlC);
                    return;
                }
            }
        };
        handler.postDelayed(runnable, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage1 = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage1,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            Set_image_path(picturePath);
            cursor.close();
            profile_img.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            btn_photo.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            Set_image_path(picturePath);

        }
        else if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.PNG, 50, bytes);

            File destination = new File(Environment.getExternalStorageDirectory(),
                    System.currentTimeMillis() + ".png");

            FileOutputStream fo;
            try {
                destination.createNewFile();
                fo = new FileOutputStream(destination);
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            btn_photo .setImageBitmap(thumbnail);
            byte[] imageBytes = bytes.toByteArray();
            picturePath = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            profile_img.setImageBitmap(thumbnail);
            Set_image_path(picturePath);
            finish();
        }
    }

    private void Set_image_path(String picturePath) {
        PATH_IMAGE=picturePath;
        finish();

    }


}
