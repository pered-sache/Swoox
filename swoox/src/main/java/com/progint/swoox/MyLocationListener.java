package com.progint.swoox;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;


/**
 * Created by Eduardo on 10/03/14.
 */
public class MyLocationListener implements LocationListener {
    MainActivity mainActivity;


    public MainActivity getMainActivity() {

        return mainActivity;
    }

    public void setMainActivity(MainActivity mainActivity){
        this.mainActivity=mainActivity;
    }

    @Override
    public void onLocationChanged(Location loc) {
        loc.getLatitude();
        loc.getAltitude();
        //String Text="Mi ubicacion actual es :"+ "\n Lat="+loc.getLatitude()+"\n Long = "+loc.getLongitude();
        this.mainActivity.setLocation(loc);

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        //aqui muestra cuando hay un cambio del proveedor los status son:
        //OUT_OF_SERVICE
        //TEMPORARILY_UNAVAILABLE
        //AVAILABLE

    }

    @Override
    public void onProviderEnabled(String s) {


    }

    @Override
    public void onProviderDisabled(String s) {
        //messageTextView.setText("GPS Desactivado");
    }



}
