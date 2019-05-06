package aaronsoftech.in.uber.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import aaronsoftech.in.uber.R;


/**
 * Created by Chouhan on 05/06/2019.
 */

public class Adapter_upcomming extends RecyclerView.Adapter<Adapter_upcomming.MyViewHolder> {
    Context con;
    String status;

    public Adapter_upcomming(Context con, String status) {
        this.con = con;
        this.status = status;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_upcomming, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


    }

    @Override
    public int getItemCount() {
        return 10;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(View itemView) {
            super(itemView);

        }
    }
}