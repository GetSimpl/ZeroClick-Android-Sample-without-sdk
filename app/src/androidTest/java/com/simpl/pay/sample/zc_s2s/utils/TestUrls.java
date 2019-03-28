package com.simpl.pay.sample.zc_s2s.utils;

public class TestUrls {
    private static final boolean isSandbox = false;

    private static final String BASE_URL_SANDBOX = "https://sandbox-api.getsimpl.com/api/v1";
    private static final String BASE_URL_STAGING = "https://staging-api.getsimpl.com/api/v1";

    private static String getBaseURL() {
        return isSandbox ? BASE_URL_SANDBOX : BASE_URL_STAGING;
    }

    public static final String APPROVEDUSER_WITHOTP = getBaseURL() + "/integration_test/first_transaction";
    public static final String LIMIT_SPENDING_AMOUNT = getBaseURL() + "/integration_test/limit_spending_amount";
    public static final String APPROVEDUSER_BILLPENDING = getBaseURL() + "/integration_test/limit_not_allowed";
    public static final String OTP_BY_VERIFICATION_ID = getBaseURL() + "/user_agents/otp?verification_id=";
    public static final String TEARDOWN_USER = getBaseURL() + "/integration_test/complete_teardown";

    public static final String ELIGIBILITY_TRANSACTION_URL = "https://staging-zero-click-api.getsimpl.com/api/v3/transactions/check_eligibility";
    public static final String CHARGE_TRANSACTION_URL = "https://staging-zero-click-api.getsimpl.com/api/v3/transactions";


}
