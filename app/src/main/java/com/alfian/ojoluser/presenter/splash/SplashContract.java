package com.alfian.ojoluser.presenter.splash;

import com.airbnb.lottie.LottieAnimationView;
import com.alfian.ojoluser.base.BasePresenter;
import com.alfian.ojoluser.base.BaseView;

public interface SplashContract {
    interface Presenter extends BasePresenter {
        void delaySplash(long i, LottieAnimationView lotti1);
    }
    interface View extends BaseView<BasePresenter> {
        void welcomeMsg();
        void pindahHalaman();
    }
}
