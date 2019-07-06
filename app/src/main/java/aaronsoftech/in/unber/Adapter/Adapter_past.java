package aaronsoftech.in.unber.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import aaronsoftech.in.unber.R;

/**
 * Created by Chouhan on 05/06/2019.
 */

public class Adapter_past extends RecyclerView.Adapter<Adapter_past.MyViewHolder> {
    Context con;
    String status;

    public Adapter_past(Context con, String status) {
        this.con = con;
        this.status = status;
    }

    @Override
    public Adapter_past.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_upcomming, parent, false);
        return new Adapter_past.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Adapter_past.MyViewHolder holder, final int position) {


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
