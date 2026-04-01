package com.scbdev.stone_plugin;

import android.util.Log;
import android.os.Handler;
import android.os.Looper;

import io.flutter.plugin.common.EventChannel;
public class PaymentEventChannelHandler implements EventChannel.StreamHandler,
        StoneManager.StonePaymentListener {

    private static PaymentEventChannelHandler instance;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public static synchronized PaymentEventChannelHandler getInstance() {
        if (instance == null) {
            instance = new PaymentEventChannelHandler();
        }
        return instance;
    }

    private EventChannel.EventSink eventSink;

    @Override
    public void onListen(Object arguments, EventChannel.EventSink events) {
        this.eventSink = events;
        StoneManager.getInstance().setStatusListener(this);
        Log.i("PaymentEventChannelHandler", "onListen");
    }

    @Override
    public void onCancel(Object arguments) {
        this.eventSink = null;
        StoneManager.getInstance().abortPayment();
        Log.i("PaymentEventChannelHandler", "onCancel");
    }

    @Override
    public void onStatusChanged(String status) {
        if (eventSink != null) {
            mainHandler.post(() -> eventSink.success(status));
        }
        Log.i("PaymentEventChannelHandler", "onStatusChanged");
    }
}
