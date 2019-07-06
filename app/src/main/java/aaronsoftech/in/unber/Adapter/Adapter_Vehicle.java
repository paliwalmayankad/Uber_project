package aaronsoftech.in.unber.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import aaronsoftech.in.unber.POJO.Response_All_Vehicle;
import aaronsoftech.in.unber.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Chouhan on 07/02/2019.
 */

public class Adapter_Vehicle extends RecyclerView.Adapter<Adapter_Vehicle.MyViewHolder>{

    Context con;
    List<Response_All_Vehicle.Data_Vehicle_List> get_list=new ArrayList<>();
    Vehicle_Item_listner click_adapter_item_listner;

    public Adapter_Vehicle(Context con, List<Response_All_Vehicle.Data_Vehicle_List> get_list,Adapter_Vehicle.Vehicle_Item_listner click_adapter_item_listner) {
        this.con = con;
        this.get_list = get_list;
        this.click_adapter_item_listner=click_adapter_item_listner;

    }

    @Override
    public Adapter_Vehicle.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_vehicle,parent,false);
        return new Adapter_Vehicle.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Adapter_Vehicle.MyViewHolder holder, final int position) {
    try{
        DecimalFormat df2=new DecimalFormat("#.##");
        if (get_list.get(position).getVehicle_price()!=null)
        {
            double price= Double.parseDouble(get_list.get(position).getVehicle_price());
            holder.txt_price.setText(String.valueOf(df2.format(price)));

        }else{
            holder.txt_price.setText("1");

        }


        holder.txt_no.setText("No :"+String.valueOf(get_list.get(position).getVehicle_number().toString()));
        String img_url=String.valueOf(get_list.get(position).getVehicle_photo().toString());
        Picasso.with(con).load(img_url).fit().centerCrop()
                .placeholder(R.drawable.ic_user)
                .error(R.drawable.ic_user)
                .into(holder.img);
        holder.llayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click_adapter_item_listner.OnClick_item(get_list.get(position));
            }
        });
    }catch (Exception e){e.printStackTrace();}

    }

    @Override
    public int getItemCount() {
        return get_list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView img;
        LinearLayout llayout;
        TextView txt_no,txt_price;
        public MyViewHolder(View itemView) {
            super(itemView);
            llayout=itemView.findViewById(R.id.layout);
            img=itemView.findViewById(R.id.vehicle_type_img);
            txt_no=itemView.findViewById(R.id.txt_veh_no);
            txt_price=itemView.findViewById(R.id.txt_price);
        }
    }

    public interface Vehicle_Item_listner {

        void OnClick_item(Response_All_Vehicle.Data_Vehicle_List vehicle_type);
    }


}

