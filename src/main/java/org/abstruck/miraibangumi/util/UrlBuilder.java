package org.abstruck.miraibangumi.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class UrlBuilder {
    private String url;

    private Map<String,String> queryParament;
    
    public UrlBuilder(String url){
        this.url = url;
        this.queryParament = new HashMap<>();
    }    

    public void setQueryParament(String key,String value){
        queryParament.put(key, value);    
    }

    public String build(){
        StringBuilder sb = new StringBuilder(url);
        if(!queryParament.isEmpty()){
            sb.append('?');
            queryParament.forEach((k,v) -> sb.append(k).append('=').append(v).append('&'));
            sb.deleteCharAt(sb.length()-1);
        }

        return sb.toString();
    }

    public String getQueryParament(String key){
        return queryParament.get(key);
    }
    public String getQueryParamentOr(String key,Supplier<String> supplier){
        String v = this.queryParament.get(key);
        if(v == null){
            return supplier.get();
        }
        return v;
    }
}
