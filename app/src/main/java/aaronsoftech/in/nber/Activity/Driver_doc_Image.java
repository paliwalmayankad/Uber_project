package aaronsoftech.in.nber.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import aaronsoftech.in.nber.R;

public class Driver_doc_Image extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_doc__image);

        String activity_type=getIntent().getExtras().getString("type","");
        TextView ed_message=findViewById(R.id.txt_message);
        TextView ed_title=findViewById(R.id.txt_title);
        if (activity_type.equalsIgnoreCase("1"))
        {
            ed_message.setText(getResources().getString(R.string.txt_message_license));
            ed_title.setText(getResources().getString(R.string.txt_title_license));
        }else if (activity_type.equalsIgnoreCase("3"))
        {
            ed_message.setText(getResources().getString(R.string.txt_message_pancard));
            ed_title.setText(getResources().getString(R.string.txt_title_pancard));
        }else if (activity_type.equalsIgnoreCase("4"))
        {
            ed_message.setText(getResources().getString(R.string.txt_message_permit_a));
            ed_title.setText(getResources().getString(R.string.txt_title_permit_a));
        }else if (activity_type.equalsIgnoreCase("5"))
        {
            ed_message.setText(getResources().getString(R.string.txt_message_register_cert));
            ed_title.setText(getResources().getString(R.string.txt_title_register_cert));
        }else if (activity_type.equalsIgnoreCase("6"))
        {
            ed_message.setText(getResources().getString(R.string.txt_message_permit_b));
            ed_title.setText(getResources().getString(R.string.txt_title_permit_b));
        }else if (activity_type.equalsIgnoreCase("7"))
        {
            ed_message.setText(getResources().getString(R.string.txt_message_insurense));
            ed_title.setText(getResources().getString(R.string.txt_title_insurense));
        }

        TextView btn_submit=findViewById(R.id.camera_id);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
