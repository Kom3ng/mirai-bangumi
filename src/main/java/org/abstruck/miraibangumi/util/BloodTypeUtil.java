package org.abstruck.miraibangumi.util;

public class BloodTypeUtil {
    public static String byId(int id){
        switch (id){
            case 1: return "A";
            case 2:return "B";
            case 3:return "AB";
            case 4: return "O";
            default: return "UNKONWN";
        }
    }
}
