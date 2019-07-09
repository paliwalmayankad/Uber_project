package aaronsoftech.in.unber.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

import aaronsoftech.in.unber.POJO.Customwindow_const;
import aaronsoftech.in.unber.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    public Context context;
    public CustomInfoWindowAdapter(Context ctx)
    {
        context=ctx;
    }
    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.custom_info_window, null);
        TextView et_title=(TextView)view.findViewById(R.id.titlewin);
        TextView et_snipt=(TextView)view.findViewById(R.id.snippet);
        try{
            Customwindow_const consteind = (Customwindow_const) marker.getTag();
            if (consteind.getSnippet()!=null)
            {
                et_snipt.setText(consteind.getSnippet());
            }if (consteind.getTitle()!=null)
            {
                et_title.setText(consteind.getTitle());
            }
        }catch (Exception e){e.printStackTrace();}



        return view;
    }
}
