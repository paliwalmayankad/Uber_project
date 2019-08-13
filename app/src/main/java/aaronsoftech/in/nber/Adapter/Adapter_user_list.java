package aaronsoftech.in.nber.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import aaronsoftech.in.nber.Activity.From_Location;
import aaronsoftech.in.nber.POJO.Response_Booking_List;
import aaronsoftech.in.nber.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_user_list extends RecyclerView.Adapter<Adapter_user_list.MyViewHolder> {

    Context con;
    List<Response_Booking_List.User_List> get_list = new ArrayList<>();
    Adapter_user_list.Vehicle_Item_listner click_adapter_item_listner;

    public Adapter_user_list(Context con, List<Response_Booking_List.User_List> get_list, Adapter_user_list.Vehicle_Item_listner click_adapter_item_listner) {
        this.con = con;
        this.get_list = get_list;
        this.click_adapter_item_listner = click_adapter_item_listner;
    }

    @Override
    public Adapter_user_list.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_user_info, parent, false);
        return new Adapter_user_list.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Adapter_user_list.MyViewHolder holder, final int position) {
        try {
            holder.txt_contact.setText("Contact :"+get_list.get(position).getUcontact());
            DecimalFormat df2=new DecimalFormat("#.##");
            if (get_list.get(position).getAmount()!=null)
            {
                holder.txt_price.setText(df2.format(Double.valueOf(get_list.get(position).getAmount())));
            }else{
                holder.txt_price.setText("0.00");
            }


            holder.txt_add_from.setText("From :"+get_list.get(position).getFrom_address());
            holder.txt_add_to.setText("To :"+get_list.get(position).getTo_address());
            holder.txt_no.setText("Name :"+String.valueOf(get_list.get(position).getUname()));
            String img_url = String.valueOf(get_list.get(position).getUimage().toString());
            Picasso.with(con).load(img_url).fit().centerCrop()
                    .placeholder(R.drawable.ic_user)
                    .error(R.drawable.ic_user)
                    .into(holder.img);

            holder.llayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Response_Booking_List.User_List get_item=get_list.get(position);
                    Show_Dialog_details(get_item,con);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void Show_Dialog_details(Response_Booking_List.User_List get_list, Context mcon) {
       try{
         //  Dialog dialog = new Dialog((Activity)mcon);
           final BottomSheetDialog dialog = new BottomSheetDialog((Activity)mcon);
           LayoutInflater inflater = ((Activity) mcon).getLayoutInflater();
           View v = inflater.inflate(R.layout.layout_trip_details, null);

           dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_bottom;
           dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
           ((Activity)con).getWindow().setBackgroundDrawableResource(android.R.color.transparent);

           CircleImageView cust_img=v.findViewById(R.id.cust_img);
           TextView txt_amount=v.findViewById(R.id.txt_amount);
           TextView txt_book_type=v.findViewById(R.id.txt_book_type);
           TextView txt_from=v.findViewById(R.id.txt_from_add);
           TextView txt_to=v.findViewById(R.id.txt_to_add);
           TextView txt_date=v.findViewById(R.id.txt_date);
           TextView txt_name=v.findViewById(R.id.txt_name);
           txt_name.setText(get_list.getUname());
           txt_from.setText(get_list.getFrom_address());
           txt_to.setText(get_list.getTo_address());
           txt_date.setText(get_list.getBooked_date_time());


           DecimalFormat df2 = new DecimalFormat("#.##");
           if (get_list.getAmount() != null) {
               txt_amount.setText(df2.format(Double.valueOf(get_list.getAmount())));
           } else {
               txt_amount.setText("0.00");
           }

           txt_book_type.setText(get_list.getPickup());
           Picasso.with(mcon).load(get_list.getUimage()).into(cust_img);
           dialog.setContentView(v);

           dialog.show();
       }catch (Exception e){e.printStackTrace();}

    }

    @Override
    public int getItemCount() {
        return get_list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView img;
        LinearLayout llayout;
        TextView txt_no, txt_price,img_icon,txt_contact,txt_add_from,txt_add_to;

        public MyViewHolder(View itemView) {
            super(itemView);
            txt_add_from=itemView.findViewById(R.id.txt_from);
            txt_add_to=itemView.findViewById(R.id.txt_to);
            txt_contact=itemView.findViewById(R.id.txt_contact);
            img_icon=itemView.findViewById(R.id.price);
            llayout = itemView.findViewById(R.id.layout);
            img = itemView.findViewById(R.id.vehicle_type_img);
            txt_no = itemView.findViewById(R.id.txt_veh_no);
            txt_price = itemView.findViewById(R.id.txt_price);
        }
    }

    public interface Vehicle_Item_listner {

        void OnClick_item(Response_Booking_List.User_List user_list);
    }
}
