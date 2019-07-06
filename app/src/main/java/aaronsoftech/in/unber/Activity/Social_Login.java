package aaronsoftech.in.unber.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import aaronsoftech.in.unber.App_Conteroller;
import aaronsoftech.in.unber.POJO.Response_Login;
import aaronsoftech.in.unber.POJO.Response_register;
import aaronsoftech.in.unber.R;
import aaronsoftech.in.unber.Service.APIClient;
import aaronsoftech.in.unber.Utils.SP_Utils;
import github.nisrulz.easydeviceinfo.base.EasyLocationMod;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Social_Login extends AppCompatActivity implements
        View.OnClickListener {
    String[] locationPermissionsl = {"android.permission.ACCESS_FINE_LOCATION","android.permission.ACCESS_COARSE_LOCATION"};
    private static int REQUEST_CODE_LOCATIONl = 102;
    public static String Lat="0.0";
    public static String Longt="0.0";
    private TextView mDetailTextView;
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    AccessToken accessToken;
    String refreshedToken;
    String socialId;
    String photourl;
    private CallbackManager mcallbackManager;
    LoginButton fb_login_button;
    ProgressDialog progressDialog;
    String  fb_email="",fb_name="",fblastname="",fbemailid="";
    private TextView mStatusTextView;
    Context con;
    private GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_social__login2);

        ImageView btn_back1=findViewById(R.id.btn_back);
        ImageView btn_back2=findViewById(R.id.btn_back2);

        btn_back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        FAceBook_Login();

        Google_login();

        Give_Permission();

        get_LOCATION();
    }

    private void Give_Permission() {
        Handler handler  = new Handler();
        Runnable runnable = new Runnable()
        {
            @Override
            public void run()
            {

                if (ActivityCompat.checkSelfPermission(getApplication(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplication(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        )                    {
                    ActivityCompat.requestPermissions(Social_Login.this, locationPermissionsl, REQUEST_CODE_LOCATIONl);
                }
            }
        };
        handler.postDelayed(runnable, 1000);
    }

    private void Call_Register_Api(Map map){

        Call<Response_register> call= APIClient.getWebServiceMethod().getRegister(map);
        call.enqueue(new Callback<Response_register>() {
            @Override
            public void onResponse(Call<Response_register> call, Response<Response_register> response) {

                String status=response.body().getApi_status();
                String msg=response.body().getApi_message();
                if (status.equalsIgnoreCase("1") && msg.equalsIgnoreCase("success") )
                {
                    String id=response.body().getId();
                    Toast.makeText(Social_Login.this, "msg "+msg+"\n"+"id"+id, Toast.LENGTH_SHORT).show();
                    get_login_with_Id(id);
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(Social_Login.this, "status "+status+"\n"+"msg "+msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Response_register> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(Social_Login.this, "Error : "+t.toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void get_login_with_Id(String id) {

        HashMap<String,String>map=new HashMap<>();
        map.put("id", id);
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
                        startActivity(new Intent(Social_Login.this, Home.class));
                        finish();
                    }
                }else{
                    Toast.makeText(Social_Login.this, "msg "+msg+"\n"+"status "+status, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Response_Login> call, Throwable t) {
                Toast.makeText(Social_Login.this, "Error "+t.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void Google_login() {
        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // [END build_client]

        // [START customize_button]
        // Set the dimensions of the sign-in button.
   //    SignInButton signInButton = findViewById(R.id.sign_in_button);
  //      signInButton.setTooltipText("Google");
   //     signInButton.setSize(SignInButton.SIZE_STANDARD);
   //     signInButton.setColorScheme(SignInButton.COLOR_LIGHT);
    }

    private void FAceBook_Login() {
        fb_login_button = (LoginButton) findViewById(R.id.login_button);
        fb_login_button.setLoginText("FaceBook");
        mcallbackManager = CallbackManager.Factory.create();
        fb_login_button.setReadPermissions("public_profile","email","user_friends");
        mStatusTextView = findViewById(R.id.status);

        // Button listeners
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        findViewById(R.id.disconnect_button).setOnClickListener(this);

        try {
            PackageInfo info = getPackageManager().getPackageInfo("aaronsoftech.in.unber", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String sign = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.e("MY KEY HASH:", sign);
                //        Toast.makeText(getApplicationContext(), sign, Toast.LENGTH_LONG).show();
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
        fb_login_button.registerCallback(mcallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                get_LOCATION();
                Toast.makeText(Social_Login.this, "facebook successfully", Toast.LENGTH_LONG).show();
                accessToken = loginResult.getAccessToken();
                socialId=accessToken.getUserId();

                GraphRequest req=GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        //    Toast.makeText(getApplicationContext(),"graph request completed",Toast.LENGTH_SHORT).show();
                        try{try{
                            fb_email =  object.getString("email");
                        }catch (Exception e){e.printStackTrace();}
                            try{
                                fb_name = object.getString("name");
                            }catch (Exception e){e.printStackTrace();}
                            try{ socialId = object.getString("id");}catch (Exception e){e.printStackTrace();}

                            photourl =object.getJSONObject("picture").getJSONObject("data").getString("url");
                            Log.i(TAG,"Social login fb_email"+fb_email);
                            Log.i(TAG,"Social login fb_name"+fb_name);
                            Log.i(TAG,"Social login socialId"+socialId);
                            Log.i(TAG,"Social login photourl"+photourl);

                            HashMap<String,String>login_map=new HashMap<>();

                            login_map.put("token_id",""+socialId);

                            HashMap<String,String>register_map=new HashMap<>();
                            register_map.put("id_cms_privileges","4");
                            register_map.put("contact_number","");
                            register_map.put("lat",""+Lat);
                            register_map.put("lng",""+Longt);
                            register_map.put("mac_id","0");
                            register_map.put("social_type","facebook");
                            register_map.put("token_id",""+socialId);
                            register_map.put("name",""+fb_name);
                            register_map.put("email",""+fb_email);

                            Api_Social_login(login_map,register_map);

                        }catch (JSONException e)
                        {
                            Toast.makeText(getApplicationContext(),"graph request error : "+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,picture.type(large)");
                req.setParameters(parameters);
                req.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(Social_Login.this, "error  "+error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void get_LOCATION() {
        try {
            EasyLocationMod easyLocationMod = new EasyLocationMod(Social_Login.this);
            if (ActivityCompat.checkSelfPermission(Social_Login.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Social_Login.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            double[] l = easyLocationMod.getLatLong();
            Lat = String.valueOf(l[0]);
            Longt = String.valueOf(l[1]);
        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void onStart() {
        super.onStart();

        // [START on_start_sign_in]
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
        // [END on_start_sign_in]
    }

    // [START onActivityResult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    // [END onActivityResult]

    // [START handleSignInResult]
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }
    // [END handleSignInResult]

    // [START signIn]
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signIn]

    // [START signOut]
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        updateUI(null);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END signOut]

    // [START revokeAccess]
    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        updateUI(null);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END revokeAccess]

    private void updateUI(@Nullable GoogleSignInAccount account) {
        if (account != null) {
            mStatusTextView.setText(getString(R.string.signed_in_fmt, account.getDisplayName()));
            get_LOCATION();
            HashMap<String,String>login_map=new HashMap<>();
            String tokenid=account.getId().toString().trim();
            String name=account.getDisplayName().toString().trim();
            String email=account.getEmail().toString().trim();
            login_map.put("token_id",""+tokenid);

            HashMap<String,String>register_map=new HashMap<>();
            register_map.put("id_cms_privileges","4");
            register_map.put("contact_number","");
            register_map.put("lat",""+Lat);
            register_map.put("lng",""+Longt);
            register_map.put("mac_id","0");
            register_map.put("social_type","google");
            register_map.put("token_id",""+tokenid);
            register_map.put("name",""+name);
            register_map.put("email",""+email);

            Api_Social_login(login_map,register_map);

            Toast.makeText(this, "tokenid "+tokenid, Toast.LENGTH_SHORT).show();
        } else {
      //      mStatusTextView.setText(R.string.signed_out);

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }

    private void Api_Social_login(HashMap<String, String> login_map, final HashMap<String, String> register_map) {
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        Call<Response_Login> call= APIClient.getWebServiceMethod().getSocial_Login(login_map);
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
                        startActivity(new Intent(Social_Login.this, Home.class));
                        finish();
                    }
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(Social_Login.this, "status "+status+"\n"+"msg "+msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Response_Login> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(Social_Login.this, "Error "+t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.sign_out_button:
                signOut();
                break;
            case R.id.disconnect_button:
                revokeAccess();
                break;
        }
    }


}
