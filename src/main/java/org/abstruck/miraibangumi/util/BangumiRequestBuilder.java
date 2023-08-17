package org.abstruck.miraibangumi.util;

import okhttp3.Request;

public class BangumiRequestBuilder extends Request.Builder {
    public static BangumiRequestBuilder create(){
        return (BangumiRequestBuilder) new BangumiRequestBuilder()
            .removeHeader("User-Agent")
            .addHeader("User-Agent", "Astrack/mirai-bangumi (https://github.com/Kom3ng/mirai-bangumi)");
    }

    public BangumiRequestBuilder apiPath(String path){
        return (BangumiRequestBuilder) url(Context.INSTANCE.bgmApiServer()+path);
    }

    public BangumiRequestBuilder auth(String token){
        return (BangumiRequestBuilder) addHeader("Authorization", "Bearer %s".formatted(token));
    }

    public BangumiRequestBuilder path(String path){
        return (BangumiRequestBuilder) url(Context.INSTANCE.bgmServer()+path);
    }

    public BangumiRequestBuilder getMethod(){
        return (BangumiRequestBuilder) get();
    }

    public RequestExcetor buildToExecutor(){
        return new RequestExcetor(Context.INSTANCE.httpClient(), build());
    }
}
