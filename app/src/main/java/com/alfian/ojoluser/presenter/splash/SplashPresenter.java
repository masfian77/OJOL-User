package com.alfian.ojoluser.presenter.splash;

import com.airbnb.lottie.LottieAnimationView;
import com.alfian.ojoluser.base.BaseView;

import android.os.Handler;

public class SplashPresenter implements SplashContract.Presenter{
    SplashContract.View splashView;
    BaseView view;

    public SplashPresenter(SplashContract.View splashView) {
        this.splashView = splashView;
    }

    @Override
    public void delaySplash(long timer, LottieAnimationView lotti1) {
        lotti1.setAnimation("motorcycle.json");
        lotti1.loop(true);
        lotti1.playAnimation();
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                splashView.welcomeMsg();
                splashView.pindahHalaman();
            }
        },timer);
    }

    @Override
    public void onAttach(BaseView view) {
        this.view = view;
    }

    @Override
    public void onDetach() {
        view = null;
    }
}
