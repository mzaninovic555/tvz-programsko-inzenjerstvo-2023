package hr.tvz.pios.modul.build;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Rekord za ažuriranje osnovnih podataka o buildu. */
public record BuildInfoChangeRequest(
    @NotBlank String link,
    @Size(max = 50) String title,
    @Size(max = 500) String description,
    boolean isFinalized,
    boolean isPublic) {}
