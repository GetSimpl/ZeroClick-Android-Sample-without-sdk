package com.simpl.pay.sample.zc_s2s;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.simpl.pay.sample.zc_s2s.objects.ItemObject;
import com.simpl.pay.sample.zc_s2s.utils.Intents;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static java.lang.String.format;


public class CartActivity extends AppCompatActivity implements View.OnClickListener {
    ArrayList<ItemObject> content = new ArrayList<>();

    @BindView(R.id.tvAmount)
    TextView tvAmount;

    @BindView(R.id.tvProduct1)
    TextView tvProduct1;

    @BindView(R.id.tvProduct2)
    TextView tvProduct2;

    @BindView(R.id.tvQuantity1)
    TextView tvQuantity1;

    @BindView(R.id.tvQuantity2)
    TextView tvQuantity2;

    @BindView(R.id.tvItemAmount1)
    TextView tvItemAmount1;

    @BindView(R.id.tvItemAmount2)
    TextView tvItemAmount2;

    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ButterKnife.bind(this);
        init();
        updateUI();
    }

    private void init() {
        content.add(new ItemObject("Awesome Product 1", 1, 100000));
        content.add(new ItemObject("Awesome Product 2", 2, 500000));

        findViewById(R.id.bQty1Minus).setOnClickListener(this);
        findViewById(R.id.bQty1Plus).setOnClickListener(this);
        findViewById(R.id.bQty2Minus).setOnClickListener(this);
        findViewById(R.id.bQty2Plus).setOnClickListener(this);
    }

    private void updateUI() {
        tvProduct1.setText(content.get(0).getName());
        tvProduct2.setText(content.get(1).getName());

        tvQuantity1.setText(String.valueOf(content.get(0).getQuantity()));
        tvQuantity2.setText(String.valueOf(content.get(1).getQuantity()));

        tvItemAmount1.setText(format("Rs. %d", content.get(0).getTotalItemPrice() / 100));
        tvItemAmount2.setText(format("Rs. %d", content.get(1).getTotalItemPrice() / 100));

        tvAmount.setText(format("Rs. %d", getTotalAmount() / 100));
    }


    private int getTotalAmount() {
        int sum_amount = 0;

        for (ItemObject object : content) {
            sum_amount += object.getTotalItemPrice();
        }
        return sum_amount;
    }

    @OnClick(R.id.bProceed)
    public void onClickProceed() {
        if (getTotalAmount() == 0){
            Toast.makeText(this, "You need to add atleast one item in the cart", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent payIntent = Intents.getPaymentActivityIntent(context);
        payIntent.putExtra("amount", getTotalAmount());
        payIntent.putExtra("merchantId", getIntent().getStringExtra("merchantId"));
        payIntent.putExtra("phone_no", getIntent().getStringExtra("phone_no"));
        payIntent.putExtra("email", getIntent().getStringExtra("email"));
        payIntent.putExtra("hasToken", getIntent().getBooleanExtra("hasToken", false));
        startActivity(payIntent);
        finish();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bQty1Minus:
                if (content.get(0).getQuantity() > 0) content.get(0).subQuantity();
                break;
            case R.id.bQty1Plus:
                content.get(0).addQuantity();
                break;
            case R.id.bQty2Minus:
                if (content.get(1).getQuantity() > 0) content.get(1).subQuantity();
                break;
            case R.id.bQty2Plus:
                content.get(1).addQuantity();
                break;
        }
        updateUI();
    }
}
