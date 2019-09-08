package aaronsoftech.in.nber.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;

import aaronsoftech.in.nber.App_Conteroller;
import aaronsoftech.in.nber.POJO.Response_Login;
import aaronsoftech.in.nber.R;
import aaronsoftech.in.nber.Service.APIClient;
import aaronsoftech.in.nber.Utils.SP_Utils;

import de.hdodenhof.circleimageview.CircleImageView;

import github.nisrulz.easydeviceinfo.base.EasyLocationMod;
import jp.wasabeef.picasso.transformations.BlurTransformation;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static aaronsoftech.in.nber.Utils.App_Utils.isNetworkAvailable;

public class Acc_edit extends AppCompatActivity  {
    TextView t_name,t_mobile,t_email,btn_save,t_status;
    RadioButton rb_btn_male,rb_btn_female;
    String gender="Male";
    String TAG="Acc_edit";
    public static String Lat="0.0";
    public static String Longt="0.0";
    ProgressDialog progressDialog;
    public static CircleImageView profile_img;
    public static String PATH_IMAGE="";
    EditText ed_name,ed_address,ed_city,ed_email,ed_state,ed_country,tx_mobile,ed_zipcode;
    Toolbar mToolbar22;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acc_edit);

        Init();

        mToolbar22 = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(mToolbar22);

        AppBarLayout mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
            }
        });


        Set_Profile_data();

        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Acc_edit.this,Driver_photo.class));
            }
        });

        try {
               EasyLocationMod easyLocationMod = new EasyLocationMod(Acc_edit.this);
               if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Acc_edit.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                  return;
                }
                double[] l = easyLocationMod.getLatLong();
                Lat = String.valueOf(l[0]);
                Longt = String.valueOf(l[1]);
             }catch (Exception e){e.printStackTrace();}

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rb_btn_female.isChecked())
                {
                    gender="Female";
                }else{
                    gender="Male";
                }

                if (ed_name.getText().toString().isEmpty())
                {
                    ed_name.setError("Enter Name");
                    ed_name.requestFocus();
                }else if (ed_email.getText().toString().isEmpty())
                {
                    ed_email.setError("Enter Email-Id");
                    ed_email.requestFocus();
                }else if (ed_address.getText().toString().isEmpty())
                {
                    ed_address.setError("Enter Address");
                    ed_address.requestFocus();
                }else if (PATH_IMAGE.toString().isEmpty() || PATH_IMAGE.equalsIgnoreCase(null) || PATH_IMAGE.equalsIgnoreCase(null))
                {
                    Toast.makeText(Acc_edit.this, "Select Image", Toast.LENGTH_SHORT).show();
                }else if (ed_city.getText().toString().isEmpty())
                {
                    ed_city.setError("Enter City");
                    ed_city.requestFocus();
                }else if (ed_state.getText().toString().isEmpty())
                {
                    ed_state.setError("Enter State");
                    ed_state.requestFocus();
                }else if (ed_country.getText().toString().isEmpty())
                {
                    ed_country.setError("Enter Country");
                    ed_country.requestFocus();
                }else{
                    Update_info();
                }

            }
        });
    }

    private void Set_Profile_data() {
        try{
            ed_name.setText(App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_NAME,""));
            t_name.setText(App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_NAME,""));
            String name=App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_NAME,"");
            //mToolbar22.setTitle(App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_NAME,""));
            mToolbar22.setTitle(name);

            String mobileno=App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_CONTACT_NUMBER,"");
            tx_mobile.setText(mobileno);
            if (tx_mobile.getText().toString().isEmpty())
            {
                tx_mobile.setEnabled(true);
            }else{
                tx_mobile.setEnabled(false);
            }



            try{
                PATH_IMAGE= App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_PHOTO,"");

                Picasso.with(Acc_edit.this).load(PATH_IMAGE)
                        .placeholder(R.drawable.ic_user)
                        .error(R.drawable.ic_user)
                        .into(profile_img);
            }catch (Exception e){e.printStackTrace();}


            t_email.setText(App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_EMAIL,""));
            ed_address.setText(App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_ADDRESS,""));
            ed_city.setText(App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_CITY,""));
            ed_state.setText(App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_STATE,""));
            t_mobile.setText(App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_CONTACT_NUMBER,""));
            ed_country.setText(App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_COUNTER,""));
            tx_mobile.setText(App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_CONTACT_NUMBER,""));
            ed_zipcode.setText(App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_ZIP_CODE,""));
            t_status.setText(App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_USR_STATUS,""));
            String gender_txt=App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_GENDER,"");
            ed_email.setText(App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_EMAIL,""));
            if (gender_txt.equalsIgnoreCase("Female"))
            {
                rb_btn_female.setChecked(true);
                rb_btn_male.setChecked(false);
                gender="Female";
            }else{
                rb_btn_female.setChecked(false);
                rb_btn_male.setChecked(true);
                gender="male";
            }
        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    protected void onResume() {
        Log.i(TAG,"Image path: "+PATH_IMAGE);
        super.onResume();
    }

    public void Update_info()
    {
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        File file = new File(PATH_IMAGE);
        RequestBody request_file = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body_request_file_aadhar = MultipartBody.Part.createFormData("photo", file.getName(), request_file);
        RequestBody id =RequestBody.create(okhttp3.MultipartBody.FORM, ""+App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_ID,""));
        RequestBody name =RequestBody.create(okhttp3.MultipartBody.FORM, ""+ed_name.getText().toString().trim());
        RequestBody d_gender =RequestBody.create(okhttp3.MultipartBody.FORM, gender);
        RequestBody email =RequestBody.create(okhttp3.MultipartBody.FORM, ""+ed_email.getText().toString());
        RequestBody contact_number =RequestBody.create(okhttp3.MultipartBody.FORM, ""+tx_mobile.getText().toString().trim());
        RequestBody address =RequestBody.create(okhttp3.MultipartBody.FORM, ""+ed_address.getText().toString().trim());
        RequestBody city =RequestBody.create(okhttp3.MultipartBody.FORM, ed_city.getText().toString().trim());
        RequestBody state =RequestBody.create(okhttp3.MultipartBody.FORM, ""+ed_state.getText().toString().trim());
        RequestBody country =RequestBody.create(okhttp3.MultipartBody.FORM, ""+ed_country.getText().toString().trim());
        RequestBody password =RequestBody.create(okhttp3.MultipartBody.FORM, "12345");
        RequestBody zip_code =RequestBody.create(okhttp3.MultipartBody.FORM, ""+ed_zipcode.getText().toString().trim());

        if (isNetworkAvailable(Acc_edit.this))
        {
            Call<Response_Login> call=null;

            if (PATH_IMAGE== App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_PHOTO,""))
            {
                call= APIClient.getWebServiceMethod().getUpdate_Profile_without_pic(id,name,d_gender,email,contact_number,address,city,state,country,password,zip_code);

            }else{
                call= APIClient.getWebServiceMethod().getUpdate_Profile(id,name,d_gender,email,contact_number,address,city,state,country,password,zip_code,body_request_file_aadhar);

            }

            call.enqueue(new Callback<Response_Login>() {
                @Override
                public void onResponse(Call<Response_Login> call, Response<Response_Login> response) {
                    try{
                        String status=response.body().getApi_status().toString();
                        String msg=response.body().getApi_message().toString();
                        progressDialog.dismiss();
                        if (status.equalsIgnoreCase("1") && msg.equalsIgnoreCase("success") )
                        {

                            App_Conteroller.sharedpreferences = getSharedPreferences(App_Conteroller.MyPREFERENCES, Context.MODE_PRIVATE);
                            App_Conteroller.editor = App_Conteroller.sharedpreferences.edit();

                            App_Conteroller. editor.putString(SP_Utils.LOGIN_NAME,""+ed_name.getText().toString().trim());

                            App_Conteroller. editor.putString(SP_Utils.LOGIN_GENDER,""+gender);

                            App_Conteroller. editor.putString(SP_Utils.LOGIN_EMAIL,""+ed_email.getText().toString().trim());

                            App_Conteroller. editor.putString(SP_Utils.LOGIN_CONTACT_NUMBER,""+tx_mobile.getText().toString().trim());

                            App_Conteroller. editor.putString(SP_Utils.LOGIN_ADDRESS,""+ed_address.getText().toString().trim());

                            App_Conteroller. editor.putString(SP_Utils.LOGIN_CITY,""+ed_city.getText().toString().trim());

                            App_Conteroller. editor.putString(SP_Utils.LOGIN_STATE,""+ed_state.getText().toString().trim());

                            App_Conteroller. editor.putString(SP_Utils.LOGIN_COUNTER,""+ed_country.getText().toString().trim());

                            App_Conteroller. editor.putString(SP_Utils.LOGIN_ZIP_CODE,""+ed_zipcode.getText().toString().trim());

                            App_Conteroller. editor.commit();

                            Call_Image_Api(App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_ID,""));

                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(Acc_edit.this, "msg  "+msg+"\n"+"status  "+status, Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        Update_info();
                //        Toast.makeText(Acc_edit.this, "try again...!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();}
                }

                @Override
                public void onFailure(Call<Response_Login> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(Acc_edit.this, "Error: "+t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            progressDialog.dismiss();
            Toast.makeText(Acc_edit.this, "No Internet", Toast.LENGTH_SHORT).show();
        }

    }

    private void Call_Image_Api(String string) {
        HashMap<String,String>map=new HashMap<>();
        map.put("id", string);

        Call<Response_Login> call= APIClient.getWebServiceMethod().getLogin_with_id(map);
        call.enqueue(new Callback<Response_Login>() {
            @Override
            public void onResponse(Call<Response_Login> call, Response<Response_Login> response) {
                progressDialog.dismiss();
                App_Conteroller.sharedpreferences = getSharedPreferences(App_Conteroller.MyPREFERENCES, Context.MODE_PRIVATE);
                App_Conteroller.editor = App_Conteroller.sharedpreferences.edit();
                App_Conteroller. editor.putString(SP_Utils.LOGIN_PHOTO,""+response.body().getData().get(0).photo);
                App_Conteroller. editor.commit();
                Toast.makeText(getApplicationContext(), "Profile saved", Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onFailure(Call<Response_Login> call, Throwable t) {

            }
        });

    }


    private void Init() {

        profile_img=findViewById(R.id.profile_image);
        t_status=findViewById(R.id.user_status);
        ed_zipcode=findViewById(R.id.e_zipcode);
        btn_save=findViewById(R.id.btn_save);
        rb_btn_female=findViewById(R.id.rb_female);
        rb_btn_male=findViewById(R.id.rb_male);
        t_name=findViewById(R.id.txt_name);
        t_mobile=findViewById(R.id.txt_mobile);
        tx_mobile=findViewById(R.id.t_mobile);
        t_email=findViewById(R.id.txt_emailid);
        ed_name=findViewById(R.id.e_name);
        ed_address=findViewById(R.id.e_address);
        ed_city=findViewById(R.id.e_city);
        ed_state=findViewById(R.id.e_state);
        ed_country=findViewById(R.id.e_country);
        ed_email=findViewById(R.id.e_email);

        ImageView backbtn=findViewById(R.id.btn_back);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }

}
