package org.abstruck.miraibangumi.data;

public record SubjectRelatedCharacter(
    Integer id,
    String name,
    Integer type,
    Images images,
    String relation,
    Actor[] actors
) {
    
    public static record Actor(
        Integer id,
        String name,
        Integer type,
        String[] career,
        Images images,
        String short_summary,
        Boolean locked
    ) {
    }
}
