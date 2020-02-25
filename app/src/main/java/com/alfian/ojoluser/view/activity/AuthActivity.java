package com.alfian.ojoluser.view.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import com.alfian.ojoluser.MainActivity;
import com.alfian.ojoluser.R;
import com.alfian.ojoluser.base.BaseActivity;
import com.alfian.ojoluser.helper.GPSTracker;
import com.alfian.ojoluser.helper.HeroHelper;
import com.alfian.ojoluser.helper.SessionManager;
import com.alfian.ojoluser.model.modelauth.ResponseAuth;
import com.alfian.ojoluser.presenter.auth.AuthContract;
import com.alfian.ojoluser.presenter.auth.AuthPresenter;
import com.rengwuxian.materialedittext.MaterialEditText;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import static com.alfian.ojoluser.helper.MyContants.READPHONE;

public class AuthActivity extends BaseActivity implements AuthContract.View {

    AuthPresenter presenter;
    @BindView(R.id.txt_rider_app)
    TextView txtRiderApp;
    @BindView(R.id.btnSignIn)
    Button btnSignIn;
    @BindView(R.id.btnRegister)
    Button btnRegister;
    @BindView(R.id.rootlayout)
    RelativeLayout rootlayout;
    private View v;
    private RegisterViewHolder holderRegister;
    private AlertDialog dialogRegis;
    private ProgressDialog loading;
    private AlertDialog dialogLogin;
    private LoginViewHolder holderLogin;
    private GPSTracker gps;
    private double myLat;
    private double myLng;
    private String nameLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        ButterKnife.bind(this);
        presenter = new AuthPresenter(this);
        loading = new ProgressDialog(this);
        callPermission(this, Manifest.permission.READ_PHONE_STATE, READPHONE);

    }

    @OnClick({R.id.btnSignIn, R.id.btnRegister})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnSignIn:
                login();
                break;
            case R.id.btnRegister:
                register();
                break;
        }
    }

    private void register() {
        LayoutInflater inflater = getLayoutInflater();
        v = inflater.inflate(R.layout.layout_register, null, false);
        holderRegister =new RegisterViewHolder(v);
        holderRegister.imgalamat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMyLocation();
            }
        });
        dialogRegis = new AlertDialog.Builder(this)
                .setView(v)
                .setTitle(getString(R.string.register))
                .setMessage(getString(R.string.messageregister))
                .setPositiveButton("register",null)
                .setNegativeButton("cancel",null)
                .create();
        dialogRegis.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button buttonPositive = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                buttonPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String email = holderRegister.edtEmail.getText().toString();
                        String alamat = holderRegister.edtAlamat.getText().toString();
                        String password = holderRegister.edtPassword.getText().toString();
                        String comfpassword = holderRegister.edtConfPassword.getText().toString();
                        String nama = holderRegister.edtName.getText().toString();
                        String phone = holderRegister.edtPhone.getText().toString();

                        if (TextUtils.isEmpty(nama)) {
                            holderRegister.edtName.setError(getString(R.string.requirename));
                        }
                        else if (TextUtils.isEmpty(email)) {
                            holderRegister.edtEmail.setError(getString(R.string.requireemail));
                        } else if (TextUtils.isEmpty(password)) {
                            holderRegister.edtPassword.setError(getString(R.string.requirepassword));
                        } else if (!password.equals(comfpassword)) {
                            holderRegister.edtConfPassword.setError("password confirm tidak sama,cek kembali");
                        } else if (TextUtils.isEmpty(alamat)) {
                            holderRegister.edtAlamat.setError(getString(R.string.requirealamat));
                        } else if (TextUtils.isEmpty(phone)) {
                            holderRegister.edtPhone.setError(getString(R.string.requirephone));
                        } else {
                            presenter.prosesRegister(email, password, nama, phone, dialog);
                        }
                    }
                });
            }
        });
        //menampilkan dialog regis
        dialogRegis.show();
    }

    private void getMyLocation() {
        gps = new GPSTracker(this);
        if (gps.canGetLocation()) {
            myLat = gps.getLatitude();
            myLng = gps.getLongitude();
            Toast.makeText(this, "lat :" + myLat + "lon :" + myLng, Toast.LENGTH_SHORT)
                    .show();
            //     addMarker(myLat, myLng);
            nameLocation = convertLocation(myLat, myLng);
            holderRegister.edtAlamat.setText(nameLocation);
        }
    }

    private String convertLocation(double myLat, double myLng) {
        nameLocation = null;
        Geocoder geocoder = new Geocoder(AuthActivity.this, Locale.getDefault());
        try {
            List<Address> list = geocoder.getFromLocation(myLat, myLng, 1);
            if (list != null && list.size() > 0) {
                nameLocation = list.get(0).getAddressLine(0) + "" + list.get(0).getCountryName();

                //fetch data from addresses
            } else {
                Toast.makeText(AuthActivity.this, "kosong", Toast.LENGTH_SHORT).show();
                //display Toast message
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nameLocation;
    }

    private void login() {
        LayoutInflater inflater = getLayoutInflater();
        v = inflater.inflate(R.layout.layout_login, null, false);
        holderLogin = new LoginViewHolder(v);
        dialogLogin = new AlertDialog.Builder(this)
                .setView(v)
                .setTitle(getString(R.string.login))
                .setMessage(getString(R.string.messagelogin))
                .setPositiveButton("login",null)
                .setNegativeButton("cancel",null)
                .create();
        dialogLogin.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button buttonPositive = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                buttonPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String email = holderLogin.edtEmail.getText().toString();
                        String password = holderLogin.edtPassword.getText().toString();

                        if (TextUtils.isEmpty(email)) {
                            holderLogin.edtEmail.setError(getString(R.string.requireemail));
                        } else if (TextUtils.isEmpty(password)) {
                            holderLogin.edtPassword.setError(getString(R.string.requirepassword));
                        } else {
                            String device = HeroHelper.getDeviceUUID(AuthActivity.this);
                            presenter.prosesLogin(email, password, device, dialog);
                        }
                    }
                });
            }
        });
        //menampilkan dialog regis
        dialogLogin.show();
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
    public void alertBerhasil(DialogInterface dialog) {
        AlertDialog dialogRegis = new AlertDialog.Builder(this)
//              .setView()
                .setTitle("Sukses")
                .setMessage("Terimakasih,registrasi berhasil")
                .setPositiveButton("OK", null)
                .create();
        dialogRegis.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface11) {
                Button buttonPositive = ((AlertDialog) dialogInterface11).getButton(DialogInterface.BUTTON_POSITIVE);
                buttonPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogInterface11.dismiss();
                    }
                });
            }
        });
        dialogRegis.show();

        AlertDialog dialogLogin = new AlertDialog.Builder(this)
//              .setView()
                .setTitle("Sukses")
                .setMessage("Login berhasil")
                .setPositiveButton("OK", null)
                .create();
        dialogLogin.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface11) {
                Button buttonPositive = ((AlertDialog) dialogInterface11).getButton(DialogInterface.BUTTON_POSITIVE);
                buttonPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogInterface11.dismiss();
                    }
                });
            }
        });
        dialogLogin.show();
    }

    @Override
    public void pindahHalaman(ResponseAuth dataUser) {
        SessionManager sesi = new SessionManager(this);
        sesi.createLoginSession(dataUser.getToken());
        sesi.setIduser(dataUser.getIdUser());
        myIntent(MainActivity.class);
        finish();
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


    static
    class RegisterViewHolder {
        @BindView(R.id.edtName)
        MaterialEditText edtName;
        @BindView(R.id.edtEmail)
        MaterialEditText edtEmail;
        @BindView(R.id.edtPhone)
        MaterialEditText edtPhone;
        @BindView(R.id.edtAlamat)
        MaterialEditText edtAlamat;
        @BindView(R.id.imgalamat)
        ImageView imgalamat;
        @BindView(R.id.edtPassword)
        MaterialEditText edtPassword;
        @BindView(R.id.edtConfPassword)
        MaterialEditText edtConfPassword;

        RegisterViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static
    class LoginViewHolder {
        @BindView(R.id.edtEmail)
        MaterialEditText edtEmail;
        @BindView(R.id.edtPassword)
        MaterialEditText edtPassword;

        LoginViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
