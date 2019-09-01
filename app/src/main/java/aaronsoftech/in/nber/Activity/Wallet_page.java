package aaronsoftech.in.nber.Activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

        recyclerView = (RecyclerView)findViewById(R.id.recycle_wallet);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager); // set LayoutManager to RecyclerView

        wallet_get_data();
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

                           txt_total.setText(String.valueOf(count));

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
