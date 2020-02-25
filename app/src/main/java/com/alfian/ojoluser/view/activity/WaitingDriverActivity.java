package com.alfian.ojoluser.view.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;
import com.alfian.ojoluser.R;
import com.alfian.ojoluser.base.BaseActivity;
import com.alfian.ojoluser.helper.HeroHelper;
import com.alfian.ojoluser.helper.SessionManager;
import com.alfian.ojoluser.presenter.waitingdriver.WaitingDriverContract;
import com.alfian.ojoluser.presenter.waitingdriver.WaitingDriverPresenter;
import java.util.Timer;
import java.util.TimerTask;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.bclogic.pulsator4droid.library.PulsatorLayout;
import static com.alfian.ojoluser.helper.MyContants.IDBOOKING;
import static com.alfian.ojoluser.helper.MyContants.IDDRIVER;

public class WaitingDriverActivity extends BaseActivity implements WaitingDriverContract.View {

    @BindView(R.id.pulsator)
    PulsatorLayout pulsator;
    @BindView(R.id.buttoncancel)
    Button buttoncancel;
    private String idbooking;
    WaitingDriverPresenter presenter;
    SessionManager session;
    ProgressDialog loading;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_driver);
        ButterKnife.bind(this);
        idbooking = getIntent().getStringExtra(IDBOOKING);
        pulsator.start();
        timer = new Timer();
        presenter = new WaitingDriverPresenter(this);
        presenter.cekStatusBooking(idbooking);
        loading = new ProgressDialog(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        presenter.cekStatusBooking(idbooking);
                    }
                }, 0, 5000);
            }
        });
    }


    @OnClick(R.id.buttoncancel)
    public void onViewClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("cancel order");
        builder.setMessage("apakah anda yakin cancel orderan ini ?");
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                session = new SessionManager(WaitingDriverActivity.this);
                String token = session.getToken();
                String device = HeroHelper.getDeviceUUID(WaitingDriverActivity.this);
                presenter.cancelRequestOrder(idbooking, token, device);
            }
        });
        builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog alert = builder.create();
        alert.show();
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
    public void getDataDriver(String iddriver) {
        Intent kirim = new Intent(this, DetailDriverActivity.class);
        kirim.putExtra(IDDRIVER, iddriver);
        startActivity(kirim);
        finish();
    }

    @Override
    public void back() {
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
        timer.cancel();
    }
}
