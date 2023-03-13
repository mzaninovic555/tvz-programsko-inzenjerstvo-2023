package hr.tvz.pios.model.component;

import hr.tvz.pios.common.Types;
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

  Types type;

  String data;

  String imageBase64;
}
