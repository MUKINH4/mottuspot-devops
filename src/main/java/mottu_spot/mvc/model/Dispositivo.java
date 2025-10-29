package mottu_spot.mvc.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Dispositivo {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Builder.Default
    private boolean ativo = false;

    @OneToOne
    @JoinColumn(name = "moto_id")
    private Moto moto;
}
