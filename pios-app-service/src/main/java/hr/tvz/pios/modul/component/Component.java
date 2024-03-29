package hr.tvz.pios.modul.component;

import hr.tvz.pios.common.Type;
import hr.tvz.pios.modul.manufacturer.Manufacturer;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Component {

  Long id;

  String name;

  BigDecimal price;

  Type type;

  String data;

  String imageBase64;

  Manufacturer manufacturer;

  Double rating;

  Integer reviewCount;

  Boolean reviewed;
}
