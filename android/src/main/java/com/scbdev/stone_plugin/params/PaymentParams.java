package com.scbdev.stone_plugin.params;

import java.util.HashMap;

public class PaymentParams {
    public String type;
    public String amount;
    public String stoneCode;

    public PaymentParams(String type, String amount, String stoneCode) {
        this.type = type;
        this.amount = amount;
        this.stoneCode = stoneCode;
    }

   static public PaymentParams fromMap(HashMap<String, String> map) {
        String type = (String) map.get("type");
        String amount = (String) map.get("amount");
        String stoneCode = (String) map.get("stoneCode");
        return new PaymentParams(type, amount, stoneCode);
    }

}
