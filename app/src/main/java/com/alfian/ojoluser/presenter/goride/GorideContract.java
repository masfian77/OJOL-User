package com.alfian.ojoluser.presenter.goride;

import com.alfian.ojoluser.base.BasePresenter;
import com.alfian.ojoluser.base.BaseView;

public interface GorideContract {
    interface Presenter extends BasePresenter {
        void getDataMap(String lokasiawal, String lokasitujuan, String key);
        void requestOrder(String iduser, String latawal, String awal, String latakhir, String longakhir, String akhir, String catatan, String jarak, String longawal, String token, String device);
    }
    interface View extends BaseView<BasePresenter> {
        void showMsg(String msg);
        void showLoading(String register);
        void hideLoading();
        void showError(String localizedMessage);
        void showInfoOrder(String durasi, String jarak, double harga);
        void showDataBooking(String idbooking, String tarif);
        void dataGaris(String dataGaris);
    }
}
