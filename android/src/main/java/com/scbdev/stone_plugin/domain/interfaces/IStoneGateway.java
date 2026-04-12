package com.scbdev.stone_plugin.domain.interfaces;

import com.scbdev.stone_plugin.domain.callback.IStoneCallback;
import com.scbdev.stone_plugin.domain.params.PaymentParams;
import stone.user.UserModel;

public interface IStoneGateway {
    void init(IStoneCallback callback);

    void activateStonecode(String stoneCode, IStoneCallback callback);

    void payment(PaymentParams params, UserModel userModel);

    void abortPayment();

    public void setStatusListener(IStonePaymentListener listener);

}
