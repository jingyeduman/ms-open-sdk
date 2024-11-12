package com.miaoshou.erp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miaoshou.erp.domain.Item;
import com.miaoshou.erp.enums.TracePlatform;
import com.miaoshou.erp.request.BaseRequest;
import com.miaoshou.erp.request.PushCollectItemsRequest;
import kotlin.reflect.KFunction;
import okhttp3.*;
import org.apache.commons.codec.digest.DigestUtils;
import com.miaoshou.erp.response.CommonResponse;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class MsOpenApiClient {

    private String appKey;

    private String appSecret;

    public MsOpenApiClient(String appKey, String appSecret) {
        this.appKey = appKey;
        this.appSecret = appSecret;
    }

    /**
     * 获取跳转到授权页面的连接
     * @param uid 鸥鹭账号唯一表示符
     * @param username 鸥鹭账号名称，默认为手机号码
     * @param tracePlatform 从哪个平台页面发起的
     * @param callbackUrl 授权成功之后，跳转页面
     */
    public String getGotoAuthUrl(String uid, String username, TracePlatform tracePlatform, String callbackUrl) {
        assert uid != null;
        assert !uid.isEmpty();

        assert username != null;
        assert !username.isEmpty();

        assert tracePlatform != null;

        HashMap<String, String> query = new HashMap<>();
        query.put("uid", uid);
        query.put("username", username);
        query.put("tracePlatform", String.valueOf(tracePlatform));
        if (callbackUrl != null && !callbackUrl.isEmpty()) {
            query.put("callbackUrl", callbackUrl);
        }

        String apiPath = "open_api/auth/goto_auth_page";
        return this.buildRequestUrl(apiPath, query);
    }

    /**
     * 获取跳转到帮助文章连接
     * @param tracePlatform 哪个平台跳转过来的
     */
    public String getGotoHelpArticleUrl(TracePlatform tracePlatform) {
        String apiPath = "open_api/auth/goto_help_article";
        HashMap<String, String> query = new HashMap<>();
        query.put("tracePlatform", String.valueOf(tracePlatform));
        return this.buildRequestUrl(apiPath, query);
    }

    /**
     * 推送发布商品到妙手
     * @param items 商品列表
     * @param uid 第三方合作方的用户id
     * @param tracePlatform 哪个平台发起的
     */
    public CommonResponse pushCollectItems(ArrayList<Item> items, String uid, TracePlatform tracePlatform) {
        PushCollectItemsRequest request = new PushCollectItemsRequest();
        request.setUid(uid);
        request.setTracePlatform(tracePlatform);
        request.setItems(items);

        return executeRequest(request);
    }

    private CommonResponse executeRequest(BaseRequest request) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .callTimeout(30, TimeUnit.SECONDS)
                    .build();

            String postData = objectMapper.writeValueAsString(request);
            RequestBody requestBody = RequestBody.create(postData, MediaType.parse("application/json;charset=utf-8"));

            String url = buildRequestUrl(request.getApiPath(), null);
            Request httpRequest = new Request.Builder()
                    .post(requestBody)
                    .url(url)
                    .build();

            Response signRsp = null;
            try {
                signRsp = client.newCall(httpRequest).execute();
                if (signRsp.isSuccessful()) {
                    assert signRsp.body() != null;
                    String body = signRsp.body().string();
                    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    CommonResponse resp = (CommonResponse) objectMapper.readValue(body, request.getResponseClass());

                    System.out.println("body : " + objectMapper.writeValueAsString(resp));
                    System.out.println("isSuccess : " + resp.isSuccess());

                    return resp;
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    private String buildRequestUrl(String apiPath, HashMap<String, String> query)  {
        //生成时间戳
        long timestamp = System.currentTimeMillis() / 1000;
        //生成签名
        String sign =  DigestUtils.md5Hex(appKey + appSecret + apiPath + timestamp).toLowerCase();

        //组装公共参数
        HashMap<String, String> commonParams = new HashMap<>();
        commonParams.put("timestamp", String.valueOf(timestamp));
        commonParams.put("sign", sign);
        commonParams.put("appKey", appKey);

        //添加get的请求参数到链接中
        if (query != null) {
            commonParams.putAll(query);
        }

        //组装get请求参数
        String queryString = commonParams.entrySet().stream()
                .map(p -> {
                    return URLEncoder.encode(p.getKey(), StandardCharsets.UTF_8) + "=" +  URLEncoder.encode(p.getValue(), StandardCharsets.UTF_8);
                })
                .reduce((p1, p2) -> p1 + "&" + p2)
                .orElse("");

        String host = "https://erp.91miaoshou.com/api/";
        List<String> list = Arrays.asList(host, apiPath, "?", queryString);
        return String.join("", list);
    }
}


