package com.alfian.ojoluser.base;

public interface BasePresenter<T extends BaseView> {
    public void onAttach(T view);
    public void onDetach();
}
