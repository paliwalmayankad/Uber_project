package aaronsoftech.in.nber.Activity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.util.HashMap;

import aaronsoftech.in.nber.App_Conteroller;
import aaronsoftech.in.nber.POJO.Response_Login;
import aaronsoftech.in.nber.POJO.Response_register;
import aaronsoftech.in.nber.R;
import aaronsoftech.in.nber.Service.APIClient;
import aaronsoftech.in.nber.Utils.App_Utils;
import aaronsoftech.in.nber.Utils.SP_Utils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Driver_document extends AppCompatActivity {
    TextView btn_licence,btn_pancard,btn_permit_a,
            btn_permit_b,btn_registertion,btn_insurence,btn_continue,btn_aadhar,btn_verification_file;
    ImageView btn_back;
    String TAG="Driver_document";
    ImageView img;
    public static String txt_aadharcard_no="",txt_pancard_no="",txt_licence_no="";
    public static String path_licence="",path_pancard="",path_permit_a="",
            path_permit_b="",path_registration="",path_insurense="",path_aadhar="",path_police_verification_file="";
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_document);
        btn_licence=findViewById(R.id.txt_licence);
        img=findViewById(R.id.pic);
        btn_verification_file=findViewById(R.id.txt_verify);
        btn_aadhar=findViewById(R.id.txt_aadhar);
        btn_pancard=findViewById(R.id.txt_pancard);
        btn_permit_a=findViewById(R.id.txt_permit_a);
        btn_permit_b=findViewById(R.id.txt_permit_b);
        btn_registertion=findViewById(R.id.txt_register_certificate);
        btn_insurence=findViewById(R.id.txt_insurence);
        btn_continue=findViewById(R.id.txt_continue);
        btn_back=findViewById(R.id.btn_ic_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //App_Utils.loadProfileImage(Driver_document.this,path_licence,img);
               Call_Document_submit_Api();
            }
        });


        btn_verification_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Driver_document.this,Driver_doc_Image.class)
                        .putExtra("type","8"));
            }
        });

        btn_aadhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Driver_document.this,Driver_doc_Image.class)
                        .putExtra("type","9"));
            }
        });
        btn_licence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Driver_document.this,Driver_doc_Image.class)
                        .putExtra("type","1"));
            }
        });
        btn_pancard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Driver_document.this,Driver_doc_Image.class)
                        .putExtra("type","3"));
            }
        });
        btn_permit_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Driver_document.this,Driver_doc_Image.class)
                        .putExtra("type","4"));
            }
        });
        btn_permit_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Driver_document.this,Driver_doc_Image.class)
                        .putExtra("type","6"));
            }
        });
        btn_registertion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Driver_document.this,Driver_doc_Image.class)
                        .putExtra("type","5"));
            }
        });
        btn_insurence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Driver_document.this,Driver_doc_Image.class)
                .putExtra("type","7"));
            }
        });
    }

    public void Update_info(final String driver_id)
    {

        HashMap<String,String> map=new HashMap<>();
        map.put("id", ""+App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_ID,""));
        map.put("if_driver_id", ""+driver_id);

        Call<Response_Login> call= APIClient.getWebServiceMethod().getUpdate_Profile(map);
        call.enqueue(new Callback<Response_Login>() {
            @Override
            public void onResponse(Call<Response_Login> call, Response<Response_Login> response) {
                String status=response.body().getApi_status();
                String msg=response.body().getApi_message();
                if (status.equalsIgnoreCase("1") && msg.equalsIgnoreCase("success") )
                {
                    App_Conteroller.sharedpreferences = getSharedPreferences(App_Conteroller.MyPREFERENCES, Context.MODE_PRIVATE);
                    App_Conteroller.editor = App_Conteroller.sharedpreferences.edit();
                    App_Conteroller. editor.putString(SP_Utils.LOGIN_DRIVER_ID,""+driver_id);
                    App_Conteroller. editor.commit();

                }else{
                    Toast.makeText(Driver_document.this, "msg "+msg+"\n"+"status "+status, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Response_Login> call, Throwable t) {
                Toast.makeText(Driver_document.this, "Error: "+t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Call_Document_submit_Api() {
        progressDialog=new ProgressDialog(Driver_document.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        File file_licence = new File(path_licence);
        File file_pancard = new File(path_pancard);
        File file_permit_a = new File(path_permit_a);
        File file_permit_b = new File(path_permit_b);
        File file_registration = new File(path_registration);
        File file_insurense = new File(path_insurense);

        File file_aadhar = new File(path_aadhar);
        File file_police_verification = new File(path_police_verification_file);

        RequestBody request_file_licence = RequestBody.create(MediaType.parse("multipart/form-data"), file_licence);
        RequestBody request_aadhar = RequestBody.create(MediaType.parse("multipart/form-data"), file_aadhar);
        RequestBody request_police_verification = RequestBody.create(MediaType.parse("multipart/form-data"), file_police_verification);
        RequestBody request_file_pancard = RequestBody.create(MediaType.parse("multipart/form-data"), file_pancard);
        RequestBody request_file_permit_a = RequestBody.create(MediaType.parse("multipart/form-data"), file_permit_a);
        RequestBody request_file_permit_b = RequestBody.create(MediaType.parse("multipart/form-data"), file_permit_b);
        RequestBody request_file_registration = RequestBody.create(MediaType.parse("multipart/form-data"), file_registration);
        RequestBody request_file_insurense = RequestBody.create(MediaType.parse("multipart/form-data"), file_insurense);

        MultipartBody.Part body_request_file_aadhar = MultipartBody.Part.createFormData("aadhar_file", file_aadhar.getName(), request_aadhar);
        MultipartBody.Part body_request_file_police_verify = MultipartBody.Part.createFormData("police_verification_file", file_police_verification.getName(), request_police_verification);

       MultipartBody.Part body_request_file_licence = MultipartBody.Part.createFormData("dl_file", file_licence.getName(), request_file_licence);
       MultipartBody.Part body_request_file_pancard = MultipartBody.Part.createFormData("pan_file", file_pancard.getName(), request_file_pancard);
     //   MultipartBody.Part body_request_file_permit_a = MultipartBody.Part.createFormData("dl_file", file_licence.getName(), request_file_permit_a);
     //   MultipartBody.Part body_request_file_permit_b = MultipartBody.Part.createFormData("dl_file", file_licence.getName(), request_file_permit_b);
     //  MultipartBody.Part body_request_file_registration = MultipartBody.Part.createFormData("file_registration", file_licence.getName(), request_file_registration);
     //   MultipartBody.Part body_request_file_insurense = MultipartBody.Part.createFormData("driver_insured_file", file_insurense.getName(), request_file_insurense);

        String userId= App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_ID,"");
        RequestBody body_user_id =RequestBody.create(okhttp3.MultipartBody.FORM, userId);
        RequestBody body_status =RequestBody.create(okhttp3.MultipartBody.FORM, "true");
        RequestBody body_verified_status =RequestBody.create(okhttp3.MultipartBody.FORM, "true");
        RequestBody body_dl_number =RequestBody.create(okhttp3.MultipartBody.FORM, ""+txt_licence_no);
        RequestBody body_aadhar_number =RequestBody.create(okhttp3.MultipartBody.FORM, ""+txt_aadharcard_no);
        RequestBody body_pan_number =RequestBody.create(okhttp3.MultipartBody.FORM, ""+txt_pancard_no);
        RequestBody body_police_verification_status =RequestBody.create(okhttp3.MultipartBody.FORM, "verify");
        RequestBody body_driver_insured_status =RequestBody.create(okhttp3.MultipartBody.FORM, "true");

         Call<Response_register> call=APIClient.getWebServiceMethod().driver_register(body_user_id,
                body_verified_status,body_dl_number,body_aadhar_number,body_pan_number,
                body_police_verification_status,body_driver_insured_status,
                body_status,body_request_file_licence,body_request_file_pancard,
                body_request_file_police_verify,body_request_file_aadhar );
        call.enqueue(new Callback<Response_register>() {
            @Override
            public void onResponse(Call<Response_register> call, Response<Response_register> response) {
                progressDialog.dismiss();
                Toast.makeText(Driver_document.this, "Submit your document ", Toast.LENGTH_SHORT).show();

                try{Log.i(TAG,"response driver getid:  "+response.body().getId());
                    Update_info(response.body().getId());}catch (Exception e){e.printStackTrace();}
                try{Log.i(TAG,"response driver getApi_message:  "+response.body().getApi_message());
                }catch (Exception e){e.printStackTrace();}
                try{Log.i(TAG,"response driver getApi_status:  "+response.body().getApi_status());
                }catch (Exception e){e.printStackTrace();}
                finish();
                }

            @Override
            public void onFailure(Call<Response_register> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(Driver_document.this, "Error: "+t.toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        Check_path_image();
    }

    private void Check_path_image() {
        if (path_licence!=""){ btn_licence.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.done, 0); }
        if (path_aadhar!=""){ btn_aadhar.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.done, 0); }
        if (path_police_verification_file!=""){ btn_verification_file.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.done, 0); }
        if (path_pancard!=""){ btn_pancard.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.done, 0); }
        if (path_permit_a!=""){ btn_permit_a.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.done, 0); }
        if (path_permit_b!=""){ btn_permit_b.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.done, 0); }
        if (path_registration!=""){ btn_registertion.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.done, 0); }
        if (path_insurense!=""){ btn_insurence.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.done, 0); }

    }
}
