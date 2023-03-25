package hr.tvz.pios.modul.wishlist;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record WishlistRequest(@NotNull @Min(1) Long componentId) {}
