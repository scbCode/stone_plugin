package com.scbdev.stone_plugin;

import android.content.Context;

import androidx.annotation.NonNull;

import com.scbdev.stone_plugin.interfaces.StoneCallback;

import android.util.Log;

import java.util.HashMap;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.FlutterPlugin.FlutterPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.EventChannel.EventSink;
import io.flutter.plugin.common.BinaryMessenger;

import com.scbdev.stone_plugin.params.PaymentParams;

/**
 * StonePlugin
 */
public class StonePlugin implements FlutterPlugin, MethodCallHandler {

    private MethodChannel channel;
    private EventChannel paymentEventChannel;
    private Context context;
    private String pluginName = "stone_plugin";

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {

        context = flutterPluginBinding.getApplicationContext();

        /// Canal de comunicação com o Flutter
        channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), pluginName);
        channel.setMethodCallHandler(this);

        /// Canal Stream de eventos com Flutter
        paymentEventChannel = new EventChannel(flutterPluginBinding.getBinaryMessenger(), "stone_plugin_stream_payment");
        paymentEventChannel.setStreamHandler(PaymentEventChannelHandler.getInstance());

    }

    ///  MethodCall é uma classe que representa uma chamada de método do Flutter.
    /// https://api.flutter.dev/javadoc/io/flutter/plugin/common/MethodCall.html
    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        MethodEnum method = MethodEnumFromText.fromText(call.method);
        if (method.equals(MethodEnum.init)) {
            init(result);
        }
        if (method.equals(MethodEnum.printReceipt)) {
            printReceipt(result);
        }
        if (method.equals(MethodEnum.payment)) {
            payment(call.arguments());
            result.success("iniciando pagamento");
        }
        if (method.equals(MethodEnum.activateStonecode)) {
            String stoneCode = call.arguments();
            activateStonecode(result, stoneCode);
        }
    }

    void init(Result result) {
        StoneManager.getInstance().init(context, new StoneCallback() {
            @Override
            public void onSuccess() {
                result.success("SDK Inicializado com sucesso");
            }

            @Override
            public void onError(String message) {
                result.error("INIT_ERROR", message, null);
            }
        });
    }

    void printReceipt(Result result) {
        StoneManager.getInstance().printReceipt(context, new StoneCallback() {
            @Override
            public void onSuccess() {
                result.success("Recibo impresso com sucesso");
            }

            @Override
            public void onError(String message) {
                result.error("PRINT_ERROR", message, null);
            }
        });
    }

    void activateStonecode(Result result, String stoneCode) {
        StoneManager.getInstance().
                activateStonecode(context, stoneCode, new StoneCallback() {
                    @Override
                    public void onSuccess() {
                        result.success(true);
                    }

                    @Override
                    public void onError(String message) {
                        result.error("STONECODE_ERROR", message, null);
                    }
                });
    }

    void payment(HashMap<String, String> arguments) {
        StoneManager.getInstance().payment(context,
                PaymentParams.fromMap(arguments));
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }
}
