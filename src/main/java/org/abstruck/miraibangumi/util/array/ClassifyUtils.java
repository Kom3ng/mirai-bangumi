package org.abstruck.miraibangumi.util.array;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ClassifyUtils {
    public static <K,V> Map<K,List<V>> classify(List<V> list, Function<V,K> classifyIdentify){
        Map<K,List<V>> result = new HashMap<>();

        list.forEach(v -> {
            K k = classifyIdentify.apply(v);
            List<V> l = result.getOrDefault(k, new ArrayList<>());
            l.add(v);
            result.put(k, l);
        });

        return result;
    } 

    public static <K,V> Map<K,List<V>> classify(V[] list,Function<V,K> classifyIdentify){
        return classify(List.of(list), classifyIdentify);
    }
}
