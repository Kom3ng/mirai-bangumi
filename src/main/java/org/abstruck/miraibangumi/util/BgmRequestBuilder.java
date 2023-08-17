package org.abstruck.miraibangumi.util;

import okhttp3.Request;

/**
 * @author Astrack
 * @date 2023/8/14
 */
public class BgmRequestBuilder {
    public static final BgmRequestBuilder INSTANCE = new BgmRequestBuilder();
    public Request.Builder apiCreate(String url){
        return internalRawCreate()
                .url(Context.INSTANCE.bgmApiServer()+url);
    }

    public Request.Builder create(String url){
        return internalRawCreate().url(Context.INSTANCE.bgmServer()+url);
    }

    public Request.Builder authCreate(String url,String token){
        return create(url).addHeader("Authorization", "Bearer %s".formatted(token));
    }

    private Request.Builder internalRawCreate(){
        return new Request.Builder()
                .removeHeader("User-Agent")
                .addHeader("User-Agent", "Astrack/mirai-bangumi (https://github.com/Kom3ng/mirai-bangumi)");
    }

    public Request.Builder apiAuthCreate(String url,String token){
        return apiCreate(url).addHeader("Authorization", "Bearer %s".formatted(token));
    }
}
