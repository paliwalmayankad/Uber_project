package aaronsoftech.in.unber.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import aaronsoftech.in.unber.Adapter.PlaceAutocompleteAdapter;
import aaronsoftech.in.unber.R;

public class Contacts extends AppCompatActivity {
    private AutoCompleteTextView locationSearchActv;// instance of AutoCompleteText View
    private TextView addressTv, locationDataTv; // TextViews Used to display the adddress selected by the user

    private GeoDataClient mGeoDataClient;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;

    private static final LatLngBounds LAT_LNG_BOUNDS_DEFAULT = new LatLngBounds(
            new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362)); // The suggestion within this geometrical boundary will be displayed on top.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        locationSearchActv = (AutoCompleteTextView)findViewById(R.id.location_actv);
        addressTv = (TextView) findViewById(R.id.address_tv);
        locationDataTv = (TextView)findViewById(R.id.location_display_tv);
        mGeoDataClient = Places.getGeoDataClient(this);
        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this,mGeoDataClient, LAT_LNG_BOUNDS_DEFAULT,null);
        locationSearchActv.setAdapter(mPlaceAutocompleteAdapter);
        locationSearchActv.setOnItemClickListener(mAutocompleteClickListener);


    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
            final AutocompletePrediction item = mPlaceAutocompleteAdapter.getItem(position);
            final String placeId = item.getPlaceId();


            /*
             Issue a request to the Places Geo Data Client to retrieve a Place object with
             additional details about the place.
              */
            Task<PlaceBufferResponse> placeResult = mGeoDataClient.getPlaceById(placeId);
            placeResult.addOnCompleteListener(mUpdatePlaceDetailsCallback);
        }
    };

    private OnCompleteListener<PlaceBufferResponse> mUpdatePlaceDetailsCallback
            = new OnCompleteListener<PlaceBufferResponse>() {
        @Override
        public void onComplete(Task<PlaceBufferResponse> task) {
            try {
                PlaceBufferResponse places = task.getResult();

                // Get the Place object from the buffer.
                final Place place = places.get(0);
                addressTv.setText(place.getAddress().toString());
                locationDataTv.setText("Latitude : "+String.valueOf(place.getLatLng().latitude)+"\n Longitude : "+String.valueOf(place.getLatLng().longitude));
                places.release();
            } catch (RuntimeRemoteException e) {
                // Request did not complete successfully
                return;
            }
        }
    };


}
