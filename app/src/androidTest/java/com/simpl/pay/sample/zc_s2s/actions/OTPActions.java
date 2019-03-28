package com.simpl.pay.sample.zc_s2s.actions;

import android.view.View;
import android.widget.TextView;

import com.simpl.pay.sample.zc_s2s.R;
import com.simpl.pay.sample.zc_s2s.network.NetworkHelper;
import com.simpl.pay.sample.zc_s2s.utils.TestUtils;

import org.hamcrest.Matcher;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class OTPActions {
    private String verification_id = "";
    private String otp = "";

    public OTPActions () {
        TestUtils.wait(2000);
    }

    public OTPActions fetchVerificationId() {
        verification_id = (String) getText(withId(R.id.verify_id));
        return this;
    }

    public OTPActions fillOTP() {
        try {
            TestUtils.wait(1000);
            otp = NetworkHelper.getOTPFromVerificationId(verification_id);
            onView(withId(R.id.otp))
                    .perform(typeText(otp), closeSoftKeyboard());
        } catch (Exception e){
            e.printStackTrace();
        }

        return this;
    }

    public void cilckVerifyOTP() {
        TestUtils.wait(2000);
        onView(withId(R.id.verifyOtpButton))
                .perform(click());
    }


    private String getText(final Matcher<View> matcher) {
        final String[] stringHolder = { null };
        onView(matcher).perform(new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isAssignableFrom(TextView.class);
            }

            @Override
            public String getDescription() {
                return "getting text from a TextView";
            }

            @Override
            public void perform(UiController uiController, View view) {
                TextView tv = (TextView) view;
                stringHolder[0] = tv.getText().toString();
            }
        });
        return stringHolder[0];
    }

}
