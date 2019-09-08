package aaronsoftech.in.nber.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.concurrent.TimeUnit;

import aaronsoftech.in.nber.R;

import static aaronsoftech.in.nber.Utils.App_Utils.isNetworkAvailable;

public class login_mobile extends AppCompatActivity {
    EditText ed_mobile;
    String[] locationPermissionsl = {"android.permission.ACCESS_FINE_LOCATION","android.permission.ACCESS_COARSE_LOCATION"};
    private static int REQUEST_CODE_LOCATIONl = 102;
    String refreshedToken;
    String TAG="login_mobile";
    //These are the objects needed
    //It is the verification id that will be sent to the user
    public static String mVerificationId;
    public  static login_mobile activity_login_mobile;
    //firebase auth object
    ProgressDialog progressDialog;
    public static FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_mobile);
        activity_login_mobile=this;
        //initializing objects
        mAuth = FirebaseAuth.getInstance();

        final TextView social_login=findViewById(R.id.social_login);
        ed_mobile=findViewById(R.id.t_mobile);
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        ImageView btn_next=findViewById(R.id.next_button);
        social_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(login_mobile.this,Social_Login.class));
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call_Intent();
            }
        });
        Give_Permission();
    }

    private void Call_Intent() {
        if (isNetworkAvailable(login_mobile.this)) {
            refreshedToken = FirebaseInstanceId.getInstance().getToken();
            Log.i(TAG, "Token ID :  " + refreshedToken);

            /*if (ed_mobile.getText().toString().isEmpty()) {
                ed_mobile.setError("Enter mobile no.");
                ed_mobile.requestFocus();
            } else if (ed_mobile.getText().toString().length() != 10) {
                ed_mobile.setError("Invalid mobile no.");
                ed_mobile.requestFocus();
            } else {
                sendVerificationCode(ed_mobile.getText().toString().trim());*/
                startActivity(new Intent(login_mobile.this,Verification.class).putExtra("mobile",ed_mobile.getText().toString().trim()).putExtra("otp","no"));

        //    }
        }else{
            Toast.makeText(activity_login_mobile, "No internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendVerificationCode(String mobile) {
        progressDialog=new ProgressDialog(login_mobile.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
        //the callback to detect the verification status
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                progressDialog.dismiss();
                startActivity(new Intent(login_mobile.this,Verification.class).putExtra("mobile",ed_mobile.getText().toString().trim()).putExtra("otp",code));
            }

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            progressDialog.dismiss();
            Toast.makeText(login_mobile.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            progressDialog.dismiss();
            startActivity(new Intent(login_mobile.this,Verification.class).putExtra("mobile",ed_mobile.getText().toString().trim()).putExtra("otp","no"));
            mVerificationId = s;
        }
    };

        private void Give_Permission() {
            Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {

                    if (ActivityCompat.checkSelfPermission(getApplication(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplication(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(login_mobile.this, locationPermissionsl, REQUEST_CODE_LOCATIONl);
                    }
                }
            };
            handler.postDelayed(runnable, 1000);
        }


    }

