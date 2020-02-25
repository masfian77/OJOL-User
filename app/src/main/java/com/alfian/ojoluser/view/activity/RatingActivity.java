package com.alfian.ojoluser.view.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.alfian.ojoluser.R;
import com.alfian.ojoluser.base.BaseActivity;
import com.alfian.ojoluser.helper.HeroHelper;
import com.alfian.ojoluser.helper.SessionManager;
import com.alfian.ojoluser.presenter.rating.RatingContract;
import com.alfian.ojoluser.presenter.rating.RatingPresenter;
import com.rengwuxian.materialedittext.MaterialEditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import static com.alfian.ojoluser.helper.MyContants.IDBOOKING;
import static com.alfian.ojoluser.helper.MyContants.IDDRIVER;

public class RatingActivity extends BaseActivity implements RatingContract.View {

    @BindView(R.id.txtReview)
    TextView txtReview;
    @BindView(R.id.ivReviewFoto)
    ImageView ivReviewFoto;
    @BindView(R.id.txtReviewUserNama)
    TextView txtReviewUserNama;
    @BindView(R.id.ratingReview)
    RatingBar ratingReview;
    @BindView(R.id.txtReview2)
    TextView txtReview2;
    @BindView(R.id.edtReviewComment)
    MaterialEditText edtReviewComment;
    @BindView(R.id.txtReview3)
    TextView txtReview3;
    @BindView(R.id.cboReview)
    CheckBox cboReview;
    @BindView(R.id.btnReview)
    Button btnReview;
    private ProgressDialog loading;
    private String iddriver;
    private String idbooking;
    private RatingPresenter presenter;
    private SessionManager session;
    private float nilaiRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        ButterKnife.bind(this);
        loading = new ProgressDialog(this);
        iddriver = getIntent().getStringExtra(IDDRIVER);
        idbooking = getIntent().getStringExtra(IDBOOKING);
        presenter = new RatingPresenter(this);
        ratingReview.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                nilaiRating = v;
            }
        });
    }

    @OnClick(R.id.btnReview)
    public void onViewClicked() {
        session = new SessionManager(this);
        String iduser = session.getIdUser();
        String token = session.getToken();
        String device = HeroHelper.getDeviceUUID(this);
        String rating = String.valueOf(nilaiRating);
        String comment = edtReviewComment.getText().toString();
        presenter.setRatingDriver(iduser,iddriver,idbooking,rating,comment,token,device);
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
        myToast(localizedMessage);
    }

    @Override
    public void showMsg(String msg) {
        myToast(msg);
    }

    @Override
    public void back() {
        finish();
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
    protected void onStart() {
        super.onStart();
        onAttachView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onDetachView();
    }
}
