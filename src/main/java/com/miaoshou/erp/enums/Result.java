package com.miaoshou.erp.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Result {

    Success("success"),
    Fail("fail");

    private String value;

    Result(String value) {
        this.value = value;
    }

    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }

}
