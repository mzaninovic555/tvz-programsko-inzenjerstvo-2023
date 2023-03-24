package hr.tvz.pios.modul.wishlist;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

public record WishlistRequest(@NotEmpty @Min(1) Long componentId) {}
