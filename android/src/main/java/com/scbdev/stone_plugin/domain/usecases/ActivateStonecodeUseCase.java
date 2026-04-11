package com.scbdev.stone_plugin.domain.usecases;

import com.scbdev.stone_plugin.domain.callback.IStoneCallback;
import com.scbdev.stone_plugin.domain.interfaces.IStoneGateway;

public class ActivateStonecodeUseCase {
    private final IStoneGateway stoneGateway;

    public ActivateStonecodeUseCase(IStoneGateway stoneGateway) {
        this.stoneGateway = stoneGateway;
    }

    public void execute(String code,IStoneCallback callback) {
        stoneGateway.activateStonecode(code,callback);
    }
}
