package aaronsoftech.in.nber.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import aaronsoftech.in.nber.POJO.Notification;
import aaronsoftech.in.nber.POJO.Response_Booking_List;
import aaronsoftech.in.nber.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_notification  extends RecyclerView.Adapter<Adapter_notification.MyViewHolder> {
    public static final List<Long> times = Arrays.asList(
            TimeUnit.DAYS.toMillis(365),
            TimeUnit.DAYS.toMillis(30),
            TimeUnit.DAYS.toMillis(1),
            TimeUnit.HOURS.toMillis(1),
            TimeUnit.MINUTES.toMillis(1),
            TimeUnit.SECONDS.toMillis(1));
    static String languagenoti;
    public static final List<String> timesString = Arrays.asList("year", "month", "day", "hour", "minute", "second");
    String timetotal = null;
    String timetotalday;
    Context con;
    List<Notification.send_data> get_list = new ArrayList<>();

    public Adapter_notification(Context con, List<Notification.send_data> get_list) {
        this.con = con;
        this.get_list = get_list;

    }

    @Override
    public Adapter_notification.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_notification, parent, false);
        return new Adapter_notification.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Adapter_notification.MyViewHolder holder, final int position) {
        try {

            long miliSecsDate = milliseconds(""+get_list.get(position).getTimestamp().toString());

            timeAgo(miliSecsDate,timetotalday);

            holder.txt_date.setText(timetotalday);

            holder.txt_message.setText(get_list.get(position).getNotification_text());
            String veh_type_id=get_list.get(position).getSent_by();
            if (veh_type_id.toString().equalsIgnoreCase("8"))
            {
                holder.img.setBackgroundResource(R.drawable.auto_icon);
            }else if (veh_type_id.toString().equalsIgnoreCase("7"))
            {
                holder.img.setBackgroundResource(R.drawable.bike);
            }else if (veh_type_id.toString().equalsIgnoreCase("6"))
            {
                holder.img.setBackgroundResource(R.drawable.ok_car_icon);
            }else if (veh_type_id.toString().equalsIgnoreCase("5"))
            {
                holder.img.setBackgroundResource(R.drawable.e_rickshaw);
            }else if (veh_type_id.toString().equalsIgnoreCase("4"))
            {
                holder.img.setBackgroundResource(R.drawable.scooty);
            }
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
        TextView txt_message, txt_date;

        public MyViewHolder(View itemView) {
            super(itemView);
            txt_message = itemView.findViewById(R.id.txt_veh_msg);
            txt_date = itemView.findViewById(R.id.txt_date);
            img = itemView.findViewById(R.id.vehicle_type_img);

        }
    }

    public  void timeAgo(long miliSecsDate, String date)
    {
        try
        {
            /*current itme*/
            long currentMili = System.currentTimeMillis();
            long diff_in_ms=currentMili-miliSecsDate;

            /*diifeence in miliseconds*/
            timetotalday= toDuration(diff_in_ms);

            date=timetotalday;
        }
        catch (Exception e)
        {
            Crashlytics.logException(e);
            e.printStackTrace();
        }
    }


    public static String toDuration(long diff_in_ms)
    {
        StringBuffer res = new StringBuffer();
        try
        {
            for(int i=0;i< times.size(); i++) {
                Long current = times.get(i);
                long temp = diff_in_ms/current;
                if(temp>0) {

                    res.append(temp).append(" ").append( timesString.get(i) ).append(temp != 1 ? "s" : "").append(" ago");

                    break;
                }}

            if("".equals(res.toString()))
                return "Just Now";
            else
                return res.toString();
        }
        catch (Exception e)
        {
            Crashlytics.logException(e);
            e.printStackTrace();
        }
        return res.toString();
    }

    public long milliseconds(String date)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        try
        {
            Date mDate = sdf.parse(date);
            long timeInMilliseconds = mDate.getTime();
            System.out.println("Date in milli :: " + timeInMilliseconds);
            return timeInMilliseconds;
        }
        catch (ParseException e)
        {
            Crashlytics.logException(e);
            e.printStackTrace();
        }

        return 0;
    }


}