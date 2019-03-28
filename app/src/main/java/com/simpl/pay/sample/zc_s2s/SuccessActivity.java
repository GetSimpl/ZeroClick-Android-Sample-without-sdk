package com.simpl.pay.sample.zc_s2s;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SuccessActivity extends AppCompatActivity {
    @BindView(R.id.tvTransactionSuccess)
    TextView tvTransactionSuccess;


    private String token = "";

    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        tvTransactionSuccess.setVisibility(View.VISIBLE);
    }

    public String getToken() {
        return token;
    }

    @OnClick(R.id.bOkay)
    void onButtonOkayClick() {
        finish();
    }
}