package com.alfian.ojoluser.presenter.waitingdriver;

import com.alfian.ojoluser.base.BasePresenter;
import com.alfian.ojoluser.base.BaseView;

public interface WaitingDriverContract {
    interface Presenter extends BasePresenter {
        void cancelRequestOrder(String idbooking, String token, String device);
        void cekStatusBooking(String idbooking);
    }
    interface View extends BaseView<BasePresenter> {
        void showMsg(String msg);
        void showLoading(String register);
        void hideLoading();
        void showError(String localizedMessage);
        void getDataDriver(String iddriver);
        void back();
    }
}
