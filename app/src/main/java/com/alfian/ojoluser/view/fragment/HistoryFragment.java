package com.alfian.ojoluser.view.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.alfian.ojoluser.R;
import com.alfian.ojoluser.adapter.CustomHistoryAdapter;
import com.alfian.ojoluser.helper.HeroHelper;
import com.alfian.ojoluser.helper.SessionManager;
import com.alfian.ojoluser.model.modelhistory.DataHistory;
import com.alfian.ojoluser.presenter.history.HistoryContract;
import com.alfian.ojoluser.presenter.history.HistoryPresenter;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryFragment extends Fragment implements HistoryContract.View {
    int status;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    HistoryPresenter presenter;
    private SessionManager session;
    private ProgressDialog loading;

    public HistoryFragment(int i) {
        status = i;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_history, container, false);
        ButterKnife.bind(this, v);
        loading = new ProgressDialog(getActivity());
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new HistoryPresenter(this);
        session = new SessionManager(getActivity());
        String token = session.getToken();
        String device = HeroHelper.getDeviceUUID(getActivity());
        String iduser = session.getIdUser();
        presenter.getDataHistory(status,token,device,iduser);

    }

    @Override
    public void showMsg(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading(String info) {
        loading.setTitle("proses " + info);
        loading.setMessage("loading...");
        loading.show();
    }

    @Override
    public void hideLoading() {
        loading.dismiss();
    }

    @Override
    public void showError(String localizedMessage) {
        Toast.makeText(getActivity(), localizedMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void dataHistory(List<DataHistory> dataHistory) {
        CustomHistoryAdapter adapter = new CustomHistoryAdapter(getActivity(), dataHistory, status);
        recyclerview.setAdapter(adapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onAttachView() {
        presenter.onAttach(this);
    }

    @Override
    public void onDetachView() {
        presenter.onDetach();
    }

    @Override
    public void onStart() {
        super.onStart();
        onAttachView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        onDetachView();
    }
}
