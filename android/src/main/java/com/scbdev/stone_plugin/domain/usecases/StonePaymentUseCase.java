package com.scbdev.stone_plugin.domain.usecases;

import com.scbdev.stone_plugin.domain.interfaces.IStoneGateway;
import com.scbdev.stone_plugin.domain.params.PaymentParams;
import stone.user.UserModel;

public class StonePaymentUseCase {

    private final IStoneGateway stoneGateway;

    public StonePaymentUseCase(IStoneGateway stoneGateway) {
        this.stoneGateway = stoneGateway;
    }

    public void execute(PaymentParams params, UserModel userModel) {
        stoneGateway.payment(
                params,
                userModel);
    }
}
