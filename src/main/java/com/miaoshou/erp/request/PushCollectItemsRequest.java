package com.miaoshou.erp.request;

import com.miaoshou.erp.domain.Item;
import com.miaoshou.erp.enums.TracePlatform;
import com.miaoshou.erp.response.CommonResponse;

import java.util.ArrayList;

public class PushCollectItemsRequest extends BaseRequest<CommonResponse> {

    private String uid;

    private TracePlatform tracePlatform;

    private ArrayList<Item> items = new ArrayList<>();

    @Override
    public Class<CommonResponse> getResponseClass() {
        return CommonResponse.class;
    }

    @Override
    public String getApiPath() {
        return "open_api/item/push_collect_items";
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public TracePlatform getTracePlatform() {
        return tracePlatform;
    }

    public void setTracePlatform(TracePlatform tracePlatform) {
        this.tracePlatform = tracePlatform;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }
}
