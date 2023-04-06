package hr.tvz.pios.modul.forum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Record koji predstavlja HTTP request za novi forum post.
 */
public record ForumPostCreateRequest(
    @NotNull Long id,
    @NotBlank @Size(max = 50) String title,
    @NotNull @Size(max = 1000) String content
) { }
