package com.simpl.pay.sample.zc_s2s.utils;

import android.content.Context;
import android.content.Intent;

import com.simpl.pay.sample.zc_s2s.CartActivity;
import com.simpl.pay.sample.zc_s2s.OTPActivity;
import com.simpl.pay.sample.zc_s2s.PaymentActivity;
import com.simpl.pay.sample.zc_s2s.SuccessActivity;

public class Intents {

    public static Intent getCartActivityIntent(Context context) {
        return new Intent(context, CartActivity.class);
    }

    public static Intent getPaymentActivityIntent(Context context) {
        return new Intent(context, PaymentActivity.class);
    }

    public static Intent getSuccessActivityIntent(Context context) {
        return new Intent(context, SuccessActivity.class);
    }

    public static Intent getOTPActivityIntent(Context context) {
        return new Intent(context, OTPActivity.class);
    }
}
