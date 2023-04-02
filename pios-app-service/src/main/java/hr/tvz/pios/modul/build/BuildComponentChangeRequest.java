package hr.tvz.pios.modul.build;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/** Rekord za slanje promjene podataka o komponentama u buildu. */
public record BuildComponentChangeRequest(
    @NotBlank String link,
    @NotNull @Min(1) Long componentId,
    boolean add) {}
