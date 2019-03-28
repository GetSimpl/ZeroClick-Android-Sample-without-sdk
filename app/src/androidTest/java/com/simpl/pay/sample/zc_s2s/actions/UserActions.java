package com.simpl.pay.sample.zc_s2s.actions;


import com.simpl.pay.sample.zc_s2s.R;
import com.simpl.pay.sample.zc_s2s.objects.User;
import com.simpl.pay.sample.zc_s2s.utils.TestUtils;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class UserActions {

    public UserActions fillUserData(User user) {
        TestUtils.wait(1000);
        onView(withId(R.id.etPhoneNo))
                .perform(typeText(user.getPhoneNumber()));

        TestUtils.wait(500);
        onView(withId(R.id.etEmail))
                .perform(typeText(user.getEmailId()), closeSoftKeyboard());

        return this;
    }

    public void proceedToCart() {
        TestUtils.wait(1000);
        onView(withId(R.id.bProceed)).perform(click());
    }
}
