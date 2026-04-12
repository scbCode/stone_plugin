package com.scbdev.stone_plugin.infra;

import android.content.Context;
import android.util.Log;

import com.scbdev.stone_plugin.BuildConfig;
import com.scbdev.stone_plugin.domain.callback.IStoneCallback;
import com.scbdev.stone_plugin.domain.interfaces.IStoneGateway;
import com.scbdev.stone_plugin.domain.interfaces.IStonePaymentListener;
import com.scbdev.stone_plugin.domain.params.PaymentParams;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import br.com.stone.posandroid.providers.PosPrintProvider;
import br.com.stone.posandroid.providers.PosTransactionProvider;
import stone.application.StoneStart;
import stone.application.enums.Action;
import stone.application.enums.InstalmentTransactionEnum;
import stone.application.enums.TypeOfTransactionEnum;
import stone.application.interfaces.StoneActionCallback;
import stone.application.interfaces.StoneCallbackInterface;
import stone.database.transaction.TransactionObject;
import stone.providers.ActiveApplicationProvider;
import stone.providers.BaseTransactionProvider;
import stone.utils.keys.StoneKeyType;
import stone.utils.Stone;
import stone.user.UserModel;

public class StoneSdkAdapter implements IStoneGateway {

    public StoneSdkAdapter(Context context, ExecutorService executor) {
        this.context = context;
        this.executor = executor;
    }

    private Context context;

    BaseTransactionProvider transactionProvider;

    ActiveApplicationProvider provider;

    public interface StonePaymentListener {
        void onStatusChanged(String status);
    }

    private StonePaymentListener listener;

    private ExecutorService executor;

    @Override
    public void init(IStoneCallback callback) {
        Map<StoneKeyType, String> stoneKeys = new HashMap<StoneKeyType, String>() {
            {
                String put = put(StoneKeyType.QRCODE_AUTHORIZATION, BuildConfig
                        .QRCODE_AUTHORIZATION);
                put(StoneKeyType.QRCODE_PROVIDERID, BuildConfig.QRCODE_PROVIDERID);
            }
        };
        // Chamada do método de inicialização do SDK
        executor.execute(
                () -> {
                    try {
                        final List<UserModel> userModelList = StoneStart.init(context, stoneKeys);
                        Stone.setAppName("NAME_APP");
                        callback.getUserModel(userModelList.get(0));
                        callback.onSuccess();
                    } catch (Exception e) {
                        callback.onError("Erro ao inicializar o SDK");
                    }
                });
    }

    public void setStatusListener(StonePaymentListener listener) {
        this.listener = listener;
    }

    @Override
    public void activateStonecode(String stoneCode, IStoneCallback callback) {
        provider = new ActiveApplicationProvider(context);
        provider.setConnectionCallback(new StoneCallbackInterface() {
            @Override
            public void onSuccess() {
                callback.onSuccess();
            }

            @Override
            public void onError() {
                callback.onError("Erro ao ativar Stonecode");
            }
        });
        executor.execute(
                () -> {
                    try {
                        provider.activate(stoneCode);
                    } catch (Exception e) {
                        callback.onError("Erro ao ativar Stonecode");
                    }
                });
    }

    @Override
    public void payment(PaymentParams params, UserModel userModel) {
        final TransactionObject transactionObject = new TransactionObject();
        transactionObject.setInstalmentTransaction(InstalmentTransactionEnum.ONE_INSTALMENT);

        if (params.type.equals("DEBIT_CARD"))
            transactionObject.setTypeOfTransaction(TypeOfTransactionEnum.DEBIT);

        if (params.type.equals("CREDIT_CARD"))
            transactionObject.setTypeOfTransaction(TypeOfTransactionEnum.CREDIT);

        transactionObject.setAmount(params.amount);

        transactionProvider = new PosTransactionProvider(context, transactionObject, userModel);

        transactionProvider.setConnectionCallback(new StoneActionCallback() {
            @Override
            public void onStatusChanged(Action action) {
                if (listener != null)
                    listener.onStatusChanged(action.name());

            }

            @Override
            public void onSuccess() {
                paymentReceipt(transactionObject);
                if (listener != null)
                    listener.onStatusChanged("PAYMENT_SUCCESS");
            }

            @Override
            public void onError() {
                if (listener != null)
                    if (transactionProvider != null) {
                        listener.onStatusChanged(transactionProvider.getTransactionStatus().toString());
                        abortPayment();
                    }
            }
        });

        executor.execute(transactionProvider::execute);
    }

    public void paymentReceipt(TransactionObject transactionObject) {
        PosPrintProvider customPosPrintProvider = new PosPrintProvider(context);
        customPosPrintProvider.addLine("PAN : " + transactionObject.getCardHolderNumber());
        customPosPrintProvider.addLine("DATE/TIME : " + transactionObject.getDate() + " " + transactionObject.getTime());
        customPosPrintProvider.addLine("AMOUNT : " + transactionObject.getAmount());
        customPosPrintProvider.addLine("ATK : " + transactionObject.getRecipientTransactionIdentification());
        customPosPrintProvider.setConnectionCallback(new StoneCallbackInterface() {
            @Override
            public void onSuccess() {
                Log.i("StoneManager", "paymentReceipt");
            }

            @Override
            public void onError() {
            }
        });
        executor.execute(customPosPrintProvider::execute);
    }

    @Override
    public void abortPayment() {
        if (transactionProvider != null) {
            transactionProvider.abortPayment();
            Log.i("StoneManager", "abortPayment");
        }
    }

    @Override
    public void setStatusListener(IStonePaymentListener listener) {
        if (listener != null)
        this.listener = status -> listener.onStatusChanged(status);
    }
}
