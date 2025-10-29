package mottu_spot.mvc.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Moto {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "A placa não pode estar vazia")
    @Size(min = 6, max = 10, message = "A placa deve ter entre 6 e 10 caracteres")
    @Pattern(regexp = "^[A-Z0-9\\- ]{6,10}$", message = "Placa fora do padrão")
    private String placa;

    private String descricao;

    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @Builder.Default
    private LocalDateTime dataAdicao =  LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "patio_id")
    private Patio patio;

    @OneToOne
    private Dispositivo dispositivo;
}
