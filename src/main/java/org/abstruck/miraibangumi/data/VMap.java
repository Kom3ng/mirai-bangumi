package org.abstruck.miraibangumi.data;

import java.util.List;

public class VMap extends Infobox {
    public String key;
    public List<V> value;
    
    public VMap(String key,List<V> value){
        this.key = key;
        this.value = value;
    }
    public static record V(String v){}
}
