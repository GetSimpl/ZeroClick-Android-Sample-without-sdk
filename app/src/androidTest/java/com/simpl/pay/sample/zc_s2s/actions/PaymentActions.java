package com.simpl.pay.sample.zc_s2s.actions;


import com.simpl.pay.sample.zc_s2s.R;
import com.simpl.pay.sample.zc_s2s.utils.Status;
import com.simpl.pay.sample.zc_s2s.utils.TestUtils;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;

public class PaymentActions {

    public PaymentActions checkApprovalSuccess() {
        TestUtils.wait(4000);
        onView(withId(R.id.llSimplPay)).check(matches(isDisplayed()));
        return this;
    }

    public void checkApprovalFail() {
        TestUtils.wait(2000);
        onView(withId(R.id.llSimplPay)).check(matches(not(isDisplayed())));
    }

    public PaymentActions checkStatus(Status errEnum) {
        TestUtils.wait(2000);
        onView(withId(R.id.llSimplPay)).check(matches(isDisplayed()));
        onView(withId(R.id.tvStatus)).check(matches(withText(getErrorStatus(errEnum))));
        return this;
    }


    private String getErrorStatus(Status errEnum){
        switch (errEnum) {
            case ELIGIBILITY_FAIL:
                return "Status: Transaction amount is greater than your credit limit";
            case ELIGIBILITY_SUCCESS:
                return "Status: You have enough balance to make this transaction";
            case PENDING_BILL:
                return "Status: You have a pending bill";
            case NOT_ENOUGHT_CREDIT:
                return "Status: You don't have enough credit for this transaction";
            default:
                return "";
        }
    }

    public PaymentActions checkSimplButtonText(String buttontext) {
        onView(withId(R.id.tvSimplPay)).check(matches(withText(buttontext)));
        return this;
    }

    public void clickSimplButton() {
        TestUtils.wait(1000);
        onView(withId(R.id.llSimplPay)).perform(click());
    }

}
