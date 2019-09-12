package aaronsoftech.in.nber.Activity;

import android.app.ProgressDialog;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.HashMap;

import aaronsoftech.in.nber.Adapter.Adapter_notification;
import aaronsoftech.in.nber.Adapter.Adapter_wallet;
import aaronsoftech.in.nber.App_Conteroller;
import aaronsoftech.in.nber.POJO.Wallet;
import aaronsoftech.in.nber.R;
import aaronsoftech.in.nber.Service.APIClient;
import aaronsoftech.in.nber.Utils.App_Utils;
import aaronsoftech.in.nber.Utils.SP_Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static aaronsoftech.in.nber.Utils.App_Utils.isNetworkAvailable;

public class Wallet_page extends AppCompatActivity {
    String TAG="Wallet_page";
    ProgressDialog progressDialog;
    static RecyclerView recyclerView;
    TextView txt_total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_page);
        ImageView btn_back=findViewById(R.id.btn_back);

        txt_total=findViewById(R.id.total_price);
        TextView txt_date=findViewById(R.id.txt_date);
        txt_date.setText(App_Utils.getCurrentdate());
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TextView btn_withdraw=findViewById(R.id.txt_withdraw);
        TextView btn_addmoney=findViewById(R.id.txt_add_money);
        btn_withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Show_driver_withdraw_box();
            }
        });
        recyclerView = (RecyclerView)findViewById(R.id.recycle_wallet);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager); // set LayoutManager to RecyclerView
        btn_addmoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_dialog_add_money();
            }
        });
        wallet_get_data();
    }

    private void show_dialog_add_money() {
            final BottomSheetDialog dialog = new BottomSheetDialog(Wallet_page.this);
            LayoutInflater inflater = this.getLayoutInflater();
            View v = inflater.inflate(R.layout.layout_add_payment, null);
             final TextInputEditText txt_no=v.findViewById(R.id.et_number);

            dialog.setContentView(v);
            Button btn_continue=v.findViewById(R.id.btn_cont);
            btn_continue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (txt_no.getText().toString().isEmpty()){
                        txt_no.setError("Enter amount");
                        txt_no.requestFocus();
                    }else{
                        dialog.dismiss();
                    }

                }
            });

            dialog.show();
    }

    public void Show_driver_withdraw_box(){

        final BottomSheetDialog dialog = new BottomSheetDialog(Wallet_page.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View v = inflater.inflate(R.layout.layout_payment_add, null);
        dialog.setContentView(v);
        Button btn_continue=v.findViewById(R.id.btn_cont);
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }



    private void wallet_get_data() {
        progressDialog=new ProgressDialog(Wallet_page.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        final HashMap<String,String> map=new HashMap<>();
        map.put("driver_id", App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,""));
        if (isNetworkAvailable(Wallet_page.this))
        {
            Call<Wallet> call= APIClient.getWebServiceMethod().wallet_get(map);
            call.enqueue(new Callback<Wallet>() {
                @Override
                public void onResponse(Call<Wallet> call, Response<Wallet> response) {
                    progressDialog.dismiss();
                    try{
                       if (response.body().getData()!=null)
                       {
                           double count=0;
                           for (int i=0;i<response.body().getData().size();i++)
                           {
                               double price=Double.parseDouble(response.body().getData().get(i).getAmount());
                               count=count+price;
                           }

                           DecimalFormat df2=new DecimalFormat("#.##");
                           txt_total.setText(String.valueOf(df2.format(count)));

                           Adapter_wallet aa=new Adapter_wallet(Wallet_page.this,response.body().getData());
                           recyclerView.setAdapter(aa);
                       }

                    }catch (Exception e){
                        progressDialog.dismiss();
                        Log.i(TAG,"Home || wallet_save || response "+response);
                        Log.i(TAG,"Home || wallet_save || send data "+map);
                        Toast.makeText(Wallet_page.this, "error wallet_save", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();}
                }

                @Override
                public void onFailure(Call<Wallet> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(Wallet_page.this, "Error : "+t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            progressDialog.dismiss();
            Toast.makeText(Wallet_page.this, "No Internet", Toast.LENGTH_SHORT).show();
        }

    }

}
