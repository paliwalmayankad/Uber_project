package aaronsoftech.in.nber.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.squareup.picasso.Picasso;

import aaronsoftech.in.nber.App_Conteroller;
import aaronsoftech.in.nber.R;
import aaronsoftech.in.nber.Utils.SP_Utils;
import de.hdodenhof.circleimageview.CircleImageView;

public class Acc_setting extends AppCompatActivity {
    private GoogleSignInClient mGoogleSignInClient;
    TextView t_name,t_mobile,t_email;
    CircleImageView img_profile;
    TextView txt_place,txt_home,txt_work,btn_tc,btn_privicy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acc_setting);

        TextView logout_btn=findViewById(R.id.btn_signout);

        Init();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGoogleSignInClient.signOut();
                App_Conteroller.sharedpreferences = getSharedPreferences(App_Conteroller.MyPREFERENCES, Context.MODE_PRIVATE);
                App_Conteroller.editor = App_Conteroller.sharedpreferences.edit();
                App_Conteroller.editor.clear();
                App_Conteroller.editor.commit();
                startActivity(new Intent(Acc_setting.this,login_mobile.class).
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        });

        ImageView btn_back=(ImageView)findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void Init() {
        btn_tc=findViewById(R.id.txt_tc);
        btn_privicy=findViewById(R.id.txt_privacy);
        img_profile=findViewById(R.id.profile);
        t_name=findViewById(R.id.txt_name);
        t_mobile=findViewById(R.id.txt_mobile);
        t_email=findViewById(R.id.txt_emailid);
        t_mobile.setText(App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_CONTACT_NUMBER,""));
        t_name.setText(App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_NAME,""));
        t_email.setText(App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_EMAIL,""));
        try{
            String photo= App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_PHOTO,"");
            Picasso.with(Acc_setting.this).load(photo)
                    .placeholder(R.drawable.ic_user)
                    .error(R.drawable.ic_user)
                    .into(img_profile);
        }catch (Exception e){e.printStackTrace();}


        TextView btn_place=findViewById(R.id.txt_add_place);
        TextView btn_home=findViewById(R.id.txt_add_home);
        TextView btn_work=findViewById(R.id.txt_add_work);

         txt_place=findViewById(R.id.txt_add_place2);
         txt_home=findViewById(R.id.txt_add_home2);
         txt_work=findViewById(R.id.txt_add_work2);

        btn_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Acc_setting.this,MapsActivity.class).putExtra("type","3"));
            }
        });
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Acc_setting.this,MapsActivity.class).putExtra("type","1"));
            }
        });
        btn_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Acc_setting.this,MapsActivity.class).putExtra("type","2"));
            }
        });


        btn_privicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent ii=new Intent(Acc_setting.this,Privacy_setting.class);
            }
        });
        btn_tc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii=new Intent(Acc_setting.this,Privacy_setting.class);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        set_data_address();
    }

    private void set_data_address() {
        txt_home.setText(App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_USR_HOME_ADDRESS,""));
        txt_place.setText(App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_USR_PLACE_ADDRESS,""));
        txt_work.setText(App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_USR_WORK_ADDRESS,""));
    }
}
