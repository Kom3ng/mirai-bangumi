package org.abstruck.miraibangumi.data;

public class SubjectType {
    public static final Type BOOK = new Type(1, "书籍");
    public static final Type ANIME = new Type(2, "动画");
    public static final Type MUSIC = new Type(3, "音乐");
    public static final Type GAME = new Type(4, "游戏");
    public static final Type REAL = new Type(6, "三次元");

    public static Type byId(int id){
        switch (id) {
            case 1: return BOOK;
            case 2: return ANIME;
            case 3: return MUSIC;
            case 4: return GAME;
            case 6: return REAL;
            default: return null;
        }
    }

    public static record Type(int id,String name) {
        
    }
}
