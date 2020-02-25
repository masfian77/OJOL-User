package com.alfian.ojoluser.presenter.goride;

import android.util.Log;

import com.alfian.ojoluser.base.BaseView;

import com.alfian.ojoluser.model.modelmap.Distance;
import com.alfian.ojoluser.model.modelmap.Duration;
import com.alfian.ojoluser.model.modelmap.LegsItem;
import com.alfian.ojoluser.model.modelmap.ResponseMap;
import com.alfian.ojoluser.model.modelmap.RoutesItem;
import com.alfian.ojoluser.model.modelreqorder.ResponseBooking;
import com.alfian.ojoluser.network.InitRetrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoridePresenter implements GorideContract.Presenter {
    GorideContract.View goRideView;
    BaseView view;

    public GoridePresenter(GorideContract.View goRideView) {
        this.goRideView = goRideView;
    }

    @Override
    public void getDataMap(String lokasiawal, String lokasitujuan, String key) {
        goRideView.showLoading("Get Data Map");
        InitRetrofit.getInstanceGoogle().getDataMap(lokasiawal, lokasitujuan, key).enqueue(new Callback<ResponseMap>() {
            @Override
            public void onResponse(Call<ResponseMap> call, Response<ResponseMap> response) {
            goRideView.hideLoading();
                if (response.isSuccessful() || response.code() == 200) {
                    String status = response.body().getStatus();

                    if (status.equals("OK")) {
                        List<RoutesItem> dataMap = response.body().getRoutes();
                        List<LegsItem> dataLegs = dataMap.get(0).getLegs();
                        Distance distance = dataLegs.get(0).getDistance();
                        Duration duration = dataLegs.get(0).getDuration();
                        double harga = Math.ceil((distance.getValue() / 1000) * 5000);
                        String jarak = String.valueOf(distance.getValue() / 1000);
                        goRideView.showInfoOrder(duration.getText(), jarak, harga);
                        String dataGaris = dataMap.get(0).getOverviewPolyline().getPoints();
                        goRideView.dataGaris(dataGaris);
                        goRideView.showMsg("tampil data");
                    } else {
                        goRideView.showMsg("gagal tampil data");
                    }
                } else {
                    goRideView.showMsg("gagal menampilkan json");
                }
            }

            @Override
            public void onFailure(Call<ResponseMap> call, Throwable t) {
                goRideView.showError(t.getLocalizedMessage());
                goRideView.hideLoading();
            }
        });
    }

    @Override
    public void requestOrder(String iduser, String latawal, String longawal, String awal, String latakhir, String longakhir, String akhir, String catatan, String jarak, String token, String device) {
        goRideView.showLoading("request order");
        InitRetrofit.getInstance().insertBooking(iduser, latawal, longawal, awal, latakhir, longakhir, akhir, catatan, jarak, token, device).enqueue(new Callback<ResponseBooking>() {
            @Override
            public void onResponse(Call<ResponseBooking> call, Response<ResponseBooking> response) {

                goRideView.hideLoading();
                Log.d("responsee", "1");
                Log.d("responsee", String.valueOf(response.code()));
                if (response.isSuccessful() || response.code() == 200) {

                    String result = response.body().getResult();
                    String msg = response.body().getMsg();

                    if (result.equals("true")) {

                        goRideView.showMsg(msg);
                        Log.d("responsee", "2");

                        String idbooking = String.valueOf(response.body().getIdBooking());
                        String tarif = String.valueOf(response.body().getTarif());

                        goRideView.showDataBooking(idbooking, tarif);
                    } else {
                        Log.d("responsee", "3");
                        goRideView.showMsg(msg);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBooking> call, Throwable t) {
                goRideView.hideLoading();
                Log.d("responsee", "4");
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
