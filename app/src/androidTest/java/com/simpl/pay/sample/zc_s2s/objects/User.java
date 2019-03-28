package com.simpl.pay.sample.zc_s2s.objects;

public class User {
    private String emailId;
    private String phoneNumber;
    private String merchantId;
    private String merchantSecret;

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantSecret() {
        return merchantSecret;
    }

    public void setMerchantSecret(String merchantSecret) {
        this.merchantSecret = merchantSecret;
    }
}