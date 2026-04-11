package com.scbdev.stone_plugin.domain.enums;

import com.scbdev.stone_plugin.domain.MethodEnum;

public class MethodEnumFromText {

   public MethodEnumFromText(){}
    public static MethodEnum fromText(String text) {
        for (MethodEnum method : MethodEnum.values()) {
            if (method.name().equalsIgnoreCase(text)) {
                return method;
            }
        }
        throw new IllegalArgumentException("Invalid method name: " + text);

    }
}
