package com.miaoshou.erp.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.miaoshou.erp.enums.Result;

public class CommonResponse {

    private String code;

    private Result result;

    private String reason;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
   }

   @JsonIgnore
   public boolean isSuccess() {
        return this.result == Result.Success;
   }
}
