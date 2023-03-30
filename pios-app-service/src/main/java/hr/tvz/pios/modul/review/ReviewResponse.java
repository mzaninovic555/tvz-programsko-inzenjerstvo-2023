package hr.tvz.pios.modul.review;

/**
 * Rekord za slanje odgovora nakon kreiranja recenzije.
 */
public record ReviewResponse(Double newRating, Integer newReviewCount) {}
