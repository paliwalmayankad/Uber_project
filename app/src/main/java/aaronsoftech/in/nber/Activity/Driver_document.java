package aaronsoftech.in.nber.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import aaronsoftech.in.nber.R;

public class Driver_document extends AppCompatActivity {
    TextView btn_licence,btn_profile_photo,btn_pancard,btn_permit_a,
            btn_permit_b,btn_registertion,btn_insurence,btn_continue;
    ImageView btn_back;
    public static String path_licence="",path_profile="",path_pancard="",path_permit_a="",
            path_permit_b="",path_registration="",path_insurense="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_document);
        btn_licence=findViewById(R.id.txt_licence);
        btn_profile_photo=findViewById(R.id.txt_photo);
        btn_pancard=findViewById(R.id.txt_pancard);
        btn_permit_a=findViewById(R.id.txt_permit_a);
        btn_permit_b=findViewById(R.id.txt_permit_b);
        btn_registertion=findViewById(R.id.txt_register_certificate);
        btn_insurence=findViewById(R.id.txt_insurence);
        btn_continue=findViewById(R.id.txt_continue);
        btn_back=findViewById(R.id.btn_ic_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btn_profile_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Driver_document.this,Driver_photo.class)
                        .putExtra("type","2"));
            }
        });
        btn_licence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Driver_document.this,Driver_doc_Image.class)
                        .putExtra("type","1"));
            }
        });
        btn_pancard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Driver_document.this,Driver_doc_Image.class)
                        .putExtra("type","3"));
            }
        });
        btn_permit_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Driver_document.this,Driver_doc_Image.class)
                        .putExtra("type","4"));
            }
        });
        btn_permit_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Driver_document.this,Driver_doc_Image.class)
                        .putExtra("type","6"));
            }
        });
        btn_registertion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Driver_document.this,Driver_doc_Image.class)
                        .putExtra("type","5"));
            }
        });
        btn_insurence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Driver_document.this,Driver_doc_Image.class)
                .putExtra("type","7"));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Check_path_image();
    }

    private void Check_path_image() {

        if (path_licence!=""){ btn_licence.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.done, 0); }
        if (path_profile!=""){ btn_profile_photo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.done, 0); }
        if (path_pancard!=""){ btn_pancard.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.done, 0); }
        if (path_permit_a!=""){ btn_permit_a.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.done, 0); }
        if (path_permit_b!=""){ btn_permit_b.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.done, 0); }
        if (path_registration!=""){ btn_registertion.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.done, 0); }
        if (path_insurense!=""){ btn_insurence.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.done, 0); }


    }
}
