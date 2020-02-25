package com.alfian.ojoluser.view.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import com.alfian.ojoluser.R;
import com.alfian.ojoluser.base.BaseActivity;
import com.alfian.ojoluser.helper.MyContants;
import com.alfian.ojoluser.model.modeldetail.DataDetailDriver;
import com.alfian.ojoluser.presenter.detaildriver.DetailDriverContract;
import com.alfian.ojoluser.presenter.detaildriver.DetailDriverPresenter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import static com.alfian.ojoluser.helper.MyContants.IDDRIVER;

public class DetailDriverActivity extends BaseActivity implements OnMapReadyCallback, DetailDriverContract.View {

    @BindView(R.id.lokasiawal)
    TextView lokasiawal;
    @BindView(R.id.lokasitujuan)
    TextView lokasitujuan;
    @BindView(R.id.txtnamadriver)
    TextView txtnamadriver;
    @BindView(R.id.linear2)
    LinearLayout linear2;
    @BindView(R.id.txthpdriver)
    TextView txthpdriver;
    @BindView(R.id.linear1)
    LinearLayout linear1;
    private GoogleMap mMap;
    private String idDriver;
    DetailDriverPresenter presenter;
    private ProgressDialog loading;
    private LatLng posisiDriver;
    private double londriver;
    private double latdriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_driver);
        ButterKnife.bind(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        idDriver = getIntent().getStringExtra(IDDRIVER);
        presenter = new DetailDriverPresenter(this);
        loading = new ProgressDialog(this);
        callPermission(this, Manifest.permission.CALL_PHONE, MyContants.READPHONE);
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
        presenter.getDetailDriver(idDriver);
    }

    @Override
    public void showMsg(String msg) {
        myToast(msg);
    }

    @Override
    public void showLoading(String info) {
        loading.setTitle("proses " + info);
        loading.setMessage("loading...");
        loading.show();
    }

    @Override
    public void hideLoading() {
        loading.dismiss();
    }

    @Override
    public void showError(String localizedMessage) {
        myToast(localizedMessage);
    }

    @Override
    public void getDataDriver(List<DataDetailDriver> driver) {
        txthpdriver.setText(driver.get(0).getUserHp());
        txtnamadriver.setText(driver.get(0).getUserNama());
        latdriver = Double.parseDouble(driver.get(0).getTrackingLat());
        londriver = Double.parseDouble(driver.get(0).getTrackingLng());
        posisiDriver = new LatLng(latdriver, londriver);
        mMap.addMarker(new MarkerOptions().position(posisiDriver)).setIcon(
                BitmapDescriptorFactory.fromResource(R.mipmap.ic_car)
        );
        mMap.moveCamera(CameraUpdateFactory.
                newLatLngZoom(posisiDriver, 17));
        mMap.setPadding(40, 150, 50, 120);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        txthpdriver.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + driver.get(0).getUserHp())));
            }
        });

    }

    @Override
    public void onAttachView() {
        presenter.onAttach(this);
    }

    @Override
    public void onDetachView() {
        presenter.onDetach();
    }

    @Override
    protected void onStart() {
        super.onStart();
        onAttachView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onDetachView();
    }
}
