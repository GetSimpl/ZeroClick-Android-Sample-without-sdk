package com.simpl.pay.sample.zc_s2s.utils;

public class Utils {
    public static String getChargePayload(String phoneNo, int amount, String verificationID, String otp){
        String payload = "{\n" +
                " \"verification_id\": \"" + verificationID + "\",\n" +
                " \"otp\": \"" + otp + "\",\n" +
                " \"amount_in_paise\": " + amount + ",\n" +
                " \"phone_number\": " + phoneNo +
                "\n}";

        return payload;
    }

    public static String getZCChargePayload(String phoneNo, int amount){
        String payload = "{\n" +
                " \"amount_in_paise\": " + amount + ",\n" +
                " \"phone_number\": " + phoneNo +
                "\n}";

        return payload;
    }
}
