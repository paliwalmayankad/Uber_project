package aaronsoftech.in.nber.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import aaronsoftech.in.nber.POJO.Response_All_Vehicle;
import aaronsoftech.in.nber.POJO.Response_Driver_vehicle;
import aaronsoftech.in.nber.R;

public class Adapter_Driver_vehicle extends RecyclerView.Adapter<Adapter_Driver_vehicle.MyViewHolder> {

    Context con;
    List<Response_Driver_vehicle.Vehicle_Info> get_list = new ArrayList<>();
    Vehicle_Item_listner click_adapter_item_listner;

    public Adapter_Driver_vehicle(Context con, List<Response_Driver_vehicle.Vehicle_Info> get_list, Adapter_Driver_vehicle.Vehicle_Item_listner click_adapter_item_listner) {
        this.con = con;
        this.get_list = get_list;
        this.click_adapter_item_listner = click_adapter_item_listner;

    }

    @Override
    public Adapter_Driver_vehicle.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_driver_vehicle, parent, false);
        return new Adapter_Driver_vehicle.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Adapter_Driver_vehicle.MyViewHolder holder, final int position) {
        try {

            holder.txt_no.setText("No :" + String.valueOf(get_list.get(position).getVehicle_number().toString()));
            holder.txt_price.setText(get_list.get(position).getStatus());
            String img_url = String.valueOf(get_list.get(position).getVehicle_photo().toString());
            Picasso.with(con).load(img_url).fit().centerCrop()
                    .placeholder(R.drawable.ic_user)
                    .error(R.drawable.ic_user)
                    .into(holder.img);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return get_list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        LinearLayout llayout;
        TextView txt_no, txt_price;

        public MyViewHolder(View itemView) {
            super(itemView);
            llayout = itemView.findViewById(R.id.layout);
            img = itemView.findViewById(R.id.vehicle_type_img);
            txt_no = itemView.findViewById(R.id.txt_veh_no);
            txt_price = itemView.findViewById(R.id.txt_price);
        }
    }

    public interface Vehicle_Item_listner {

        void OnClick_item(Response_All_Vehicle.Data_Vehicle_List vehicle_type);
    }


}


