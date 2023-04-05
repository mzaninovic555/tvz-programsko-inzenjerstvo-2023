package hr.tvz.pios.modul.manufacturer;

import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * Repozitorij za dohvat manufacturera.
 */
@Mapper
@Repository
public interface ManufacturerRepository {

  List<Manufacturer> getAll();

  Optional<Manufacturer> getById(Long id);

  void insert(Manufacturer manufacturer);

  Integer updateById(Manufacturer manufacturer);

  Integer deleteById(Long id);
}
