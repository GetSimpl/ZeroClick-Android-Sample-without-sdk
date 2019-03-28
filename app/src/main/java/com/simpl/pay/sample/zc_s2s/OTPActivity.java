package com.simpl.pay.sample.zc_s2s;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class OTPActivity extends AppCompatActivity {
    private String phoneNo = "";
    private String verificationID = "";
    private int amount;
    private final String TAG = OTPActivity.class.getSimpleName();

    @BindView(R.id.otp)
    EditText otp;

    @BindView(R.id.verifyOtpButton)
    Button verifyButton;

    @BindView(R.id.resend_otp)
    TextView resendOtp;

    @BindView(R.id.verify_id)
    TextView verifyId;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        ButterKnife.bind(this);
        fetchData();
        initOtp();
    }

    private void initOtp() {
        String requestZCPayload = "{\"phone_number\": \"" + phoneNo + "\"}";
        RequestBody body = BaseApi.createRequestBody(requestZCPayload);
        Request request = BaseApi.buildPOSTRequest(body, Urls.REQUEST_SIMPL_TOKEN);

        BaseApi.executeRequest(request, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    try {
                        JSONObject res = new JSONObject(response.body().string());
                        if (res.getBoolean("success")){
                            JSONObject data = res.getJSONObject("data");
                            verificationID = data.getString("verification_id");
                            runOnUiThread(() -> {
                                verifyId.setText(verificationID);
                                verifyButton.setVisibility(View.VISIBLE);
                                // See if you can make this better
                                resendOtp.setVisibility(View.VISIBLE);
                                Toast.makeText(OTPActivity.this, "OTP successfully sent", Toast.LENGTH_SHORT).show();
                            });
                        } else {
                            runOnUiThread(() -> Toast.makeText(OTPActivity.this, "There was an error in generating the OTP", Toast.LENGTH_SHORT));
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                        Log.e(TAG, "" + e);
                    }
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                // Error occurred, handle it gracefully
                Log.e(TAG, "" + e);
            }

        });
    }

    private void fetchData() {
        phoneNo = getIntent().getStringExtra("phone_no");
        amount = getIntent().getIntExtra("amount", 1);
    }

    @OnClick(R.id.resend_otp)
    public void onClickResendOTP() {
        String resendOTPPayload = "{\"verification_id\": \"" + verificationID + "\"}";
        RequestBody body = BaseApi.createRequestBody(resendOTPPayload);
        Request request = BaseApi.buildPOSTRequest(body, Urls.REQUEST_SIMPL_TOKEN);

        BaseApi.executeRequest(request, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    try {
                        JSONObject res = new JSONObject(response.body().string());
                        if (res.getBoolean("success")){
                            verificationID = res.getString("verification_id");
                            runOnUiThread(() -> Toast.makeText(OTPActivity.this, "OTP successfully resent.", Toast.LENGTH_SHORT).show());
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {

            }
        });
    }

    @OnClick(R.id.verifyOtpButton)
    public void onClickVerifyButton() {
        String placeOrderPayload = Utils.getChargePayload(phoneNo, amount, verificationID, otp.getText().toString());
        RequestBody body = BaseApi.createRequestBody(placeOrderPayload);
        Request request = BaseApi.buildPOSTRequest(body, Urls.PLACE_SIMPL_ORDER);

        BaseApi.executeRequest(request, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject res = new JSONObject(response.body().string());
                        if (res.getBoolean("success")) {
                            startActivity(new Intent(OTPActivity.this, SuccessActivity.class)
                                    .putExtra("isFirstTime", true));
                            finish();
                        } else {
                            String error_code = res.getString("error_code");
                            String redirection_url = res.getString("redirection_url");
                            runOnUiThread(() -> {
                                Toast.makeText(OTPActivity.this, "Your trancsaction was unsuccessfull " + error_code, Toast.LENGTH_SHORT).show();
                                if (redirection_url != null && !redirection_url.equals("null")) {
                                    Intent webViewIntent = new Intent(OTPActivity.this, WebViewActivity.class);
                                    webViewIntent.putExtra("redirection_url", redirection_url);
                                    startActivity(webViewIntent);
                                }
                            });
                        }
                    } catch (Exception ex){
                        Log.e(TAG, "" + ex);
                    }
                }else {
                    // TODO think about what can be done here
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {

            }
        });
    }
}
