package com.alfian.ojoluser.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.alfian.ojoluser.R;
import com.alfian.ojoluser.base.BaseActivity;
import com.alfian.ojoluser.helper.DirectionMapsV2;
import com.alfian.ojoluser.helper.GPSTracker;
import com.alfian.ojoluser.helper.HeroHelper;
import com.alfian.ojoluser.helper.MyContants;
import com.alfian.ojoluser.helper.SessionManager;
import com.alfian.ojoluser.presenter.goride.GorideContract;
import com.alfian.ojoluser.presenter.goride.GoridePresenter;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.compat.AutocompleteFilter;
import com.google.android.libraries.places.compat.Place;
import com.google.android.libraries.places.compat.ui.PlaceAutocomplete;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import static com.alfian.ojoluser.helper.MyContants.IDBOOKING;
import static com.alfian.ojoluser.helper.MyContants.LOKASIAWAL;
import static com.alfian.ojoluser.helper.MyContants.LOKASITUJUAN;
import static com.alfian.ojoluser.helper.MyContants.TARIF;

public class GoRideActivity extends BaseActivity implements OnMapReadyCallback, GorideContract.View {

    @BindView(R.id.imgpick)
    ImageView imgpick;
    @BindView(R.id.lokasiawal)
    TextView lokasiawal;
    @BindView(R.id.lokasitujuan)
    TextView lokasitujuan;
    @BindView(R.id.edtcatatan)
    EditText edtcatatan;
    @BindView(R.id.txtharga)
    TextView txtharga;
    @BindView(R.id.txtjarak)
    TextView txtjarak;
    @BindView(R.id.txtdurasi)
    TextView txtdurasi;
    @BindView(R.id.requestorder)
    Button requestorder;
    @BindView(R.id.rootlayout)
    RelativeLayout rootlayout;
    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    GoridePresenter presenter;
    private double myLat;
    private double myLng;
    private LatLng myLatLng;
    private String nameLocation;
    private double desLat;
    private double desLng;
    private String nameDesLocation;
    private ProgressDialog loading;
    private GPSTracker gps;
    private String value;
    private double latawal;
    private double lonawal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        checkGpsDevice(this);
        presenter = new GoridePresenter(this);
        loading = new ProgressDialog(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getMyLocation();
    }

    private void getMyLocation() {
        gps = new GPSTracker(this);

        if (gps.canGetLocation()) {
            myLat = gps.getLatitude();
            myLng = gps.getLongitude();
            Toast.makeText(this, "lat :" + myLat + "lon :" + myLng, Toast.LENGTH_SHORT)
                    .show();
            addMarker(myLat, myLng);
            lokasiawal.setText(nameLocation);
        }
    }

    private void addMarker(double myLat, double myLng) {
        myLatLng = new LatLng(myLat, myLng);
        nameLocation = convertLocation(myLat, myLng);
        mMap.addMarker(new MarkerOptions().position(myLatLng).title(nameLocation))
                .setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_pickup));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, 18));
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
    }

    private String convertLocation(double myLat, double myLng) {
        nameLocation = null;
        Geocoder geocoder = new Geocoder(GoRideActivity.this, Locale.getDefault());
        try {
            List<Address> list = geocoder.getFromLocation(myLat, myLng, 1);
            if (list != null && list.size() > 0) {
                nameLocation = list.get(0).getAddressLine(0) + "" + list.get(0).getCountryName();

                //fetch data from addresses
            } else {
                Toast.makeText(GoRideActivity.this, "kosong", Toast.LENGTH_SHORT).show();
                //display Toast message
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nameLocation;
    }

    @OnClick({R.id.imgpick, R.id.lokasiawal, R.id.lokasitujuan, R.id.requestorder})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgpick:
                startActivityForResult(new Intent(GoRideActivity.this, MapsActivity.class), 0);
                break;
            case R.id.lokasiawal:
                setLocation(LOKASIAWAL);
                break;
            case R.id.lokasitujuan:
                setLocation(LOKASITUJUAN);
                break;
            case R.id.requestorder:
                SessionManager sessionManager = new SessionManager(this);
                String iduser = sessionManager.getIdUser();
                String latawal = String.valueOf(myLat);
                String awal = lokasiawal.getText().toString();
                String latakhir = String.valueOf(desLat);
                String longakhir = String.valueOf(desLng);
                String akhir = lokasitujuan.getText().toString();
                String catatan = edtcatatan.getText().toString();
                String jarak = txtjarak.getText().toString();
                String longawal = String.valueOf(myLng);
                String token = sessionManager.getToken();
                String device = HeroHelper.getDeviceUUID(this);
                presenter.requestOrder(iduser, latawal, longawal, awal, latakhir, longakhir, akhir, catatan, jarak, token, device);
                break;
        }
    }

    private void setLocation(int lokasi) {
        AutocompleteFilter filter = new AutocompleteFilter.Builder()
                .setCountry("ID")
                .build();

        Intent i = null;
        try {
            i = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .setFilter(filter)
                    .build(GoRideActivity.this);
            startActivityForResult(i, lokasi);
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOKASIAWAL && resultCode == RESULT_OK) {
            Place place = PlaceAutocomplete.getPlace(this, data);
            myLat = place.getLatLng().latitude;
            myLng = place.getLatLng().longitude;
            myLatLng = new LatLng(myLat, myLng);
            mMap.clear();
            nameLocation = place.getAddress().toString();
            mMap.addMarker(new MarkerOptions().position(myLatLng).title(nameLocation))
                    .setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_pickup));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, 17));
            lokasiawal.setText(nameLocation);
        } else if (requestCode == LOKASITUJUAN && resultCode == RESULT_OK) {
            Place place = PlaceAutocomplete.getPlace(this, data);

            desLat = place.getLatLng().latitude;
            desLng = place.getLatLng().longitude;
            LatLng desLatLng = new LatLng(desLat, desLng);
            nameDesLocation = place.getAddress().toString();
            mMap.addMarker(new MarkerOptions().position(desLatLng).title(nameDesLocation))
                    .setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_pickup));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(desLatLng, 17));
            String key = getString(R.string.google_maps_key);
            presenter.getDataMap(myLat+","+myLng,desLat+","+desLng,key);
            lokasitujuan.setText(nameDesLocation);
        }
        if (data == null) {
            Toast.makeText(this, "no data", Toast.LENGTH_SHORT).show();
            return; }else {


            switch (resultCode) {
                case MyContants.LOKASI:
                    Bundle resultData = data.getExtras();
                    value = resultData.getString("value");
                    latawal = resultData.getDouble("lat");
                    lonawal = resultData.getDouble("lon");
                    //Implicit intent to make a call
                    mMap.clear();
                    lokasiawal.setText(value);
//                    latawal = lati;
//                    lonawal = longi;
                    break;

            }
        }
    }

    @Override
    public void showMsg(String msg) {
        myToast(msg);
    }

    @Override
    public void showLoading(String info) {
        loading.setTitle("proses "+info+"user");
        loading.setMessage("loading . . . .");
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
    public void showInfoOrder(String durasi, String jarak, double harga) {
        txtdurasi.setText(durasi);
        txtjarak.setText(jarak);
        txtharga.setText(String.valueOf(harga));
    }

    @Override
    public void showDataBooking(String idbooking, String tarif) {
        Intent krm = new Intent(this, WaitingDriverActivity.class);
        krm.putExtra(IDBOOKING,idbooking);
        krm.putExtra(TARIF,tarif);
        startActivity(krm);
    }

    @Override
    public void dataGaris(String dataGaris) {
        DirectionMapsV2 mapsV2 = new DirectionMapsV2(this);
        mapsV2.gambarRoute(mMap, dataGaris);
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
