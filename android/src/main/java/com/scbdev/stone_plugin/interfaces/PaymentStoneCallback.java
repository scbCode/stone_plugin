package com.scbdev.stone_plugin.interfaces;

public interface PaymentStoneCallback {
    void onSuccess();
    void onError(String message);

}