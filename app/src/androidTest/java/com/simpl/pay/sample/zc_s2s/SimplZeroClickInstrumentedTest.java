package com.simpl.pay.sample.zc_s2s;

import android.util.Log;

import com.simpl.pay.sample.zc_s2s.actions.CartActions;
import com.simpl.pay.sample.zc_s2s.actions.OTPActions;
import com.simpl.pay.sample.zc_s2s.actions.PaymentActions;
import com.simpl.pay.sample.zc_s2s.actions.SuccessActions;
import com.simpl.pay.sample.zc_s2s.actions.UserActions;
import com.simpl.pay.sample.zc_s2s.actions.WebviewActions;
import com.simpl.pay.sample.zc_s2s.network.NetworkHelper;
import com.simpl.pay.sample.zc_s2s.objects.User;
import com.simpl.pay.sample.zc_s2s.utils.Status;
import com.simpl.pay.sample.zc_s2s.utils.TestUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class SimplZeroClickInstrumentedTest {
    private User user = null;
    private String zeroClickToken = "";

    @Rule
    public ActivityTestRule<UserActivity> mUserActivityRule = new ActivityTestRule<>(UserActivity.class);

    @Rule
    public TestName testName = new TestName();

    @Before
    public void setup() throws Exception {
        // reset zeroClickToken and fetch user
        zeroClickToken = "";
        user = NetworkHelper.createUser(testName.getMethodName());
        Log.d("User", "Phone No:" + user.getPhoneNumber());
        mUserActivityRule.getActivity().merchantId = user.getMerchantId();
    }


    @Test
    public void approvedUserLinking() {
        new UserActions()
                .fillUserData(user)
                .proceedToCart();

        TestUtils.wait(2000);
        new CartActions()
                .adjustCartItems(200)
                .proceedToPayment();

        new PaymentActions()
                .checkApprovalSuccess()
                .checkSimplButtonText("Link with Simpl")
                .clickSimplButton();

        new OTPActions()
                .fetchVerificationId()
                .fillOTP()
                .cilckVerifyOTP();

        new SuccessActions().clickOk();
    }

    @Test
    public void approvedUserSuccessfulZCTransaction() {
        approvedUserLinking();

        new UserActions()
                .proceedToCart();

        new CartActions()
                .proceedToPayment();

        new PaymentActions()
                .checkStatus(Status.ELIGIBILITY_SUCCESS)
                .checkSimplButtonText("Pay with Simpl")
                .clickSimplButton();

        new SuccessActions().clickOk();
    }

    @Test
    public void approvedUserLimitSpend() {
        approvedUserLinking();

        new UserActions()
                .proceedToCart();

        new CartActions()
                .adjustCartItems(15000)
                .proceedToPayment();

        TestUtils.wait(2000);
        new WebviewActions()
                .closeWebView();

        TestUtils.wait(2000);
        new PaymentActions()
                .checkStatus(Status.ELIGIBILITY_FAIL);
    }

    @Test
    public void approvedUserInsufficientCredit() {

        new UserActions()
                .fillUserData(user)
                .proceedToCart();

        new CartActions()
                .adjustCartItems(70000)
                .proceedToPayment();

        new PaymentActions()
                .checkApprovalSuccess()
                .clickSimplButton();

        new OTPActions()
                .fetchVerificationId()
                .fillOTP()
                .cilckVerifyOTP();

        TestUtils.wait(2000);
        new WebviewActions()
                .closeWebView();

        TestUtils.wait(2000);
        new PaymentActions()
                .checkStatus(Status.NOT_ENOUGHT_CREDIT);
    }

    @Test
    public void approvedUserPendingBill() {
        new UserActions()
                .fillUserData(user)
                .proceedToCart();

        new  CartActions()
                .adjustCartItems(10000)
                .proceedToPayment();

        new PaymentActions()
                .checkApprovalSuccess()
                .clickSimplButton();

        new OTPActions()
                .fetchVerificationId()
                .fillOTP()
                .cilckVerifyOTP();

        TestUtils.wait(2000);
        new WebviewActions()
                .closeWebView();

        TestUtils.wait(2000);
        new PaymentActions()
                .checkStatus(Status.PENDING_BILL);
    }

    @Test
    public void nonExistingUserLinking() {
        new UserActions()
                .fillUserData(user)
                .proceedToCart();

        new CartActions()
                .proceedToPayment();

        new PaymentActions()
                .checkApprovalFail();

    }

    //    public void blockedUserLinking() {
//        //cannot test this
//        new UserActions()
//                .fillUserData(user)
//                .proceedToCart();
//
//        new CartActions()
//                .proceedToPayment();
//
//        new PaymentActions()
//                .checkApprovalFail();
//    }


    @After
    public void teardown() throws Exception {
        //teardown user
        try {
            NetworkHelper.removeUser(user.getPhoneNumber());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("TEAR DOWN", "Tear down complete..");
    }
}