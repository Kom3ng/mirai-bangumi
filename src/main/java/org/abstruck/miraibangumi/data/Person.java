package org.abstruck.miraibangumi.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record Person(
    @NotNull Integer id,
    @NotNull String name,
    @NotNull Integer type,
    @NotNull String[] career,
    @Nullable Images images,
    @NotNull String summary,
    @NotNull Boolean locked,
    @NotNull String last_modified,
    @Nullable Infobox[] infobox,
    @Nullable String gender,
    @Nullable Integer blood_type,
    @Nullable Integer birth_year,
    @Nullable Integer birth_mon,
    @Nullable Integer birth_day,
    @NotNull Stat stat) {
    
    public static record Stat(@NotNull Integer comments,@NotNull Integer collects) {
    }    
}
