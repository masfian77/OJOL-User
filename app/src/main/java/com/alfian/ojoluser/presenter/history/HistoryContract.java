package com.alfian.ojoluser.presenter.history;

import com.alfian.ojoluser.base.BasePresenter;
import com.alfian.ojoluser.base.BaseView;
import com.alfian.ojoluser.model.modelhistory.DataHistory;

import java.util.List;

public interface HistoryContract {
    interface Presenter extends BasePresenter {
        void getDataHistory(int status, String token, String device, String iduser);
    }
    interface View extends BaseView<BasePresenter> {
        void showMsg(String msg);
        void showLoading(String register);
        void hideLoading();
        void showError(String localizedMessage);
        void dataHistory(List<DataHistory> dataHistoryComplete);
    }
}
