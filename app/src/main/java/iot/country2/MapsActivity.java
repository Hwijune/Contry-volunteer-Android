package iot.country2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.io.IOException;
import java.io.InputStream;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnInfoWindowClickListener,OnMapReadyCallback {
    private int currentApiVersion; //네비바 없애기
    private GoogleMap mMap;
    private  final int MY_PERMISSIONS_REQUEST_READ_CONTACTS=1;
    public int row=1,sheets=0;
    public static int sendrow=1,sendsheets=0;
    double Lat=0;
    double Lng=0;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

//네비게이션바 없애기
        currentApiVersion = android.os.Build.VERSION.SDK_INT;

        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        // This work only for android 4.4+
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {

            getWindow().getDecorView().setSystemUiVisibility(flags);

            // Code below is to handle presses of Volume up or Volume down.
            // Without this, after pressing volume buttons, the navigation bar will
            // show up and won't hide
            final View decorView = getWindow().getDecorView();
            decorView
                    .setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {

                        @Override
                        public void onSystemUiVisibilityChange(int visibility) {
                            if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                                decorView.setSystemUiVisibility(flags);
                            }
                        }
                    });
        }

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng Seoul = new LatLng(36.892652, 127.754585);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Seoul));

        CameraUpdate zoom = CameraUpdateFactory.zoomTo(9);
        mMap.animateCamera(zoom);


        // 현재 위치를 받을수 있는 코드
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        } else {
            mMap.setMyLocationEnabled(true);
        }

        marker();
        mMap.setOnInfoWindowClickListener(this);

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

        int i=0;
        Workbook workbook = null;
        Sheet sheet = null;
        try {

            for(sheets=0;sheets<10;sheets++) {
                InputStream inputStream = getBaseContext().getResources().getAssets().open("체험마을 데이터 총합.xls");
                workbook = Workbook.getWorkbook(inputStream);
                sheet = workbook.getSheet(sheets);

                for (row = 1; row < sheet.getRows(); row++) {


                    if(marker.getId().equals("m"+i))
                    {
                        sendrow = row;
                        sendsheets = sheets;
                    }
                    i++;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } finally {
            workbook.close();
        }

        Intent intent = new Intent(MapsActivity.this, detailActivity.class);
        startActivity(intent);

        // Toast.makeText(this,"Info row = "+sendrow+",sheet = "+ sendsheets,
        //       Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    finish();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    private double parseDouble(String s){
        if(s == null || s.isEmpty())
            return 0.0;
        else
            return Double.parseDouble(s);
    }
    public void marker() {
        Workbook workbook = null;
        Sheet sheet = null;
        try {

            for(sheets=0;sheets<10;sheets++) {
                InputStream inputStream = getBaseContext().getResources().getAssets().open("체험마을 데이터 총합.xls");
                workbook = Workbook.getWorkbook(inputStream);

                sheet = workbook.getSheet(sheets);

                for (row = 1; row < sheet.getRows(); row++) {
                    Lat = parseDouble(sheet.getCell(14, row).getContents());
                    Lng = parseDouble(sheet.getCell(15, row).getContents());
                    String aname = sheet.getCell(0, row).getContents();
                    String bname = sheet.getCell(4,row).getContents();

                    mMap.addMarker(new MarkerOptions().position(new LatLng(Lat, Lng)).title(aname).snippet(bname));//마크 찍기
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } finally {
            workbook.close();
        }
    }
}
