package com.scbdev.stone_plugin.channel;

import android.util.Log;
import android.os.Handler;
import android.os.Looper;

import com.scbdev.stone_plugin.domain.interfaces.IStoneGateway;
import com.scbdev.stone_plugin.domain.interfaces.IStonePaymentListener;

import io.flutter.plugin.common.EventChannel;
public class PaymentEventChannelHandler implements EventChannel.StreamHandler,
        IStonePaymentListener  {
    private EventChannel.EventSink eventSink;

    IStoneGateway stoneGateway;

    public PaymentEventChannelHandler(IStoneGateway stoneGateway){
        this.stoneGateway = stoneGateway;
    }

    @Override
    public void onListen(Object arguments, EventChannel.EventSink events) {
        this.eventSink = events;
        stoneGateway.setStatusListener(this);
        Log.i("PaymentEventChannelHandler", "onListen");
    }

    @Override
    public void onCancel(Object arguments) {
        this.eventSink = null;
        stoneGateway.abortPayment();
        stoneGateway.setStatusListener(null); // ← falta isso
        Log.i("PaymentEventChannelHandler", "onCancel");
    }

    Handler mainHandler = new Handler(Looper.getMainLooper());
    @Override
    public void onStatusChanged(String status) {

        if (eventSink != null) {
            mainHandler.post(() -> eventSink.success(status));
        }
        Log.i("PaymentEventChannelHandler", "onStatusChanged");
    }
}
