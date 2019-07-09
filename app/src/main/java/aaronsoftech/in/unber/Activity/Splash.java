package aaronsoftech.in.unber.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import aaronsoftech.in.unber.App_Conteroller;
import aaronsoftech.in.unber.R;
import aaronsoftech.in.unber.Utils.SP_Utils;

public class Splash extends AppCompatActivity {
    String userid="";
    String book_id="1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        App_Conteroller.sharedpreferences = getSharedPreferences(App_Conteroller.MyPREFERENCES, Context.MODE_PRIVATE);
        App_Conteroller.editor = App_Conteroller.sharedpreferences.edit();

        try{
            book_id=getIntent().getExtras().getString("book_id","");
            Toast.makeText(this, "book_id: "+book_id, Toast.LENGTH_SHORT).show();

        }catch (Exception e){e.printStackTrace();}

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
                }else{
                    startActivity(new Intent(Splash.this,Home.class).putExtra("book_id",book_id));
                }

            }
        };
        handler.postDelayed(runnable,1000);

        super.onResume();
    }
}
