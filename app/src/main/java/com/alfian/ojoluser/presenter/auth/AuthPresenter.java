package com.alfian.ojoluser.presenter.auth;

import android.content.DialogInterface;

import com.alfian.ojoluser.base.BaseView;
import com.alfian.ojoluser.model.modelauth.ResponseAuth;
import com.alfian.ojoluser.network.InitRetrofit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthPresenter implements AuthContract.Presenter {
    AuthContract.View authView;
    BaseView view;

    public AuthPresenter(AuthContract.View authView) {
        this.authView = authView;
    }


    @Override
    public void prosesRegister(String email, String password, String nama, String phone, DialogInterface dialog) {
            authView.showLoading("Register");
            InitRetrofit.getInstance().registerUser(email, password, phone, nama).enqueue(new Callback<ResponseAuth>() {
                @Override
                public void onResponse(Call<ResponseAuth> call, Response<ResponseAuth> response) {
                    authView.hideLoading();
                    if (response.isSuccessful() || response.code() == 200) {
                        String msg = response.body().getMsg();
                        String result = response.body().getResult();
                        if (result.equals("true")) {
                            authView.showMsg(msg);
                            dialog.dismiss();
                            authView.alertBerhasil(dialog);
                        } else {
                            authView.showMsg(msg);
                        }
                    } else {
                        authView.showMsg("gagal menampilkan json");
                    }
                }

                @Override
                public void onFailure(Call<ResponseAuth> call, Throwable t) {
                    authView.showError(t.getLocalizedMessage());
                    authView.hideLoading();
                }
            });
    }

    @Override
    public void prosesLogin(String email, String password, String device, DialogInterface dialog) {
        authView.showLoading("Login");
        InitRetrofit.getInstance().loginUser(email, password, device).enqueue(new Callback<ResponseAuth>() {
            @Override
            public void onResponse(Call<ResponseAuth> call, Response<ResponseAuth> response) {
                authView.hideLoading();
                if (response.isSuccessful() || response.code() == 200) {
                    String msg = response.body().getMsg();
                    String result = response.body().getResult();
                    if (result.equals("true")) {
                        authView.showMsg(msg);
                        dialog.dismiss();
                        ResponseAuth dataUser = response.body();
                        authView.pindahHalaman(dataUser);
                    } else {
                        authView.showMsg(msg);
                    }
                } else {
                    authView.showMsg("gagal menampilkan json");
                }
            }

            @Override
            public void onFailure(Call<ResponseAuth> call, Throwable t) {
                authView.showError(t.getLocalizedMessage());
                authView.hideLoading();
            }
        });
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
