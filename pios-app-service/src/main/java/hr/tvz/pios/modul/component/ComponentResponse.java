package hr.tvz.pios.modul.component;

import hr.tvz.pios.common.Type;
import hr.tvz.pios.modul.manufacturer.Manufacturer;
import java.math.BigDecimal;

/**
 * Rekord za slanje odgovora kod dohvata komponenata
 */
public record ComponentResponse(
    Long id,
    String imageBase64,
    Manufacturer manufacturer,
    String name,
    BigDecimal price,
    Double rating,
    Integer reviewCount,
    Boolean reviewed,
    Type type) {
  public static ComponentResponse fromComponent(Component component) {
    return new ComponentResponse(
        component.getId(),
        component.getImageBase64(),
        component.getManufacturer(),
        component.getName(),
        component.getPrice(),
        component.getRating() == null ? 0 : component.getRating(),
        component.getReviewCount() == null ? 0 : component.getReviewCount(),
        component.getReviewed(),
        component.getType());
  }
}
