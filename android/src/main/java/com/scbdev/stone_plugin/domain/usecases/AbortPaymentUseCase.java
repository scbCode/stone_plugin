package com.scbdev.stone_plugin.domain.usecases;

import com.scbdev.stone_plugin.domain.interfaces.IStoneGateway;

public class AbortPaymentUseCase {

    private final IStoneGateway stoneGateway;

    public AbortPaymentUseCase(IStoneGateway stoneGateway) {
        this.stoneGateway = stoneGateway;
    }

    public void execute() {
        stoneGateway.abortPayment();
    }
}
