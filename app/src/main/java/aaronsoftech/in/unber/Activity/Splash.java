package aaronsoftech.in.unber.Activity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import aaronsoftech.in.unber.App_Conteroller;
import aaronsoftech.in.unber.R;
import aaronsoftech.in.unber.Utils.SP_Utils;

public class Splash extends AppCompatActivity {
    String userid="";
    String book_id="1";
    String refreshedToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        App_Conteroller.sharedpreferences = getSharedPreferences(App_Conteroller.MyPREFERENCES, Context.MODE_PRIVATE);
        App_Conteroller.editor = App_Conteroller.sharedpreferences.edit();
        refreshedToken = FirebaseInstanceId.getInstance().getToken();


    }

    @Override
    protected void onResume() {
        Handler handler=new Handler();
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                try{
                    userid= App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_ID,"");
                }catch (Exception e){e.printStackTrace();}
                if (userid.equalsIgnoreCase("") || userid.equalsIgnoreCase(null))
                {
                   startActivity(new Intent(Splash.this,login_mobile.class));
                    overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
                }else{
                    startActivity(new Intent(Splash.this,Home.class).putExtra("book_id",book_id));
                    overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
                }

            }
        };
        handler.postDelayed(runnable,1000);

        super.onResume();
    }
}
