package com.miaoshou.erp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miaoshou.erp.domain.Item;
import com.miaoshou.erp.enums.TracePlatform;
import com.miaoshou.erp.response.CommonResponse;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class MsOpenApiClientTest {

    private MsOpenApiClient msOpenApiClient;

    @Before
    public void before() {
        String appKey = "d070d5de-73df-4b34-82fe-0734732afe74";
        String appSecret = "iCLgtldC8ITx8dtJ6IRNBBRQ1uaIisEB4pRyL5aTN3qWhTGoCy";
        msOpenApiClient = new MsOpenApiClient(appKey, appSecret);
    }

    @Test
    public void pushCollectItems() throws JsonProcessingException {
        Item item = new Item();
        item.setTitle("JOISCOPE 电脑桌带 USB 和 C 型插座,小型家庭办公桌,带 2 层储物架,现代游戏桌面工作站,48 英寸(约 121.9 厘米),白色");
        item.setImageUrl("https://m.media-amazon.com/images/I/71ufeJkoM+L._AC_SX679_.jpg");
        item.setProductUrl("https://www.amazon.com/dp/B0D5CXRL3N");

        ArrayList<Item> items = new ArrayList<>();
        items.add(item);

        String uid = "xxxxxxx";
        CommonResponse resp = msOpenApiClient.pushCollectItems(items, uid, TracePlatform.Amazon);

        ObjectMapper objectMapper = new ObjectMapper();
        String string = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(resp);
        System.out.println(string);
    }


    @Test
    public void getGotoAuthUrl() {
        String uid = "xxxxxxx";
        String url = msOpenApiClient.getGotoAuthUrl(uid, "15695908791", TracePlatform.Tiktok, "https://vip.oalur.com/tiktok/master/list?site=US&shopId=7495625889764313322");
        System.out.println(url);
    }

    @Test
    public void getGotoHelpArticleUrl() {
        String url = msOpenApiClient.getGotoHelpArticleUrl(TracePlatform.Temu);
        System.out.println(url);
    }
}