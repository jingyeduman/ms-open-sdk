package com.miaoshou.erp.request;

import com.miaoshou.erp.response.CommonResponse;

public abstract class BaseRequest <T extends CommonResponse> {

    public abstract Class<T> getResponseClass();

    public abstract String getApiPath();

}
