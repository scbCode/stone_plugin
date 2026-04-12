package com.scbdev.stone_plugin;

import android.util.Log;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.scbdev.stone_plugin.channel.PaymentEventChannelHandler;
import com.scbdev.stone_plugin.domain.MethodEnum;
import com.scbdev.stone_plugin.domain.enums.MethodEnumFromText;
import com.scbdev.stone_plugin.domain.callback.IStoneCallback;
import com.scbdev.stone_plugin.domain.interfaces.IStoneGateway;
import com.scbdev.stone_plugin.domain.params.PaymentParams;
import com.scbdev.stone_plugin.domain.usecases.AbortPaymentUseCase;
import com.scbdev.stone_plugin.domain.usecases.ActivateStonecodeUseCase;
import com.scbdev.stone_plugin.domain.usecases.StoneInitUseCase;
import com.scbdev.stone_plugin.domain.usecases.StonePaymentUseCase;
import com.scbdev.stone_plugin.infra.StoneSdkAdapter;

import stone.user.UserModel;

import java.util.HashMap;
import java.util.concurrent.Executors;

import android.os.Handler;
import android.os.Looper;

import io.flutter.embedding.engine.plugins.FlutterPlugin;                      // ← ESSE ESTAVA FALTANDO
import io.flutter.embedding.engine.plugins.FlutterPlugin.FlutterPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.EventChannel;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;

public class StonePlugin implements FlutterPlugin, MethodCallHandler, ActivityAware {

    private MethodChannel channel;
    private EventChannel paymentEventChannel;
    private Context context;
    IStoneGateway stoneGateway;
    PaymentEventChannelHandler paymentEventChannelHandler;
    UserModel user;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    StoneInitUseCase stoneInitUseCase;
    ActivateStonecodeUseCase activateStonecodeUseCase;
    StonePaymentUseCase stonePaymentUseCase;
    AbortPaymentUseCase abortPaymentUseCase;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {

        context = flutterPluginBinding.getApplicationContext();

        if (stoneGateway == null) {
            stoneGateway = new StoneSdkAdapter(context,
                    Executors.newSingleThreadExecutor());
        }

        if (paymentEventChannelHandler == null) {
            paymentEventChannelHandler = new PaymentEventChannelHandler(stoneGateway);
        }

        stoneInitUseCase = new StoneInitUseCase(stoneGateway);
        activateStonecodeUseCase = new ActivateStonecodeUseCase(stoneGateway);
        stonePaymentUseCase = new StonePaymentUseCase(stoneGateway);
        abortPaymentUseCase = new AbortPaymentUseCase(stoneGateway);
        abortPaymentUseCase.execute();

        setupChannel(flutterPluginBinding);
    }


    public void setupChannel(FlutterPluginBinding flutterPluginBinding) {
        if (channel == null) {
            channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "stone_plugin");
            channel.setMethodCallHandler(this);
        }
        if (paymentEventChannel == null) {
            paymentEventChannel = new EventChannel(flutterPluginBinding.getBinaryMessenger(), "stone_plugin_stream_payment");
            paymentEventChannel.setStreamHandler(paymentEventChannelHandler);
        }
    }

    ///  MethodCall é uma classe que representa uma chamada de método do Flutter.
    /// https://api.flutter.dev/javadoc/io/flutter/plugin/common/MethodCall.html
    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        MethodEnum method = MethodEnumFromText.fromText(call.method);
        if (method.equals(MethodEnum.init)) {
            init(result);
            return;
        }
        if (method.equals(MethodEnum.payment)) {
            if (user == null) {
                mainHandler.post(() -> result.error("PAYMENT_ERROR", "Usuário não inicializado", null));
                return;
            }

            HashMap<String, String> map = (HashMap<String, String>) call.arguments();
            PaymentParams params = PaymentParams.fromMap(map);

            if (params == null) {
                mainHandler.post(() -> result.error("PAYMENT_ERROR", "Parâmetros inválidos", null));
                return;
            }

            payment(params);
            result.success("iniciando pagamento");
            return;
        }
        if (method.equals(MethodEnum.activateStonecode)) {
            String stoneCode = call.arguments();
            activateStonecode(result, stoneCode);
            return;
        }
    }

    void init(Result result) {
        stoneInitUseCase.execute(
                new IStoneCallback() {
                    @Override
                    public void onSuccess() {
                        mainHandler.post(() -> result.success("SDK Inicializado com sucesso"));
                    }


                    @Override
                    public void onError(String message) {
                        mainHandler.post(() ->
                                result.error("INIT_ERROR", message, null));

                    }

                    @Override
                    public void getUserModel(UserModel userModel) {
                        user = userModel;
                    }
                }
        );
    }

    void activateStonecode(Result result, String stoneCode) {
        activateStonecodeUseCase.execute(stoneCode, new IStoneCallback() {
            @Override
            public void onSuccess() {
                mainHandler.post(() -> result.success(true));
            }

            @Override
            public void onError(String message) {
                mainHandler.post(() -> result.error("STONECODE_ERROR", message, null));
            }

            @Override
            public void getUserModel(UserModel userModel) {
                user = userModel;
            }
        });
    }

    void payment(PaymentParams params) {
        stonePaymentUseCase.execute(
                params, user);
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        if (abortPaymentUseCase != null) abortPaymentUseCase.execute();
        if (channel != null) channel.setMethodCallHandler(null);
        if (paymentEventChannel != null) paymentEventChannel.setStreamHandler(null);
        if (stoneGateway != null) stoneGateway.setStatusListener(null);
        stoneGateway = null;
        channel = null;
        paymentEventChannelHandler = null;
    }

    @Override
    public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
        if (abortPaymentUseCase != null) abortPaymentUseCase.execute();
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {
        if (abortPaymentUseCase != null) abortPaymentUseCase.execute();
    }

    @Override
    public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
    }

    @Override
    public void onDetachedFromActivity() {
        if (abortPaymentUseCase != null) abortPaymentUseCase.execute();
    }

}
