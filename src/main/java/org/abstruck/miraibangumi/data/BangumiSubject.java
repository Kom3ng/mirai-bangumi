package org.abstruck.miraibangumi.data;

public record BangumiSubject(
    Integer id,
    Integer type,
    String name,
    String name_cn,
    String summary,
    Boolean nsfw,
    Boolean locked,
    String date,
    String platform,
    Images images,
    Object[] infobox,
    Integer volumes,
    Integer eps,
    Integer total_episodes,
    Rating rating,
    Collection collection,
    Tag[] tags) {

    public static record Rating(Integer rank,Integer total,BangumiSubjectCount count,Number score) {
    }
    public static record Collection(Integer wish,Integer collect,Integer doing,Integer on_hold,Integer dropped) {
    }
    public static record Tag(String name,Integer count) {
    }
}
