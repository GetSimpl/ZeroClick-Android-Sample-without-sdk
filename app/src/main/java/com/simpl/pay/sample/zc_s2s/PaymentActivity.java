package com.simpl.pay.sample.zc_s2s;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.simpl.android.fingerprint.SimplFingerprint;
import com.simpl.pay.sample.zc_s2s.utils.BaseApi;
import com.simpl.pay.sample.zc_s2s.utils.Urls;
import com.simpl.pay.sample.zc_s2s.utils.Utils;

import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PaymentActivity extends AppCompatActivity {
    private WebView webView;
    private String email = "";
    private String phoneNo = "";
    private String merchantId = "";
    private Boolean hasToken = false;
    String zeroClickToken = "";
    private int amount = 0;
    private static String TAG = PaymentActivity.class.getSimpleName();


    @BindView(R.id.tvAmount)
    TextView tvAmount;

    @BindView(R.id.tvStatus)
    TextView tvStatus;

    @BindView(R.id.tvSimplPay)
    TextView tvSimplPay;

    @BindView(R.id.llSimplPay)
    LinearLayout llSimplPay;

    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);
        fetchData();
        updateUI();
        initSimpl();

    }

    private void initSimpl() {
        if (!hasToken)
            callApproval();
        else
            callEligibility();
    }

    private void fetchData() {
        email = getIntent().getStringExtra("email");
        phoneNo = getIntent().getStringExtra("phone_no");
        merchantId = getIntent().getStringExtra("merchant_id");
        hasToken = getIntent().getBooleanExtra("hasToken", false);
        amount = getIntent().getIntExtra("amount", 0);
    }

    private void updateUI() {
        tvAmount.setText(String.format("Rs. %d", amount / 100));
    }

    void callApproval() {
        if (amount == 0) return;

        SimplFingerprint.init(this, phoneNo, email);
        SimplFingerprint.getInstance().generateFingerprint(payload -> {
            String approvalPayload = "{\"phone_number\": \"" + phoneNo + "\",\"payload\": \""+ payload +"\",\"transaction_amount_in_paisa\":" + amount + "}";
            RequestBody body = BaseApi.createRequestBody(approvalPayload);
            Request approvalRequest = BaseApi.buildPOSTRequest(body, Urls.CHECK_APPROVAL);

            BaseApi.executeRequest(approvalRequest, new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()){
                        try {
                            JSONObject resObj = new JSONObject(response.body().string());
                            if (resObj.getBoolean("approved")){
                                runOnUiThread(() -> {
                                    llSimplPay.setVisibility(View.VISIBLE);
                                    tvStatus.setText("Status: Approval Call successful.");
                                    tvSimplPay.setText("Link with Simpl");
                                });
                            } else {
                                runOnUiThread(() -> {
                                    //user not approved
                                    llSimplPay.setVisibility(View.GONE);
                                    tvStatus.setText("Status: Approval Call Failed");
                                    tvStatus.setTextColor(getColor(R.color.colorRed));
                                });
                            }
                        } catch (Exception ex){
                            Log.e(TAG, "" + ex);
                        }
                    } else {
                        Log.v(TAG, "Approval call has failed");
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    // Error occurred, handle it gracefully
                    Log.e(TAG, "" + e);
                }
            });
        }
        );
    }

    private void callEligibility() {
        // Call your server find out the eligibility to make a transaction.
        SimplFingerprint.init(PaymentActivity.this, phoneNo, email);
        SimplFingerprint.getInstance().generateFingerprint(payload -> {
            Log.v("Fingerprint", "Fingerprint Data: " + payload);
            String eligibilityPayload = "{\"phone_number\": \"" + phoneNo + "\",\"payload\": \""+ payload +"\",\"amount_in_paisa\":" + amount + "}";
            RequestBody body = BaseApi.createRequestBody(eligibilityPayload);
            Request request = BaseApi.buildPOSTRequest(body, Urls.ELIGIBILITY_CHECK);

            BaseApi.executeRequest(request, new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        try {
                            JSONObject responseObject = new JSONObject(response.body().string());
                            if (responseObject.getBoolean("success")) {
                                runOnUiThread(() -> {
                                    llSimplPay.setVisibility(View.VISIBLE);
                                    tvStatus.setText("Status: You have enough balance to make this transaction");
                                    tvSimplPay.setText("Pay with Simpl");
                                });
                            } else {
                                // If eligibility call fails
                                runOnUiThread(() -> onEligibilityFail(responseObject));
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "" + e);
                        }
                    } else {
                        Log.v("Error", "Eligibility did not succeed");
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    // Error occurred, handle it gracefully
                    Log.e(TAG, "" + e);
                }

            });

        });

    }

    private void performZCTransaction() {
        String chargePayload = Utils.getZCChargePayload(phoneNo, amount);
        RequestBody body = BaseApi.createRequestBody(chargePayload);
        Request request = BaseApi.buildPOSTRequest(body, Urls.CHARGE_USER);

        BaseApi.executeRequest(request, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject res = new JSONObject(response.body().string());
                        if (res.getBoolean("success")) {
                            startActivity(new Intent(PaymentActivity.this, SuccessActivity.class)
                                    .putExtra("isFirstTime", false));
                            finish();
                        }else
                            runOnUiThread(() -> onEligibilityFail(res));
                    } catch (Exception ex){
                        Log.e(TAG, "" + ex);
                    }
                }else {
                    Log.v("Error", "Transaction did not succeed");
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {

            }
        });
    }

    @OnClick(R.id.llSimplPay)
    public void onClickSimplPay() {
        Log.d(TAG, "Simpl Pay button clicked.");

        if (!hasToken){
            startActivity(new Intent(PaymentActivity.this, OTPActivity.class)
                    .putExtra("phone_no", phoneNo)
                    .putExtra("amount", amount));
            finish();
        } else
            performZCTransaction();
    }

    private void onEligibilityFail (JSONObject responseObject) {
        try {
            // If the Eligibility returns false, navigate to redirection URL.
            // This will allow user to pay his outstanding dues and on success callback, you can initiate the charge call on server.
            if (responseObject.getString("redirection_url") != null && !responseObject.getString("redirection_url").equals("null")) {
                Intent webViewIntent = new Intent(PaymentActivity.this, WebViewActivity.class);
                webViewIntent.putExtra("redirection_url", responseObject.getString("redirection_url"));
                startActivity(webViewIntent);
            }

            //update the UI.
            String error_code = responseObject.getString("error_code");
            switch (error_code) {
                case "pending_dues":
                    tvStatus.setText("Status: You have a pending bill");
                    tvStatus.setTextColor(getColor(R.color.colorRed));
                    break;
                case "unable_to_process":
                    tvStatus.setText("Status: Transaction amount is greater than your credit limit");
                    tvStatus.setTextColor(getColor(R.color.colorRed));
                    break;
                case "user_unauthorized":
                    tvStatus.setText("Status: You have been blocked on simpl");
                    tvStatus.setTextColor(getColor(R.color.colorRed));
                    // As the user is blocked you can also delete user zeroClickToken in database
                    break;
                default:
                    tvStatus.setText("Status: You don't have enough credit for this transaction");
                    tvStatus.setTextColor(getColor(R.color.colorRed));
                    break;
            }
        }catch (Exception exception) {
            Log.e("SIMPLSDK", "" + exception);
        }

    }
}
