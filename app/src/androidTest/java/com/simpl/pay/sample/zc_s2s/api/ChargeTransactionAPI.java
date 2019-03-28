package com.simpl.pay.sample.zc_s2s.api;

import com.simpl.pay.sample.zc_s2s.utils.TestUrls;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.simpl.pay.sample.zc_s2s.api.BaseApi.createRequestBody;
import static com.simpl.pay.sample.zc_s2s.api.BaseApi.executeRequest;

public class ChargeTransactionAPI {
    private static final String TAG = ChargeTransactionAPI.class.getSimpleName();

    public static JSONObject postChargeTransaction(String transactionToken, String mMerchantSecret, String amount) {

        String chargeJson = getChargeJson(amount, transactionToken);
        RequestBody chargeTransactionRequestbody = createRequestBody(chargeJson);
        Request chargeRequest = BaseApi.buildPOSTRequest(chargeTransactionRequestbody,
                TestUrls.CHARGE_TRANSACTION_URL, mMerchantSecret);

        Response chargeTransactionResponse;
        try {
            chargeTransactionResponse = executeRequest(chargeRequest);
            return new JSONObject(chargeTransactionResponse.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    private static String getChargeJson(String amount, String transactionToken) {
        String payload = "{\n" +
                "  \"amount_in_paise\": " + amount + ",\n" +
                "  \"items\": [\n" +
                "    {\n" +
                "      \"sku\": \"some unique id\",\n" +
                "      \"quantity\": \"12\",\n" +
                "      \"rate_per_item\": \"1200\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"shipping_address\": {\n" +
                "    \"line1\": \"811, Crescent Business Park\",\n" +
                "    \"line2\": \"Near Telephone exchange\",\n" +
                "    \"city\": \"Mumbai\",\n" +
                "    \"state\": \"Maharashtra\",\n" +
                "    \"country\": \"India\"\n" +
                "  },\n" +
                "  \"billing_address\": {\n" +
                "    \"line1\": \"811, Crescent Business Park\",\n" +
                "    \"line2\": \"Near Telephone exchange\",\n" +
                "    \"city\": \"Mumbai\",\n" +
                "    \"state\": \"Maharashtra\",\n" +
                "    \"country\": \"India\"\n" +
                "  },\n" +
                "  \"discount_in_paise\": 2000,\n" +
                "  \"shipping_amount_in_paise\": 200,\n" +
                "  \"metadata\": {\n" +
                "    \"inventory_partner\": {\n" +
                "      \"ip_id\": \"123\",\n" +
                "      \"ip_name\": \"abc\",\n" +
                "      \"ip_phone_number\": \"123\",\n" +
                "      \"ip_email\": \"a@gmail.com\",\n" +
                "      \"ip_address\": \"M.G. Road, Mumbai\",\n" +
                "      \"ip_ltln\": \"12.9748295 77.6469903\"\n" +
                "    },\n" +
                "    \"service_partner\": {\n" +
                "      \"sp_id\": \"123\",\n" +
                "      \"sp_name\": \"abc\",\n" +
                "      \"sp_phone_number\": \"123\",\n" +
                "      \"sp_address\": \"M.G. Road, Mumbai\"\n" +
                "    },\n" +
                "    \"customer_id\": \"xyz\",\n" +
                "    \"email\": \"user@gmail.com\"\n" +
                "  },\n" +
                "  \"order_id\": \"CRX12311P1\",\n" +
                "  \"zero_click_token\": \"" + transactionToken + "\"\n" +
                "}";
        return payload;
    }
}
