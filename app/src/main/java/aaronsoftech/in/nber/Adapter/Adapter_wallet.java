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
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import aaronsoftech.in.nber.POJO.Response_Vehicle_type;
import aaronsoftech.in.nber.POJO.Wallet;
import aaronsoftech.in.nber.R;

public class Adapter_wallet extends RecyclerView.Adapter<Adapter_wallet.MyViewHolder> {
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
    List<Wallet.Wallet_List> get_list = new ArrayList<>();

    public Adapter_wallet(Context con, List<Wallet.Wallet_List> get_list) {
        this.con = con;
        this.get_list = get_list;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_wallet, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.txt_price.setText(get_list.get(position).getAmount());

        long miliSecsDate = milliseconds(""+get_list.get(position).getTimestamp().toString());

        String time= timeAgo(miliSecsDate,timetotalday);

        holder.txt_date.setText(time);
        holder.txt_status.setText(get_list.get(position).getStatus());
        holder.txt_msg.setText(get_list.get(position).getRemark());

    }

    @Override
    public int getItemCount() {
        return get_list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        LinearLayout llayout;
        TextView txt_price, txt_date,txt_msg,txt_status;

        public MyViewHolder(View itemView) {
            super(itemView);
            llayout = itemView.findViewById(R.id.layout);
            img = itemView.findViewById(R.id.vehicle_type_img);
            txt_price = itemView.findViewById(R.id.txt_price);
            txt_date = itemView.findViewById(R.id.txt_date);
            txt_status = itemView.findViewById(R.id.txt_status);
            txt_msg = itemView.findViewById(R.id.txt_veh_msg);
        }
    }

    public String timeAgo(long miliSecsDate, String date)
    {
        String time=null;
        try
        {
            /*current itme*/
            long currentMili = System.currentTimeMillis();
            long diff_in_ms=currentMili-miliSecsDate;

            /*diifeence in miliseconds*/
            timetotalday= toDuration(diff_in_ms);
            time=timetotalday;
            return time;
        }
        catch (Exception e)
        {
            Crashlytics.logException(e);
            e.printStackTrace();
        }
        return time;
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
        // sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
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