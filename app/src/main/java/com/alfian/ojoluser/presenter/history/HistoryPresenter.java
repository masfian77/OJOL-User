package com.alfian.ojoluser.presenter.history;

import com.alfian.ojoluser.base.BaseView;
import com.alfian.ojoluser.model.modelhistory.DataHistory;
import com.alfian.ojoluser.model.modelhistory.ResponseHistory;
import com.alfian.ojoluser.network.InitRetrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryPresenter implements HistoryContract.Presenter {
    HistoryContract.View historyView;
    BaseView view;

    public static List<DataHistory> dataHistory;
    public static List<DataHistory> dataHistoryComplete;

    public HistoryPresenter(HistoryContract.View historyView) {
        this.historyView = historyView;
    }

    @Override
    public void getDataHistory(int status, String token, String device, String iduser) {
        historyView.showLoading("proses get data history");
        if (String.valueOf(status).equals("2")) {
            InitRetrofit.getInstance().getDataHistory(String.valueOf(status),iduser,token,device).enqueue(new Callback<ResponseHistory>() {
                @Override
                public void onResponse(Call<ResponseHistory> call, Response<ResponseHistory> response) {
                    historyView.hideLoading();
                    if (response.isSuccessful()) {
                        String result = response.body().getResult();
                        String msg = response.body().getMsg();
                        if (result.equals("true")) {
                            historyView.showMsg(msg);
                            dataHistory = response.body().getData();
                            historyView.dataHistory(dataHistory);
                        } else {
                            historyView.showMsg(msg);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseHistory> call, Throwable t) {
                    historyView.showError(t.getLocalizedMessage());
                    historyView.hideLoading();
                }
            });
        } else if (String.valueOf(status).equals("4")) {
            InitRetrofit.getInstance().getDataHistory(String.valueOf(status), iduser, token, device).enqueue(new Callback<ResponseHistory>() {
                @Override
                public void onResponse(Call<ResponseHistory> call, Response<ResponseHistory> response) {
                    historyView.hideLoading();
                    if (response.isSuccessful()) {
                        String result = response.body().getResult();
                        String msg = response.body().getMsg();
                        if (result.equals("true")) {
                            historyView.showMsg(msg);
                            dataHistoryComplete = response.body().getData();
                            historyView.dataHistory(dataHistoryComplete);
                        } else {
                            historyView.showMsg(msg);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseHistory> call, Throwable t) {
                    historyView.showError(t.getLocalizedMessage());
                    historyView.hideLoading();
                }
            });
        }
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
