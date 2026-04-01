package com.scbdev.stone_plugin;

public enum MethodEnum {
    init,
    printReceipt,
    payment,
    abortPayment,
    activateStonecode,
}

class MethodEnumFromText{
    public static MethodEnum fromText(String text) {
        for (MethodEnum method : MethodEnum.values()) {
            if (method.name().equalsIgnoreCase(text)) {
                return method;
            }
        }
        throw new IllegalArgumentException("Invalid method name: " + text);

    }
}