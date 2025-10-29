package mottu_spot.mvc.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Pattern(regexp = "^\\d{5}-?\\d{3}$", message = "CEP Inválido. Use o formato 12345-000")
    private String cep;

    @NotBlank(message = "logradouro não pode ser nulo")
    private String logradouro;

    @Positive(message = "O número deve ser positivo")
    @Min(value = 1, message = "O valor mínimo é 1")
    private int numero;

    @NotBlank(message = "bairro não pode ser nulo")
    private String bairro;

    @NotBlank(message = "cidade não pode ser nulo")
    private String cidade;

    @NotBlank(message = "estado não pode ser nulo")
    private String estado;

    @NotBlank(message = "país não pode ser nulo")
    private String pais;

}
