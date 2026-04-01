package com.scbdev.stone_plugin;

import android.widget.Toast;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.scbdev.stone_plugin.interfaces.StoneCallback;
import com.scbdev.stone_plugin.params.PaymentParams;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
import stone.user.UserModel;
import stone.utils.keys.StoneKeyType;
import stone.utils.Stone;

public class StoneManager {

    public static synchronized StoneManager getInstance() {
        if (instance == null) {
            instance = new StoneManager();
        }
        return instance;
    }

    BaseTransactionProvider transactionProvider;

    ActiveApplicationProvider provider;
    private static StoneManager instance;

    private List<UserModel> user;

    public interface StonePaymentListener {
        void onStatusChanged(String status);
    }

    private StonePaymentListener listener;

    public void setStatusListener(StonePaymentListener listener) {
        this.listener = listener;
    }

    public void init(Context context, StoneCallback callback) {
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
                        user = StoneStart.init(context, stoneKeys);
                        Stone.setAppName("VALET FITPARK");
                        if (user != null) {
                            new Handler(Looper.getMainLooper()).post(callback::onSuccess);
                            return;
                        }
                        new Handler(Looper.getMainLooper()).post(() -> {
                            callback.onError("Erro ao inicializar o SDK");
                        });
                    } catch (Exception e) {
                        new Handler(Looper.getMainLooper()).post(() -> {
                            callback.onError("Erro ao inicializar o SDK");
                        });
                    }
                });
    }

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    /* * Encapsulamento de Hardware I/O:
     * Execução em Background via Executor + Resposta em UI Thread via Handler.
     * Garante fluidez no Flutter e integridade no MethodChannel.
     */
    public void printReceipt(Context context, StoneCallback callback) {

        PosPrintProvider customPosPrintProvider = new PosPrintProvider(context);
        customPosPrintProvider.addLine("PAN : TEXT");
        customPosPrintProvider.addLine("DATE/TIME : ");
        customPosPrintProvider.addLine("AMOUNT : ");
        customPosPrintProvider.addLine("ATK : ");
        customPosPrintProvider.addLine("Signature");
        customPosPrintProvider.setConnectionCallback(new StoneCallbackInterface() {
            @Override
            public void onSuccess() {
                new Handler(Looper.getMainLooper()).post(callback::onSuccess);
            }

            @Override
            public void onError() {
                new Handler(Looper.getMainLooper()).post(() -> {
                    callback.onError("Erro ao imprimir");
                });
            }
        });

        executor.execute(customPosPrintProvider::execute);
    }

    public void activateStonecode(Context context, String stoneCode, StoneCallback callback) {
        provider = new ActiveApplicationProvider(context);
        provider.setConnectionCallback(new StoneCallbackInterface() {
            @Override
            public void onSuccess() {
                new Handler(Looper.getMainLooper()).post(callback::onSuccess);
            }

            @Override
            public void onError() {
                new Handler(Looper.getMainLooper()).post(() -> {
                    callback.onError("Erro ao ativar Stonecode");
                });
            }
        });
        executor.execute(
                () -> {
                    try {
                        provider.activate(stoneCode);
                    } catch (Exception e) {
                        new Handler(Looper.getMainLooper()).post(() -> {
                            callback.onError("Erro ao ativar Stonecode");
                        });
                    }
                });
    }

    public void payment(Context context, PaymentParams params) {
        final TransactionObject transactionObject = new TransactionObject();
        transactionObject.setInstalmentTransaction(InstalmentTransactionEnum.ONE_INSTALMENT);

        if (params.type.equals("DEBIT_CARD"))
            transactionObject.setTypeOfTransaction(TypeOfTransactionEnum.DEBIT);

        if (params.type.equals("CREDIT_CARD"))
            transactionObject.setTypeOfTransaction(TypeOfTransactionEnum.CREDIT);

        transactionObject.setAmount(params.amount);

        transactionProvider = new PosTransactionProvider(context, transactionObject, user.get(0));

        transactionProvider.setConnectionCallback(new StoneActionCallback() {
            @Override
            public void onStatusChanged(Action action) {
                listener.onStatusChanged(action.name());

            }

            @Override
            public void onSuccess() {
                paymentReceipt(context, transactionObject);
                listener.onStatusChanged("PAYMENT_SUCCESS");
            }

            @Override
            public void onError() {
                listener.onStatusChanged(transactionProvider.getTransactionStatus().toString());
            }
        });

        executor.execute(transactionProvider::execute);
    }

    public void paymentReceipt(Context context, TransactionObject transactionObject) {
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
                Toast.makeText(context, "Erro ao imprimir: " + customPosPrintProvider.getListOfErrors(), Toast.LENGTH_SHORT).show();
            }
        });
        executor.execute(customPosPrintProvider::execute);
    }

    public void abortPayment() {
        if (transactionProvider != null) {
            transactionProvider.abortPayment();
            Log.i("StoneManager", "abortPayment");
        }
    }
}
