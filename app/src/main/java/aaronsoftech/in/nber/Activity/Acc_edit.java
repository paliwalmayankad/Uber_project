package aaronsoftech.in.nber.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import aaronsoftech.in.nber.App_Conteroller;
import aaronsoftech.in.nber.R;
import aaronsoftech.in.nber.Utils.SP_Utils;

public class Acc_edit extends AppCompatActivity {
    TextView t_name,t_mobile,t_email;
    EditText ed_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acc_edit);

        t_name=findViewById(R.id.txt_name);
        t_mobile=findViewById(R.id.txt_mobile);
        t_email=findViewById(R.id.txt_emailid);
        ed_name=findViewById(R.id.e_name);

        ed_name.setText(App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_NAME,""));
        t_name.setText(App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_NAME,""));
        t_mobile.setText(App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_CONTACT_NUMBER,""));
        t_email.setText(App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_EMAIL,""));

    }
}
