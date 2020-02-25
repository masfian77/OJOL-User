package com.alfian.ojoluser.presenter.waitingdriver;

import com.alfian.ojoluser.base.BaseView;
import com.alfian.ojoluser.model.modelwaiting.ResponseWaiting;
import com.alfian.ojoluser.network.InitRetrofit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WaitingDriverPresenter implements WaitingDriverContract.Presenter {
    WaitingDriverContract.View waitingView;
    BaseView view;

    public WaitingDriverPresenter(WaitingDriverContract.View waitingView) {
        this.waitingView = waitingView;
    }

    @Override
    public void cancelRequestOrder(String idbooking, String token, String device) {
        waitingView.showLoading("cancel order");
        InitRetrofit.getInstance().cancelBooking(idbooking, token, device).enqueue(new Callback<ResponseWaiting>() {
            @Override
            public void onResponse(Call<ResponseWaiting> call, Response<ResponseWaiting> response) {
                waitingView.hideLoading();
                if (response.isSuccessful()) {
                    String result = response.body().getResult();
                    String msg = response.body().getMsg();
                    if (result.equals("true")) {
                        waitingView.showMsg(msg);
                        waitingView.back();
                    } else {
                        waitingView.showMsg(msg);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseWaiting> call, Throwable t) {
                waitingView.showError(t.getLocalizedMessage());
                waitingView.hideLoading();
            }
        });
    }

    @Override
    public void cekStatusBooking(String idbooking) {
        InitRetrofit.getInstance().cekStatusBooking(idbooking).enqueue(new Callback<ResponseWaiting>() {
            @Override
            public void onResponse(Call<ResponseWaiting> call, Response<ResponseWaiting> response) {
                if (response.isSuccessful()) {
                    String result = response.body().getResult();
                    String msg = response.body().getMsg();
                    if (result.equals("true")) {
                        waitingView.showMsg(msg);
                        String iddriver = response.body().getDriver();
                        waitingView.getDataDriver(iddriver);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseWaiting> call, Throwable t) {
                waitingView.showError(t.getLocalizedMessage());
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
