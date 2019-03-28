package com.simpl.pay.sample.zc_s2s.actions;

import com.simpl.pay.sample.zc_s2s.R;
import com.simpl.pay.sample.zc_s2s.utils.TestUtils;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.TestCase.assertTrue;

public class SuccessActions {

    public void clickOk() {
        TestUtils.wait(3000);
        onView(withId(R.id.bOkay)).perform(click());
    }
}
