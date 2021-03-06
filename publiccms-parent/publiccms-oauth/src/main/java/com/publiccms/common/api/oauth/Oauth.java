package com.publiccms.common.api.oauth;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import com.publiccms.view.pojo.oauth.OauthAccess;
import com.publiccms.view.pojo.oauth.OauthUser;

/**
 *
 * Oauth
 *
 */
public interface Oauth {
    /**
     * @return channel
     */
    public String getChannel();

    /**
     * @param siteId
     * @return enabled
     */
    public boolean enabled(int siteId);

    /**
     * @param siteId
     * @param state
     * @param mobilde
     * @return authorize url
     */
    public String getAuthorizeUrl(int siteId, String state, boolean mobilde);

    /**
     * @param siteId
     * @param state
     * @return authorize url
     */
    public String getAuthorizeUrl(int siteId, String state);

    /**
     * @param siteId
     * @param code
     * @return oauth access
     * @throws ClientProtocolException
     * @throws IOException
     */
    public OauthAccess getOpenId(int siteId, String code) throws ClientProtocolException, IOException;
    /**
     * @param siteId
     * @param oauthAccess
     * @return user info
     * @throws ClientProtocolException
     * @throws IOException
     */
    public OauthUser getUserInfo(int siteId, OauthAccess oauthAccess) throws ClientProtocolException, IOException;
}
