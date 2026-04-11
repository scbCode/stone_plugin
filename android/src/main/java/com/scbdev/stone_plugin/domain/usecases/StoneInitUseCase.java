package com.scbdev.stone_plugin.domain.usecases;

import com.scbdev.stone_plugin.domain.callback.IStoneCallback;
import com.scbdev.stone_plugin.domain.interfaces.IStoneGateway;

public class StoneInitUseCase {

    private final IStoneGateway stoneGateway;

    public StoneInitUseCase(IStoneGateway stoneGateway) {
        this.stoneGateway = stoneGateway;
    }

    public void execute(IStoneCallback callback) {
        stoneGateway.init(callback);
    }
}
