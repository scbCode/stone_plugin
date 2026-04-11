package com.scbdev.stone_plugin.domain.callback;
import stone.user.UserModel;
import stone.utils.Stone;
import stone.application.StoneStart;
public interface IStoneCallback {
    void onSuccess();
    void onError(String message);

    void getUserModel(UserModel userModel);

}