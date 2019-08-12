package aaronsoftech.in.nber.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import aaronsoftech.in.nber.Adapter.Adapter_Driver_vehicle;
import aaronsoftech.in.nber.App_Conteroller;
import aaronsoftech.in.nber.POJO.Response_All_Vehicle;
import aaronsoftech.in.nber.POJO.Response_Driver_vehicle;
import aaronsoftech.in.nber.R;
import aaronsoftech.in.nber.Service.APIClient;
import aaronsoftech.in.nber.Utils.SP_Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static aaronsoftech.in.nber.Utils.App_Utils.isNetworkAvailable;

public class Show_Vehicle extends AppCompatActivity implements Adapter_Driver_vehicle.Vehicle_Item_listner{
    ProgressDialog progressDialog;
    RecyclerView recy_vehicle_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show__vehicle);

        ImageView btn_back1=findViewById(R.id.btn_back);
        btn_back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TextView btn_add_new_vehicle=findViewById(R.id.txt_add_new_vehicle);
        btn_add_new_vehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Show_Vehicle.this,Vehicle_reg.class));

            }
        });
        String Driver_ID= App_Conteroller.sharedpreferences.getString(SP_Utils.LOGIN_DRIVER_ID,"");
        recy_vehicle_list = (RecyclerView)findViewById(R.id.recycle_vehicle);
        StaggeredGridLayoutManager staggeredGridLayoutManager2 = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        recy_vehicle_list.setLayoutManager(staggeredGridLayoutManager2); // set LayoutManager to RecyclerView

        Call_Vihicle_Api(Driver_ID);

    }



    private void Call_Vihicle_Api(String driver_ID) {
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        HashMap map= new HashMap<>();
        map.put("driver_id",driver_ID);
        if (isNetworkAvailable(Show_Vehicle.this))
        {
            Call<Response_Driver_vehicle> call= APIClient.getWebServiceMethod().get_Driver_Vehicle(map);
            call.enqueue(new Callback<Response_Driver_vehicle>() {
                @Override
                public void onResponse(Call<Response_Driver_vehicle> call, Response<Response_Driver_vehicle> response) {
                    progressDialog.dismiss();
                    try{
                        String status=response.body().getApi_status();
                        String msg=response.body().getApi_message();
                        if (status.equalsIgnoreCase("1") && msg.equalsIgnoreCase("success") )
                        {
                            List< Response_Driver_vehicle.Vehicle_Info> getlist=new ArrayList<>();
                            for(int k=0;k<response.body().getData().size();k++)
                            {
                                if (response.body().getData().get(k).getStatus().equalsIgnoreCase("Active"))
                                {
                                    getlist.add(response.body().getData().get(k));
                                }
                            }

                            Adapter_Driver_vehicle adapter_past=new Adapter_Driver_vehicle(Show_Vehicle.this,getlist,Show_Vehicle.this);
                            recy_vehicle_list.setAdapter(adapter_past);

                        }else{

                            Toast.makeText(Show_Vehicle.this, "status "+status+"\n"+"msg "+msg, Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
    //                    Toast.makeText(Show_Vehicle.this, "Server error", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();}


                }

                @Override
                public void onFailure(Call<Response_Driver_vehicle> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(Show_Vehicle.this, "Error : "+t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(Show_Vehicle.this, "No Internet", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void OnClick_item(Response_All_Vehicle.Data_Vehicle_List vehicle_type) {


    }
}
