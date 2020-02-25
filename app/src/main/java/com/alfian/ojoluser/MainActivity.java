package com.alfian.ojoluser;

import android.Manifest;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import com.alfian.ojoluser.base.BaseActivity;
import com.alfian.ojoluser.view.activity.GoRideActivity;
import com.alfian.ojoluser.view.activity.HistoryActivity;
import static com.alfian.ojoluser.helper.MyContants.REQUEST_LOCATION;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        callPermission(this, Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_LOCATION);
    }

    public void onGoride(View view) {
        myIntent(GoRideActivity.class);
    }

    public void onHistory(View view) {
        myIntent(HistoryActivity.class);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.mn_logout){
            keluarApps(this,2,"logout","Apakah anda yakin logout app");
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        keluarApps(this,1,"keluar","Apakah anda yakin keluar app");
    }

}
