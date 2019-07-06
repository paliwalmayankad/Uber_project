package aaronsoftech.in.nber.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import aaronsoftech.in.nber.R;

public class login_mobile extends AppCompatActivity {
    EditText ed_mobile;
    String[] locationPermissionsl = {"android.permission.ACCESS_FINE_LOCATION","android.permission.ACCESS_COARSE_LOCATION"};
    private static int REQUEST_CODE_LOCATIONl = 102;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_mobile);
        final TextView social_login=findViewById(R.id.social_login);
        ed_mobile=findViewById(R.id.t_mobile);

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
                if (ed_mobile.getText().toString().isEmpty())
                {
                    ed_mobile.setError("Enter mobile no.");
                    ed_mobile.requestFocus();
                }else{
                    startActivity(new Intent(login_mobile.this,Verification.class)
                            .putExtra("mobile",ed_mobile.getText().toString().trim()));
                }

            }
        });
        Give_Permission();
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
                    ActivityCompat.requestPermissions(login_mobile.this, locationPermissionsl, REQUEST_CODE_LOCATIONl);
                }
            }
        };
        handler.postDelayed(runnable, 1000);
    }
}
