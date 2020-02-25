package com.alfian.ojoluser.network;


import com.alfian.ojoluser.model.modelauth.ResponseAuth;
import com.alfian.ojoluser.model.modeldetail.ResponseDetailDriver;
import com.alfian.ojoluser.model.modelhistory.ResponseHistory;
import com.alfian.ojoluser.model.modelmap.ResponseMap;
import com.alfian.ojoluser.model.modelreqorder.ResponseBooking;
import com.alfian.ojoluser.model.modelwaiting.ResponseWaiting;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RestApi {
    @FormUrlEncoded
    @POST("daftar")
    Call<ResponseAuth> registerUser(
            @Field("email")String email,
            @Field("password")String password,
            @Field("phone")String phone,
            @Field("nama")String nama
    );

    @FormUrlEncoded
    @POST("login")
    Call<ResponseAuth> loginUser(
            @Field("f_email")String email,
            @Field("f_password")String password,
            @Field("device")String device
    );

    @GET("json")
    Call<ResponseMap> getDataMap(
            @Query("origin")String origin,
            @Query("destination")String destination,
            @Query("key")String key
    );

    @FormUrlEncoded
    @POST("insert_booking")
    Call<ResponseBooking> insertBooking(
            @Field("f_idUser")String iduser,
            @Field("f_latAwal")String latawal,
            @Field("f_lngAwal")String longawal,
            @Field("f_awal")String awal,
            @Field("f_latAkhir")String latakhir,
            @Field("f_lngAkhir")String longakhir,
            @Field("f_akhir")String akhir,
            @Field("f_catatan")String catatan,
            @Field("f_jarak")String jarak,
            @Field("f_token")String token,
            @Field("f_device")String device
    );

    @FormUrlEncoded
    @POST("checkBooking")
    Call<ResponseWaiting> cekStatusBooking(
            @Field("idbooking")String idbooking
    );

    @FormUrlEncoded
    @POST("cancel_booking")
    Call<ResponseWaiting> cancelBooking(
            @Field("idbooking")String idbooking,
            @Field("f_token")String token,
            @Field("f_device")String device

    );

    @FormUrlEncoded
    @POST("get_driver")
    Call<ResponseDetailDriver> getDetailDriver(
            @Field("f_iddriver")String iddriver
    );

    @FormUrlEncoded
    @POST("get_booking")
    Call<ResponseHistory> getDataHistory(
            @Field("status")String status,
            @Field("f_idUser")String idUser,
            @Field("f_token")String token,
            @Field("f_device")String device

    );

    @FormUrlEncoded
    @POST("complete_booking_from_user")
    Call<ResponseHistory> completeBooking(
            @Field("f_idUser")String f_idUser,
            @Field("id")String id,
            @Field("f_token")String token,
            @Field("f_device")String device

    );

    @FormUrlEncoded
    @POST("insert_review")
    Call<ResponseHistory> ratingDriver(
            @Field("f_idUser") String striduser,
            @Field("f_driver") String stridDriver,
            @Field("f_idBooking") String strIdbooking,
            @Field("f_ratting") String strRatting,
            @Field("f_comment") String strComment,
            @Field("f_token") String f_token,
            @Field("f_device") String f_device
    );
}