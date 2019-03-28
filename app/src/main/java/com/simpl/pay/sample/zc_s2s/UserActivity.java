package com.simpl.pay.sample.zc_s2s;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.simpl.pay.sample.zc_s2s.utils.BaseApi;
import com.simpl.pay.sample.zc_s2s.utils.Intents;
import com.simpl.pay.sample.zc_s2s.utils.Urls;

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

public class UserActivity extends AppCompatActivity {

    private final String TAG = UserActivity.class.getSimpleName();
    public String merchantId = "7ce1b079ff105587ad535a5f2c93fcc1";
    public boolean hasToken = false;

    @BindView(R.id.etPhoneNo)
    EditText etPhoneNo;

    @BindView(R.id.etEmail)
    EditText etEmail;

    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.bProceed)
    public void onClickProceed() {
        if (etPhoneNo.getText().toString().length() > 0)
            if (etEmail.getText().toString().length() > 0) {
                String hasTokenPayload = "{\"phone_number\":\""+ etPhoneNo.getText() + "\"}";
                RequestBody body = BaseApi.createRequestBody(hasTokenPayload);
                Request request = BaseApi.buildPOSTRequest(body, Urls.HAS_TOKEN);

                BaseApi.executeRequest(request, new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()){
                            try {
                                JSONObject res = new JSONObject(response.body().string());
                                if (res.getBoolean("success"))
                                    hasToken = true;

                                Intent cartIntent = Intents.getCartActivityIntent(context);
                                cartIntent.putExtra("merchantId", merchantId);
                                cartIntent.putExtra("phone_no", etPhoneNo.getText().toString());
                                cartIntent.putExtra("email", etEmail.getText().toString());
                                cartIntent.putExtra("hasToken", hasToken);
                                startActivity(cartIntent);
                            } catch (Exception ex){
                                Log.e(TAG, "" + ex);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e(TAG, "" + e);
                    }

                });
            } else Toast.makeText(UserActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(UserActivity.this, "Enter phone number", Toast.LENGTH_SHORT).show();

    }
}
