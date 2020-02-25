package com.alfian.ojoluser.presenter.rating;



import com.alfian.ojoluser.base.BaseView;
import com.alfian.ojoluser.model.modelhistory.ResponseHistory;
import com.alfian.ojoluser.network.InitRetrofit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatingPresenter implements RatingContract.Presenter {

    RatingContract.View ratingView;
    BaseView view;

    public RatingPresenter(RatingContract.View view) {
        ratingView = view;
    }

    @Override
    public void setRatingDriver(String iduser, String iddriver, String idbooking, String rating, String comment, String token, String device) {
        ratingView.showLoading("proses gift rating driver");
        InitRetrofit.getInstance().ratingDriver(iduser, iddriver, idbooking, rating, comment, token, device)
                .enqueue(new Callback<ResponseHistory>() {
                    @Override
                    public void onResponse(Call<ResponseHistory> call, Response<ResponseHistory> response) {
                        ratingView.hideLoading();
                        if (response.isSuccessful()) {
                            String result = response.body().getResult();
                            String msg = response.body().getMsg();
                            if (result.equals("true")) {
                                ratingView.showMsg(msg);
                                ratingView.back();

                            } else {

                                ratingView.showMsg(msg);
                               }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseHistory> call, Throwable t) {
                        ratingView.showError(t.getLocalizedMessage());
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
