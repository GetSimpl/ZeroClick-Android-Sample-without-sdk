package com.simpl.pay.sample.zc_s2s.actions;

import com.simpl.pay.sample.zc_s2s.R;
import com.simpl.pay.sample.zc_s2s.utils.TestUtils;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


public class CartActions {

    public CartActions adjustCartItems (int amount) {
        int total = 20000;

        if (total > amount) {
            while (total > amount) {
                TestUtils.wait(200);
                if (amount < 10000) {
                    onView(withId(R.id.bQty2Minus)).perform(click());
                    total -= 500;
                }
                else {
                    onView(withId(R.id.bQty1Minus)).perform(click());
                    total -= 10000;
                }
            }
            return this;
        }


        while (total < amount) {
            TestUtils.wait(200);
            if (amount < 10000) {
                onView(withId(R.id.bQty2Plus)).perform(click());
                total += 500;
            }
            else {
                onView(withId(R.id.bQty1Plus)).perform(click());
                total += 10000;
            }
        }
        return this;
    }

    public void proceedToPayment() {
        TestUtils.wait(1000);
        onView(withId(R.id.bProceed)).perform(click());
    }
}
