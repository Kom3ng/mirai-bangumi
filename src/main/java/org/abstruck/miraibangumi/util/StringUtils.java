package org.abstruck.miraibangumi.util;

import java.util.List;
import java.util.function.Function;

public class StringUtils {
    public static final int QQ_LIMIT = 4999;
    public static String[] limit(String str, int l){
        int count = (int) Math.ceil(str.length()/l) + 1;
        String[] result = new String[count];
        for (int i=0;i<count;i++){
            int endIndex = i*l + l;
            result[i] = str.substring(i*l,endIndex>str.length()?str.length():endIndex);
        }
        return result;
    }
    
    public static String[] qqLimit(String str){
        return limit(str, QQ_LIMIT);
    }

    public static <T> String listStringGenerate(List<T> list,String title,Function<T,String> strParser){
        StringBuilder sb = new StringBuilder();
        sb.append(title).append("\n\n");
        list.forEach(t -> {
            sb.append(strParser.apply(t)).append('、');
        });
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }
}
