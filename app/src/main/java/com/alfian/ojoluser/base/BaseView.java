package com.alfian.ojoluser.base;

public interface BaseView <T extends BasePresenter> {
     void onAttachView();
     void onDetachView();

}
