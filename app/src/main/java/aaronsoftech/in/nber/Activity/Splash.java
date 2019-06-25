package aaronsoftech.in.nber.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import aaronsoftech.in.nber.R;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


    }

    @Override
    protected void onResume() {
        Handler handler=new Handler();
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(Splash.this,login_mobile.class));
            }
        };
        handler.postDelayed(runnable,1000);

        super.onResume();
    }
}
