package aaronsoftech.in.nber.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import aaronsoftech.in.nber.R;

public class Payment_add extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_add);
        TextView btn_add_code=(TextView)findViewById(R.id.add_code);
        btn_add_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Payment_add.this,Payment_code.class));
            }
        });
    }
}
