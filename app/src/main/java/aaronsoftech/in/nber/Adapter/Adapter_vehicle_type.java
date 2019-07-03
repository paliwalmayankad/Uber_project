package aaronsoftech.in.nber.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import aaronsoftech.in.nber.Activity.Home;
import aaronsoftech.in.nber.POJO.Response_Vehicle_type;
import aaronsoftech.in.nber.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Chouhan on 07/02/2019.
 */

public class Adapter_vehicle_type extends RecyclerView.Adapter<Adapter_vehicle_type.MyViewHolder>{

    Context con;
    List<Response_Vehicle_type.Data_List> get_list=new ArrayList<>();
    Click_Adapter_Item_listner click_adapter_item_listner;

    public Adapter_vehicle_type(Context con, List<Response_Vehicle_type.Data_List> get_list,Click_Adapter_Item_listner click_adapter_item_listner) {
        this.con = con;
        this.get_list = get_list;
        this.click_adapter_item_listner=click_adapter_item_listner;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_vehicle_type,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.txt_name.setText(get_list.get(position).getVehicle_type());
        holder.txt_seating.setText("Seating :"+get_list.get(position).getSeating_capacity());
        String img_url=get_list.get(position).getVehicle_icon();
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
    }

    @Override
    public int getItemCount() {
        return get_list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView img;
        LinearLayout llayout;
        TextView txt_name,txt_seating;
        public MyViewHolder(View itemView) {
            super(itemView);
            llayout=itemView.findViewById(R.id.layout);
            img=itemView.findViewById(R.id.vehicle_type_img);
            txt_name=itemView.findViewById(R.id.txt_veh_name);
            txt_seating=itemView.findViewById(R.id.txt_seating);
        }
    }

    public interface Click_Adapter_Item_listner {

        void OnClick_item(Response_Vehicle_type.Data_List getData);
    }


}
