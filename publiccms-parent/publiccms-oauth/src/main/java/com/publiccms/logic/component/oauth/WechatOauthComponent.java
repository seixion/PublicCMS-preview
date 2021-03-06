package com.publiccms.logic.component.oauth;

import static com.publiccms.common.tools.CommonUtils.notEmpty;

import java.io.IOException;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import com.publiccms.common.base.oauth.AbstractOauth;
import com.publiccms.view.pojo.oauth.OauthAccess;
import com.publiccms.view.pojo.oauth.OauthConfig;
import com.publiccms.view.pojo.oauth.OauthUser;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 *
 * 
 * WechatOauthClient
 * 
 */
@Component
public class WechatOauthComponent extends AbstractOauth {

    public WechatOauthComponent() {
        super("wechat");
    }

    /*
     * 
     * https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=
     * resource/res_list&verify=1&id=open1419316505&token=&lang=zh_CN
     * 
     */
    public String getAuthorizeUrl(int siteId, String state, boolean mobile) {
        OauthConfig config = getConfig(siteId);
        if (null != config) {
            StringBuilder sb = new StringBuilder("https://open.weixin.qq.com/connect/qrconnect?appid=");
            sb.append(config.getAppKey()).append("&redirect_uri=").append(config.getReturnUrl())
                    .append("&response_type=code&scope=snsapi_login&state=").append(state).append("#wechat_redirect");
            return sb.toString();
        }
        return null;
    }

    /*
     * 
     */
    @Override
    public OauthAccess getAccessToken(int siteId, String code) throws ClientProtocolException, IOException {
        OauthConfig config = getConfig(siteId);
        if (null != config) {
            StringBuilder sb = new StringBuilder("https://api.weixin.qq.com/sns/oauth2/access_token?appid=");
            sb.append(config.getAppKey()).append("&secret=").append(config.getAppSecret()).append("&code=").append(code)
                    .append("&grant_type=authorization_code");
            String html = get(sb.toString());
            Map<String, Object> map = objectMapper.readValue(html, new TypeReference<Map<String, Object>>() {
            });
            if (null != map.get("access_token")) {
                return new OauthAccess(code, (String) map.get("access_token"), (String) map.get("openid"));
            }
        }
        return null;
    }

    /*
     * https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=
     * resource/res_list&verify=1&id=open1419316518&token=&lang=zh_CN
     */
    @Override
    public OauthUser getUserInfo(int siteId, OauthAccess oauthInfo) throws ClientProtocolException, IOException {
        if (null != oauthInfo) {
            StringBuilder sb = new StringBuilder("https://api.weixin.qq.com/sns/userinfo?access_token=");
            sb.append(oauthInfo.getAccessToken()).append("&openid=").append(oauthInfo.getOpenId());
            String html = get(sb.toString());
            if (notEmpty(html)) {
                Map<String, Object> map = objectMapper.readValue(html, new TypeReference<Map<String, Object>>() {
                });
                return new OauthUser(oauthInfo.getOpenId(), (String) map.get("nickname"), (String) map.get("headimgurl"),
                        (Integer) map.get("sex") == 1 ? "m" : "f");
            }
        }
        return null;
    }

}
