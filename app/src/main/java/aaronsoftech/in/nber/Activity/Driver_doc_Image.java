package aaronsoftech.in.nber.Activity;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import aaronsoftech.in.nber.R;
import aaronsoftech.in.nber.Utils.App_Utils;

import static aaronsoftech.in.nber.Activity.Driver_document.path_insurense;
import static aaronsoftech.in.nber.Activity.Driver_document.path_licence;
import static aaronsoftech.in.nber.Activity.Driver_document.path_pancard;
import static aaronsoftech.in.nber.Activity.Driver_document.path_permit_a;
import static aaronsoftech.in.nber.Activity.Driver_document.path_permit_b;
import static aaronsoftech.in.nber.Activity.Driver_document.path_registration;

public class Driver_doc_Image extends AppCompatActivity {
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
    String TAG="Driver_doc_Image";
    Intent CropIntent ;
    ImageView btn_photo;
    String activity_type;
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
        }

        TextView btn_submit=findViewById(R.id.camera_id);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        getAllpermission();
    }

    private void selectimage() {

        final CharSequence[] items={"Camera","Gallery","Cancel"};
        AlertDialog.Builder builder=new AlertDialog.Builder(Driver_doc_Image.this);
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
            btn_photo.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            finish();
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

            Set_image_path(picturePath);
            finish();
        }
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
        }
    }

}
