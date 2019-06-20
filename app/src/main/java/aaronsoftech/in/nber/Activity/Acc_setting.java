package aaronsoftech.in.nber.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import aaronsoftech.in.nber.App_Conteroller;
import aaronsoftech.in.nber.R;

public class Acc_setting extends AppCompatActivity {
    private GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acc_setting);

        TextView logout_btn=findViewById(R.id.btn_signout);

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
                startActivity(new Intent(Acc_setting.this,Social_Login.class).
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
}
