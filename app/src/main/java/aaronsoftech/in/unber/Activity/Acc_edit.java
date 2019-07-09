package aaronsoftech.in.unber.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.HashMap;

import aaronsoftech.in.unber.App_Conteroller;
import aaronsoftech.in.unber.POJO.Response_Login;
import aaronsoftech.in.unber.R;
import aaronsoftech.in.unber.Service.APIClient;
import aaronsoftech.in.unber.Utils.SP_Utils;
import de.hdodenhof.circleimageview.CircleImageView;
import github.nisrulz.easydeviceinfo.base.EasyLocationMod;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Acc_edit extends AppCompatActivity {
    TextView t_name,t_mobile,t_email,btn_save,t_status;
    RadioButton rb_btn_male,rb_btn_female;
    String gender="";
    public static String Lat="0.0";
    public static String Longt="0.0";
    ProgressDialog progressDialog;
    CircleImageView profile_img;
    EditText ed_name,ed_address,ed_city,ed_email,ed_state,ed_country,tx_mobile,ed_zipcode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acc_edit);

        Init();

        Set_Profile_data();

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
                    gender="male";
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
        ed_name.setText(App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_NAME,""));
        t_name.setText(App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_NAME,""));

        String mobileno=App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_CONTACT_NUMBER,"");
        tx_mobile.setText(mobileno);
        if (tx_mobile.getText().toString().isEmpty())
        {
            tx_mobile.setEnabled(true);
        }else{
            tx_mobile.setEnabled(false);
        }

        try{
            String photo= App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_PHOTO,"");
            Picasso.with(Acc_edit.this).load(photo)
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
    }


    public void Update_info()
    {
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        HashMap<String,String> map=new HashMap<>();
        map.put("id", ""+App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_ID,""));
        map.put("name", ""+ed_name.getText().toString().trim());
        map.put("gender", ""+gender);
        map.put("photo", ""+App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_PHOTO,""));
        map.put("email", ""+ed_email.getText().toString());
    //   map.put("password", id);
    //   map.put("id_cms_privileges", id);
    //   map.put("status", id);
       map.put("contact_number", ""+tx_mobile.getText().toString().trim());
       map.put("address", ""+ed_address.getText().toString().trim());
       map.put("city", ""+ed_city.getText().toString().trim());
       map.put("state", ""+ed_state.getText().toString().trim());
       map.put("country", ""+ed_country.getText().toString().trim());
       map.put("password", "12345");
    //  map.put("lng", ""+Longt);
       map.put("zip_code", ""+ed_zipcode.getText().toString().trim());
    //    map.put("mac_id", id);
    //    map.put("passcode", id);
    //    map.put("if_driver_id", id);

        Call<Response_Login> call= APIClient.getWebServiceMethod().getUpdate_Profile(map);
        call.enqueue(new Callback<Response_Login>() {
            @Override
            public void onResponse(Call<Response_Login> call, Response<Response_Login> response) {
                String status=response.body().getApi_status();
                String msg=response.body().getApi_message();
                progressDialog.dismiss();
                if (status.equalsIgnoreCase("1") && msg.equalsIgnoreCase("success") )
                {

                        App_Conteroller.sharedpreferences = getSharedPreferences(App_Conteroller.MyPREFERENCES, Context.MODE_PRIVATE);
                        App_Conteroller.editor = App_Conteroller.sharedpreferences.edit();

                      //  App_Conteroller. editor.putString(SP_Utils.LOGIN_ID,""+response.body().getData().get(0).getId());
                        App_Conteroller. editor.putString(SP_Utils.LOGIN_NAME,""+ed_name.getText().toString().trim());

                        App_Conteroller. editor.putString(SP_Utils.LOGIN_GENDER,""+gender);
                      //  App_Conteroller. editor.putString(SP_Utils.LOGIN_PHOTO,""+response.body().getData().get(0).getPhoto());

                       App_Conteroller. editor.putString(SP_Utils.LOGIN_EMAIL,""+ed_email.getText().toString().trim());
                      //  App_Conteroller. editor.putString(SP_Utils.LOGIN_PASSWORD,""+response.body().getData().get(0).getPassword());
                      //  App_Conteroller. editor.putString(SP_Utils.LOGIN_ID_CMS_PRIVILEGES,""+response.body().getData().get(0).getId_cms_privileges());
                      //  App_Conteroller. editor.putString(SP_Utils.LOGIN_CMS_PRIVILEGES_NAME,""+response.body().getData().get(0).getCms_privileges_name());
                      //  App_Conteroller. editor.putString(SP_Utils.LOGIN_CMS_PRIVILEGES_IS_SUPERADMIN,""+response.body().getData().get(0).getCms_privileges_is_superadmin());
                      //  App_Conteroller. editor.putString(SP_Utils.LOGIN_CMS_PRIVILEGES_THEME_COLOR,""+response.body().getData().get(0).getCms_privileges_theme_color());
                      //  App_Conteroller. editor.putString(SP_Utils.LOGIN_STATUS,""+response.body().getData().get(0).getStatus());

                       /* if (response.body().getData().get(0).getContact_number()==null)
                        {
                            App_Conteroller. editor.putString(SP_Utils.LOGIN_CONTACT_NUMBER,"");
                        }else{
                            App_Conteroller. editor.putString(SP_Utils.LOGIN_CONTACT_NUMBER,""+response.body().getData().get(0).getContact_number());
                        }*/

                    App_Conteroller. editor.putString(SP_Utils.LOGIN_CONTACT_NUMBER,""+tx_mobile.getText().toString().trim());

                    App_Conteroller. editor.putString(SP_Utils.LOGIN_ADDRESS,""+ed_address.getText().toString().trim());

                    App_Conteroller. editor.putString(SP_Utils.LOGIN_CITY,""+ed_city.getText().toString().trim());

                    App_Conteroller. editor.putString(SP_Utils.LOGIN_STATE,""+ed_state.getText().toString().trim());


                    App_Conteroller. editor.putString(SP_Utils.LOGIN_COUNTER,""+ed_country.getText().toString().trim());
                      //  App_Conteroller. editor.putString(SP_Utils.LOGIN_LAT,""+response.body().getData().get(0).getLat());
                      //  App_Conteroller. editor.putString(SP_Utils.LOGIN_LNG,""+response.body().getData().get(0).getLng());
                    App_Conteroller. editor.putString(SP_Utils.LOGIN_ZIP_CODE,""+ed_zipcode.getText().toString().trim());
                      //  App_Conteroller. editor.putString(SP_Utils.LOGIN_MAC_ID,""+response.body().getData().get(0).getMac_id());
                      //  App_Conteroller. editor.putString(SP_Utils.LOGIN_SOCIAL_TYPE,""+response.body().getData().get(0).getSocial_type());
                      //  App_Conteroller. editor.putString(SP_Utils.LOGIN_TOKEN_ID,""+response.body().getData().get(0).getToken_id());
                      //  App_Conteroller. editor.putString(SP_Utils.LOGIN_PASSCODE,""+response.body().getData().get(0).getPasscode());
                      //  App_Conteroller. editor.putString(SP_Utils.LOGIN_USR_STATUS,""+response.body().getData().get(0).getUsr_status());

                    App_Conteroller. editor.commit();
                    Toast.makeText(getApplicationContext(), "Update Successfully", Toast.LENGTH_LONG).show();

                }else{
                    progressDialog.dismiss();
                    Toast.makeText(Acc_edit.this, "msg "+msg+"\n"+"status "+status, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Response_Login> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(Acc_edit.this, "Error: "+t.toString(), Toast.LENGTH_SHORT).show();
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
