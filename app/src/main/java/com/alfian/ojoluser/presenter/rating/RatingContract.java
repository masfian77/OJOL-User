package com.alfian.ojoluser.presenter.rating;


import com.alfian.ojoluser.base.BasePresenter;
import com.alfian.ojoluser.base.BaseView;

public interface RatingContract {
    interface Presenter extends BasePresenter {
        void setRatingDriver(String iduser, String iddriver, String idbooking, String rating, String comment, String token, String device);
    }
    interface View extends BaseView<BasePresenter> {
        void showLoading(String pesanloading);
        void hideLoading();
        void showError(String localizedMessage);
        void showMsg(String msg);

        void back();
    }
}
