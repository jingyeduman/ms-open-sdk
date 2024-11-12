package com.miaoshou.erp.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TracePlatform {
    Amazon("amazon"),
    Tiktok("tiktok"),
    Temu("temu"),
    Shein("shein"),
    Shopify("shopify");


    private String value;

    TracePlatform(String value) {
        this.value = value;
    }

    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

}
