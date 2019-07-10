package aaronsoftech.in.unber.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

import aaronsoftech.in.unber.App_Conteroller;
import aaronsoftech.in.unber.POJO.Response_Login;
import aaronsoftech.in.unber.POJO.Response_register;
import aaronsoftech.in.unber.R;
import aaronsoftech.in.unber.Service.APIClient;
import aaronsoftech.in.unber.Utils.SP_Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static aaronsoftech.in.unber.Utils.App_Utils.isNetworkAvailable;

public class Verification extends AppCompatActivity {
    public static String Lat="0.0";
    public static String Longt="0.0";
    String mobileno,refreshedToken;
    ProgressDialog progressDialog;
    String TAG="Verification";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        ImageView btn_next=findViewById(R.id.next_button);
        mobileno=getIntent().getExtras().getString("mobile");
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Call_Api_contact(mobileno);
            }
        });

    }

    private void Call_Api_contact(String mobileno) {
        HashMap<String,String> login_map=new HashMap<>();
        login_map.put("contact_number",""+mobileno);

        HashMap<String,String>register_map=new HashMap<>();
        register_map.put("id_cms_privileges","4");
        register_map.put("contact_number",""+mobileno);
        register_map.put("lat",""+Lat);
        register_map.put("lng",""+Longt);
        register_map.put("mac_id","0");
        register_map.put("social_type","contact");
        register_map.put("token_id",""+refreshedToken);
        register_map.put("name","");
        register_map.put("email","");
        Log.i(TAG,"Token ID : "+refreshedToken);
        Api_Social_login(login_map,register_map);

    }

    private void Call_Register_Api(Map map){
        if (isNetworkAvailable(Verification.this))
        {
            Call<Response_register> call= APIClient.getWebServiceMethod().getRegister(map);
            call.enqueue(new Callback<Response_register>() {
                @Override
                public void onResponse(Call<Response_register> call, Response<Response_register> response) {

                    String status=response.body().getApi_status();
                    String msg=response.body().getApi_message();
                    if (status.equalsIgnoreCase("1") && msg.equalsIgnoreCase("success") )
                    {
                        String id=response.body().getId();
                        Toast.makeText(Verification.this, "msg "+msg+"\n"+"id"+id, Toast.LENGTH_SHORT).show();
                        get_login_with_Id(id);
                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(Verification.this, "status "+status+"\n"+"msg "+msg, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Response_register> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(Verification.this, "Error : "+t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(Verification.this, "No Internet", Toast.LENGTH_SHORT).show();
        }




    }

    private void get_login_with_Id(String id) {

        HashMap<String,String>map=new HashMap<>();
        map.put("id", id);
        if (isNetworkAvailable(Verification.this))
        {
            Call<Response_Login> call= APIClient.getWebServiceMethod().getLogin_with_id(map);
            call.enqueue(new Callback<Response_Login>() {
                @Override
                public void onResponse(Call<Response_Login> call, Response<Response_Login> response) {

                    String status=response.body().getApi_status();
                    String msg=response.body().getApi_message();
                    progressDialog.dismiss();
                    if (status.equalsIgnoreCase("1") && msg.equalsIgnoreCase("success") )
                    {
                        int size_list=response.body().getData().size();
                        if (size_list!=0)
                        {
                            progressDialog.dismiss();
                            App_Conteroller.sharedpreferences = getSharedPreferences(App_Conteroller.MyPREFERENCES, Context.MODE_PRIVATE);
                            App_Conteroller.editor = App_Conteroller.sharedpreferences.edit();


                            App_Conteroller. editor.putString(SP_Utils.LOGIN_ID,""+response.body().getData().get(0).getId());
                            if (response.body().getData().get(0).getName()==null)
                            {       App_Conteroller. editor.putString(SP_Utils.LOGIN_NAME,"");
                            }else{  App_Conteroller. editor.putString(SP_Utils.LOGIN_NAME,""+response.body().getData().get(0).getName());                    }


                            if (response.body().getData().get(0).getGender()==null)
                            {         App_Conteroller. editor.putString(SP_Utils.LOGIN_GENDER,"");
                            }else{    App_Conteroller. editor.putString(SP_Utils.LOGIN_GENDER,""+response.body().getData().get(0).getGender());              }

                            if (response.body().getData().get(0).getPhoto()==null)
                            {          App_Conteroller. editor.putString(SP_Utils.LOGIN_PHOTO,"");
                            }else{     App_Conteroller. editor.putString(SP_Utils.LOGIN_PHOTO,""+response.body().getData().get(0).getPhoto());               }

                            if (response.body().getData().get(0).getEmail()==null)
                            {       App_Conteroller. editor.putString(SP_Utils.LOGIN_EMAIL,"");          }
                            else{   App_Conteroller. editor.putString(SP_Utils.LOGIN_EMAIL,""+response.body().getData().get(0).getEmail());                  }

                            App_Conteroller.editor.putString(SP_Utils.LOGIN_PASSWORD,""+response.body().getData().get(0).getPassword());
                            App_Conteroller.editor.putString(SP_Utils.LOGIN_ID_CMS_PRIVILEGES,""+response.body().getData().get(0).getId_cms_privileges());
                            App_Conteroller.editor.putString(SP_Utils.LOGIN_CMS_PRIVILEGES_NAME,""+response.body().getData().get(0).getCms_privileges_name());
                            App_Conteroller.editor.putString(SP_Utils.LOGIN_CMS_PRIVILEGES_IS_SUPERADMIN,""+response.body().getData().get(0).getCms_privileges_is_superadmin());
                            App_Conteroller.editor.putString(SP_Utils.LOGIN_CMS_PRIVILEGES_THEME_COLOR,""+response.body().getData().get(0).getCms_privileges_theme_color());

                            if (response.body().getData().get(0).getStatus()==null)
                            {          App_Conteroller. editor.putString(SP_Utils.LOGIN_STATUS,"");
                            }else{     App_Conteroller. editor.putString(SP_Utils.LOGIN_STATUS,""+response.body().getData().get(0).getStatus());                                  }


                            if (response.body().getData().get(0).getContact_number()==null)
                            {         App_Conteroller. editor.putString(SP_Utils.LOGIN_CONTACT_NUMBER,"");
                            }else{    App_Conteroller. editor.putString(SP_Utils.LOGIN_CONTACT_NUMBER,""+response.body().getData().get(0).getContact_number());                    }


                            if (response.body().getData().get(0).getAddress()==null)
                            {         App_Conteroller. editor.putString(SP_Utils.LOGIN_ADDRESS,"");
                            }else{    App_Conteroller. editor.putString(SP_Utils.LOGIN_ADDRESS,""+response.body().getData().get(0).getAddress());                               }


                            if (response.body().getData().get(0).getCity()==null)
                            {         App_Conteroller. editor.putString(SP_Utils.LOGIN_CITY,"");
                            }else{    App_Conteroller. editor.putString(SP_Utils.LOGIN_CITY,""+response.body().getData().get(0).getCity());                                   }

                            if(response.body().getData().get(0).getState()==null)
                            {        App_Conteroller. editor.putString(SP_Utils.LOGIN_STATE,"");
                            }else{   App_Conteroller. editor.putString(SP_Utils.LOGIN_STATE,""+response.body().getData().get(0).getState());                  }


                            if (response.body().getData().get(0).getCountry()==null)
                            {        App_Conteroller. editor.putString(SP_Utils.LOGIN_COUNTER,"");
                            }else{   App_Conteroller. editor.putString(SP_Utils.LOGIN_COUNTER,""+response.body().getData().get(0).getCountry());               }


                            App_Conteroller. editor.putString(SP_Utils.LOGIN_LAT,""+response.body().getData().get(0).getLat());
                            App_Conteroller. editor.putString(SP_Utils.LOGIN_LNG,""+response.body().getData().get(0).getLng());

                            if (response.body().getData().get(0).getZip_code()==null)
                            {          App_Conteroller. editor.putString(SP_Utils.LOGIN_ZIP_CODE,"");
                            }else{     App_Conteroller. editor.putString(SP_Utils.LOGIN_ZIP_CODE,""+response.body().getData().get(0).getZip_code());            }

                            App_Conteroller. editor.putString(SP_Utils.LOGIN_MAC_ID,""+response.body().getData().get(0).getMac_id());
                            App_Conteroller. editor.putString(SP_Utils.LOGIN_SOCIAL_TYPE,""+response.body().getData().get(0).getSocial_type());
                            App_Conteroller. editor.putString(SP_Utils.LOGIN_TOKEN_ID,""+response.body().getData().get(0).getToken_id());
                            App_Conteroller. editor.putString(SP_Utils.LOGIN_PASSCODE,""+response.body().getData().get(0).getPasscode());

                            if (response.body().getData().get(0).getUsr_status()==null)
                            {        App_Conteroller. editor.putString(SP_Utils.LOGIN_USR_STATUS,"");
                            }else{   App_Conteroller. editor.putString(SP_Utils.LOGIN_USR_STATUS,""+response.body().getData().get(0).getUsr_status());                    }

                            App_Conteroller. editor.commit();
                            Toast.makeText(getApplicationContext(), "Wel-Come", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(Verification.this, Home.class));
                            finish();
                        }
                    }else{
                        Toast.makeText(Verification.this, "msg "+msg+"\n"+"status "+status, Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<Response_Login> call, Throwable t) {
                    Toast.makeText(Verification.this, "Error "+t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(Verification.this, "No Internet", Toast.LENGTH_SHORT).show();
        }


    }

    private void Api_Social_login(HashMap<String, String> login_map, final HashMap<String, String> register_map) {
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        if (isNetworkAvailable(Verification.this))
        {
            Call<Response_Login> call= APIClient.getWebServiceMethod().getContect_Login(login_map);
            call.enqueue(new Callback<Response_Login>() {
                @Override
                public void onResponse(Call<Response_Login> call, Response<Response_Login> response) {

                    String status=response.body().getApi_status();
                    String msg=response.body().getApi_message();
                    if (status.equalsIgnoreCase("1") && msg.equalsIgnoreCase("success") )
                    {
                        int size_list=response.body().getData().size();
                        if (size_list==0)
                        {
                            Call_Register_Api(register_map);
                        }else{
                            progressDialog.dismiss();
                            App_Conteroller.sharedpreferences = getSharedPreferences(App_Conteroller.MyPREFERENCES, Context.MODE_PRIVATE);
                            App_Conteroller.editor = App_Conteroller.sharedpreferences.edit();

                            App_Conteroller. editor.putString(SP_Utils.LOGIN_ID,""+response.body().getData().get(0).getId());
                            if (response.body().getData().get(0).getName()==null)
                            {       App_Conteroller. editor.putString(SP_Utils.LOGIN_NAME,"");
                            }else{  App_Conteroller. editor.putString(SP_Utils.LOGIN_NAME,""+response.body().getData().get(0).getName());                    }


                            if (response.body().getData().get(0).getGender()==null)
                            {         App_Conteroller. editor.putString(SP_Utils.LOGIN_GENDER,"");
                            }else{    App_Conteroller. editor.putString(SP_Utils.LOGIN_GENDER,""+response.body().getData().get(0).getGender());              }

                            if (response.body().getData().get(0).getPhoto()==null)
                            {          App_Conteroller. editor.putString(SP_Utils.LOGIN_PHOTO,"");
                            }else{     App_Conteroller. editor.putString(SP_Utils.LOGIN_PHOTO,""+response.body().getData().get(0).getPhoto());               }

                            if (response.body().getData().get(0).getEmail()==null)
                            {       App_Conteroller. editor.putString(SP_Utils.LOGIN_EMAIL,"");          }
                            else{   App_Conteroller. editor.putString(SP_Utils.LOGIN_EMAIL,""+response.body().getData().get(0).getEmail());                  }

                            App_Conteroller.editor.putString(SP_Utils.LOGIN_DRIVER_ID,""+response.body().getData().get(0).getIf_driver_id());

                            App_Conteroller.editor.putString(SP_Utils.LOGIN_PASSWORD,""+response.body().getData().get(0).getPassword());
                            App_Conteroller.editor.putString(SP_Utils.LOGIN_ID_CMS_PRIVILEGES,""+response.body().getData().get(0).getId_cms_privileges());
                            App_Conteroller.editor.putString(SP_Utils.LOGIN_CMS_PRIVILEGES_NAME,""+response.body().getData().get(0).getCms_privileges_name());
                            App_Conteroller.editor.putString(SP_Utils.LOGIN_CMS_PRIVILEGES_IS_SUPERADMIN,""+response.body().getData().get(0).getCms_privileges_is_superadmin());
                            App_Conteroller.editor.putString(SP_Utils.LOGIN_CMS_PRIVILEGES_THEME_COLOR,""+response.body().getData().get(0).getCms_privileges_theme_color());

                            if (response.body().getData().get(0).getStatus()==null)
                            {          App_Conteroller. editor.putString(SP_Utils.LOGIN_STATUS,"");
                            }else{     App_Conteroller. editor.putString(SP_Utils.LOGIN_STATUS,""+response.body().getData().get(0).getStatus());                                  }


                            if (response.body().getData().get(0).getContact_number()==null)
                            {         App_Conteroller. editor.putString(SP_Utils.LOGIN_CONTACT_NUMBER,"");
                            }else{    App_Conteroller. editor.putString(SP_Utils.LOGIN_CONTACT_NUMBER,""+response.body().getData().get(0).getContact_number());                    }


                            if (response.body().getData().get(0).getAddress()==null)
                            {         App_Conteroller. editor.putString(SP_Utils.LOGIN_ADDRESS,"");
                            }else{    App_Conteroller. editor.putString(SP_Utils.LOGIN_ADDRESS,""+response.body().getData().get(0).getAddress());                               }


                            if (response.body().getData().get(0).getCity()==null)
                            {         App_Conteroller. editor.putString(SP_Utils.LOGIN_CITY,"");
                            }else{    App_Conteroller. editor.putString(SP_Utils.LOGIN_CITY,""+response.body().getData().get(0).getCity());                                   }

                            if(response.body().getData().get(0).getState()==null)
                            {        App_Conteroller. editor.putString(SP_Utils.LOGIN_STATE,"");
                            }else{   App_Conteroller. editor.putString(SP_Utils.LOGIN_STATE,""+response.body().getData().get(0).getState());                  }


                            if (response.body().getData().get(0).getCountry()==null)
                            {        App_Conteroller. editor.putString(SP_Utils.LOGIN_COUNTER,"");
                            }else{   App_Conteroller. editor.putString(SP_Utils.LOGIN_COUNTER,""+response.body().getData().get(0).getCountry());               }


                            App_Conteroller. editor.putString(SP_Utils.LOGIN_LAT,""+response.body().getData().get(0).getLat());
                            App_Conteroller. editor.putString(SP_Utils.LOGIN_LNG,""+response.body().getData().get(0).getLng());


                            if (response.body().getData().get(0).getZip_code()==null)
                            {          App_Conteroller. editor.putString(SP_Utils.LOGIN_ZIP_CODE,"");
                            }else{     App_Conteroller. editor.putString(SP_Utils.LOGIN_ZIP_CODE,""+response.body().getData().get(0).getZip_code());            }


                            App_Conteroller. editor.putString(SP_Utils.LOGIN_MAC_ID,""+response.body().getData().get(0).getMac_id());
                            App_Conteroller. editor.putString(SP_Utils.LOGIN_SOCIAL_TYPE,""+response.body().getData().get(0).getSocial_type());
                            App_Conteroller. editor.putString(SP_Utils.LOGIN_TOKEN_ID,""+response.body().getData().get(0).getToken_id());
                            App_Conteroller. editor.putString(SP_Utils.LOGIN_PASSCODE,""+response.body().getData().get(0).getPasscode());

                            if (response.body().getData().get(0).getUsr_status()==null)
                            {        App_Conteroller. editor.putString(SP_Utils.LOGIN_USR_STATUS,"");
                            }else{   App_Conteroller. editor.putString(SP_Utils.LOGIN_USR_STATUS,""+response.body().getData().get(0).getUsr_status());                    }


                            App_Conteroller. editor.commit();
                            Toast.makeText(getApplicationContext(), "Wel-Come", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(Verification.this, Home.class));
                            finish();
                        }
                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(Verification.this, "status "+status+"\n"+"msg "+msg, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Response_Login> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(Verification.this, "Error "+t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(Verification.this, "No Internet", Toast.LENGTH_SHORT).show();
        }

    }

}
