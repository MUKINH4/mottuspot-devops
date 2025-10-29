package mottu_spot.mvc.repository;

import mottu_spot.mvc.model.Moto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MotoRepository extends JpaRepository<Moto, Long> {

    List<Moto> findByPatioId(Long id);

}
