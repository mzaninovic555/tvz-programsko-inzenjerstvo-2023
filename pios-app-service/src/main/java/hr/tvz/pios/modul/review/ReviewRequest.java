package hr.tvz.pios.modul.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Rekord za slanje nove recenzije.
 */
public record ReviewRequest(
    @NotNull @Min(1) Long componentId,
    @NotNull @Min(1) @Max(5) int rating
) {}
