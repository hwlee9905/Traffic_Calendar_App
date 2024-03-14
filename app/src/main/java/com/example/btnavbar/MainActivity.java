package com.example.btnavbar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    public static Context mContext;
    // 바텀 네비게이션
    BottomNavigationView bottomNavigationView;

    private String TAG = "메인";
    String temp;

    Fragment fragment_weater;
    Fragment fragment_calendar;
    Fragment fragment_option;

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    static double latitude=0;
    static double longitude=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        // 프래그먼트 생성
        fragment_weater = new FragWeather();
        fragment_calendar = new FragCal();
        fragment_option = new FragOption();



        // 바텀 네비게이션
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // 초기 플래그먼트 설정
        getSupportFragmentManager().beginTransaction().replace(R.id.main_layout, fragment_weater).commitAllowingStateLoss();


        // 바텀 네비게이션
        bottomNavigationView = findViewById(R.id.bottomNavigationView);


        // 리스너 등록
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.i(TAG, "바텀 네비게이션 클릭");

                switch (item.getItemId()){
                    case R.id.weather:

                        getSupportFragmentManager().beginTransaction() .replace(R.id.main_layout,fragment_weater).commitAllowingStateLoss();
                        return true;
                    case R.id.calendar:

                        getSupportFragmentManager().beginTransaction() .replace(R.id.main_layout,fragment_calendar).commitAllowingStateLoss();
                        return true;
                    case R.id.option:
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("nmap://route/public?"));
                        startActivity(intent);
//                        getSupportFragmentManager().beginTransaction() .replace(R.id.main_layout,fragment_option).commitAllowingStateLoss();
                        return true;
                }
                return true;
            }
        });
        checkPermission();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (isGPSEnable && isNetworkEnable) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location!=null){
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                Log.i("디버깅","값확인좀 = "+latitude+"/"+longitude);
            }else{
                Location locations = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if(locations!=null){
                    latitude = locations.getLatitude();
                    longitude = locations.getLongitude();
                    Log.i("디버깅","값확인좀 = "+latitude+"/"+longitude);
                }
            }
        }

    }



    private String[] permission = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
    private boolean mLocationPermissionApproved = false;
    private boolean checkPermission() {
        int result;
        List<String> listPermission = new ArrayList<>();
        for (String p : permission) {
            result = ContextCompat.checkSelfPermission(MainActivity.this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermission.add(p);
            }
        }
        if (!listPermission.isEmpty()) {
            ActivityCompat.requestPermissions(MainActivity.this, listPermission.toArray(new String[listPermission.size()]), 0);
            return false;
        }
        return true;
    }


    public final ActivityResultLauncher<Intent> getSearchResults = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // Search Activity 로부터의 결과 값이 이곳으로 전달 (setResult에 의해)
                if(result.getResultCode() == RESULT_OK){
                    if(result.getData() != null){

                        temp = (result.getData().getStringExtra("data"));
                        CustomCalendarView.start.setText(temp);
                        result.getData().putExtra("null","");
                    }
                }
            }
    );
    public final ActivityResultLauncher<Intent> getSearchResultd = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // Search Activity 로부터의 결과 값이 이곳으로 전달 (setResult에 의해)
                if(result.getResultCode() == RESULT_OK){
                    if(result.getData() != null){

                        temp = (result.getData().getStringExtra("data"));
                        CustomCalendarView.destination.setText(temp);
                        result.getData().putExtra("null","");
                    }
                }
            }
    );



}