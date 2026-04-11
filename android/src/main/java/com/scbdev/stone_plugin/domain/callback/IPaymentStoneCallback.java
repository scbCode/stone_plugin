package com.scbdev.stone_plugin.domain.callback;

public interface IPaymentStoneCallback {
    void onSuccess();
    void onError(String message);

}