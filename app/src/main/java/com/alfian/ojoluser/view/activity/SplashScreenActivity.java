package com.alfian.ojoluser.view.activity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import com.airbnb.lottie.LottieAnimationView;
import com.alfian.ojoluser.MainActivity;
import com.alfian.ojoluser.R;
import com.alfian.ojoluser.base.BaseActivity;
import com.alfian.ojoluser.helper.SessionManager;
import com.alfian.ojoluser.presenter.splash.SplashContract;
import com.alfian.ojoluser.presenter.splash.SplashPresenter;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashScreenActivity extends BaseActivity implements SplashContract.View {

    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.lotti1)
    LottieAnimationView lotti1;

    SplashPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);
        presenter = new SplashPresenter(this);
        presenter.delaySplash((long)3000, lotti1);
    }

    @Override
    public void welcomeMsg() {
        Toast.makeText(this, "Selamat datang di Aplikasi OJOL", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void pindahHalaman() {
        SessionManager sesi = new SessionManager(this);
        if (sesi.isLogin()) {
            myIntent(MainActivity.class);
        } else {
            myIntent(AuthActivity.class);
        }
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
