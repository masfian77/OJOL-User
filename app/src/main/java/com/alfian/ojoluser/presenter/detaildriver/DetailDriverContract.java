package com.alfian.ojoluser.presenter.detaildriver;

import com.alfian.ojoluser.base.BasePresenter;
import com.alfian.ojoluser.base.BaseView;
import com.alfian.ojoluser.model.modeldetail.DataDetailDriver;

import java.util.List;

public interface DetailDriverContract {
    interface Presenter extends BasePresenter {
      void getDetailDriver(String idDriver);
    }
    interface View extends BaseView<BasePresenter> {
        void showMsg(String msg);
        void showLoading(String register);
        void hideLoading();
        void showError(String localizedMessage);
        void getDataDriver(List<DataDetailDriver> datadriver);
    }
}
