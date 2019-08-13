package aaronsoftech.in.nber.Adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import aaronsoftech.in.nber.POJO.Response_Vehicle_type;
import aaronsoftech.in.nber.R;

public class Adapter_Vehicle_gallery extends BaseAdapter {
    LayoutInflater inflater;
    Context con;
    List<Response_Vehicle_type.Data_List> get_list=new ArrayList<>();


    public Adapter_Vehicle_gallery(Context con, List<Response_Vehicle_type.Data_List> get_list) {
        this.con = con;
        this.get_list = get_list;
        inflater = (LayoutInflater.from(con));

    }

    @Override
    public int getCount() {
        return get_list.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View v, ViewGroup viewGroup) {

        v=inflater.inflate(R.layout.layout_vehicle_type,null);
        try{
            LinearLayout llayout=v.findViewById(R.id.layout);
            ImageView img=v.findViewById(R.id.vehicle_type_img);
            TextView txt_name=v.findViewById(R.id.txt_veh_name);
            TextView txt_seating=v.findViewById(R.id.txt_seating);
            TextView txt_price=v.findViewById(R.id.txt_km);
            txt_name.setText(get_list.get(position).getVehicle_type());
            if (get_list.get(position).getSeating_capacity()==null)
            {
                txt_seating.setText("1");

            }else{
                txt_seating.setText(get_list.get(position).getSeating_capacity());
            }
            txt_price.setText("\u20B9"+get_list.get(position).getKm_price());
            String img_url=get_list.get(position).getVehicle_icon();
            Picasso.with(con).load(img_url).fit()
                    .placeholder(R.drawable.ic_user)
                    .error(R.drawable.ic_user)
                    .into(img);

        }catch (Exception e){e.printStackTrace();}

        return v;
    }

}
