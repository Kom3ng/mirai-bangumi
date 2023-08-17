package org.abstruck.miraibangumi.util;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.abstruck.miraibangumi.data.SearchResult;
import okhttp3.Request;
import okhttp3.Response;

public class SearchUtil {
    public static final SearchUtil INSTANCE = new SearchUtil();

    public SearchResult search(String keyword,String type,String start,String maxResults){
        UrlBuilder urlBuilder = new UrlBuilder("/search/subject/"+URLEncoder.encode(keyword, StandardCharsets.UTF_8));
        urlBuilder.setQueryParament("responseGroup", "medium");
        if (type != null){
            urlBuilder.setQueryParament("type", type);
        }
        if(start != null){
            urlBuilder.setQueryParament("start",start);
        }
        if(maxResults != null){
            urlBuilder.setQueryParament("max_results", maxResults);
        }

        return search(urlBuilder);
    }

    public SearchResult search(UrlBuilder url){
        Request request = BgmRequestBuilder.INSTANCE
                    .apiCreate(url.build())
                    .get()
                    .build();      
        
        try (Response response = Context.INSTANCE.httpClient().newCall(request).execute()){
            if (response.isSuccessful()) {
                return Context.INSTANCE.gson().fromJson(response.body().string(),SearchResult.class);
            }
        } catch(Exception e){
            Context.INSTANCE.logger().warning(e);
        }

        return null;
    }

    public UrlBuilder generateUrlBuilder(String keyword,String type,String start,String maxResults){
        UrlBuilder urlBuilder = new UrlBuilder("/search/subject/"+URLEncoder.encode(keyword, StandardCharsets.UTF_8));
        urlBuilder.setQueryParament("responseGroup", "medium");
        if (type != null){
            urlBuilder.setQueryParament("type", type);
        }
        if(start != null){
            urlBuilder.setQueryParament("start",start);
        }
        if(maxResults != null){
            urlBuilder.setQueryParament("max_results", maxResults);
        }
        return urlBuilder;
    }
}
