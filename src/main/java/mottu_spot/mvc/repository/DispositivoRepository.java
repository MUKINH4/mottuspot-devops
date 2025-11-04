package mottu_spot.mvc.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import mottu_spot.mvc.model.Dispositivo;

public interface DispositivoRepository extends JpaRepository<Dispositivo, UUID> {

    
}