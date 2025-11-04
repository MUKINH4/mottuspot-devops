package mottu_spot.mvc.service;

import java.util.List;

import org.springframework.stereotype.Service;

import mottu_spot.mvc.model.Patio;
import mottu_spot.mvc.repository.PatioRepository;

@Service
public class PatioService {

    private final PatioRepository patioRepository;

    public PatioService(PatioRepository patioRepository) {
        this.patioRepository = patioRepository;
    }

    public List<Patio> listarPatios() {
        return patioRepository.findAll();
    }
    public Patio encontrarPatio(Long id){
        return patioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patio no encontrado"));
    }

    public int contarMotosPorPatio(Long patioId) {
        Patio patio = encontrarPatio(patioId);
        return patio.getMotos() != null ? patio.getMotos().size() : 0;
    }

    public Patio salvarPatio(Patio patio) {
        return patioRepository.save(patio);
    }

    public void deletePatio(Long id) {
        patioRepository.deleteById(id);
    }

    public Patio editarPatio(Long id, Patio patio) {
        Patio patioExistente = encontrarPatio(id);
        
        // Atualizar apenas os campos editáveis, preservando as motos
        if (patio.getNome() != null) {
            patioExistente.setNome(patio.getNome());
        }
        if (patio.getLotacao() > 0) {
            patioExistente.setLotacao(patio.getLotacao());
        }
        if (patio.getDataAdicao() != null) {
            patioExistente.setDataAdicao(patio.getDataAdicao());
        }
        
        // Só atualiza o endereço se for fornecido
        if (patio.getEndereco() != null) {
            patioExistente.setEndereco(patio.getEndereco());
        }
        
        // As motos são preservadas automaticamente (não são tocadas)
        return patioRepository.save(patioExistente);
    }
}
