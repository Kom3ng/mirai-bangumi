package org.abstruck.miraibangumi.data;

import java.util.List;

public class KVMap extends Infobox{
    public String key;
    public List<KV> value;

    public KVMap(String key,List<KV> value){
        this.key = key;
        this.value = value;
    }

    public static record KV(String k,String v){

    }
}
