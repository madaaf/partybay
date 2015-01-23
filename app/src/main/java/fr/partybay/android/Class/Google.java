package fr.partybay.android.Class;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import fr.partybay.android.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;


/**
 * Created by mada on 22/01/15.
 */
public class Google extends Activity {
    /** Local variables **/
    GoogleMap googleMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.google);
        ActionBar bar = this.getActionBar();
        bar.hide();
        createMapView();
    }

    private void createMapView(){


        try {
            if(null == googleMap){
                googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                        R.id.mapView)).getMap();


                if(null == googleMap) {
                    Toast.makeText(getApplicationContext(),
                            "Error creating map", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (NullPointerException exception){
            Log.e("mapApp", exception.toString());
        }
    }
    }