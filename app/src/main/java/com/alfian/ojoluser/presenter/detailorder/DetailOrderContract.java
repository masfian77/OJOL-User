package com.alfian.ojoluser.presenter.detailorder;

import com.alfian.ojoluser.base.BasePresenter;
import com.alfian.ojoluser.base.BaseView;

public interface DetailOrderContract {
    interface Presenter extends BasePresenter {
        void detailRute(String origin, String desti, String key);
        void completeBooking(String iduser, String idbooking, String token, String device);
    }
    interface View extends BaseView<BasePresenter> {
        void showMsg(String msg);
        void showLoading(String register);
        void hideLoading();
        void showError(String localizedMessage);
        void getDataMap(String dataGaris);
        void pindahHalaman();
    }
}
