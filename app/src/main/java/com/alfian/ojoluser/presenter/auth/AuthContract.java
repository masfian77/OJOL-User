package com.alfian.ojoluser.presenter.auth;

import android.content.DialogInterface;

import com.alfian.ojoluser.base.BasePresenter;
import com.alfian.ojoluser.base.BaseView;
import com.alfian.ojoluser.model.modelauth.ResponseAuth;

public interface AuthContract {
    interface Presenter extends BasePresenter {
        void prosesLogin(String email, String password, String device, DialogInterface dialog);
        void prosesRegister(String email, String password, String nama, String phone, DialogInterface dialog);
    }
    interface View extends BaseView<BasePresenter> {
        void showMsg(String msg);
        void showLoading(String register);
        void hideLoading();
        void showError(String localizedMessage);
        void alertBerhasil(DialogInterface dialog);
        void pindahHalaman(ResponseAuth dataUser);
    }
}
