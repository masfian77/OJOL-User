package com.alfian.ojoluser.presenter.detaildriver;

import com.alfian.ojoluser.base.BaseView;
import com.alfian.ojoluser.model.modeldetail.DataDetailDriver;
import com.alfian.ojoluser.model.modeldetail.ResponseDetailDriver;
import com.alfian.ojoluser.network.InitRetrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailDriverPresenter implements DetailDriverContract.Presenter {
    DetailDriverContract.View detailView;
    BaseView view;

    public DetailDriverPresenter(DetailDriverContract.View detailView) {
        this.detailView = detailView;
    }

    @Override
    public void getDetailDriver(String idDriver) {
        detailView.showLoading("get detail driver");
        InitRetrofit.getInstance().getDetailDriver(idDriver).enqueue(new Callback<ResponseDetailDriver>() {
            @Override
            public void onResponse(Call<ResponseDetailDriver> call, Response<ResponseDetailDriver> response) {
                detailView.hideLoading();
                if (response.isSuccessful()) {
                    String result = response.body().getResult();
                    String msg = response.body().getMsg();
                    if (result.equals("true")) {
                        detailView.showMsg(msg);
                        List<DataDetailDriver> datadriver = response.body().getData();
                        detailView.getDataDriver(datadriver);
                    } else {
                        detailView.showMsg(msg);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseDetailDriver> call, Throwable t) {
                detailView.showError(t.getLocalizedMessage());
                detailView.hideLoading();
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
