package mottu_spot.mvc.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import mottu_spot.mvc.model.Dispositivo;
import mottu_spot.mvc.repository.DispositivoRepository;

@Service
public class DispositivoService {

    private final DispositivoRepository dispositivoRepository;

    public DispositivoService(DispositivoRepository dispositivoRepository) {
        this.dispositivoRepository = dispositivoRepository;
    }

    public List<Dispositivo> listarDispositivos() {
        return dispositivoRepository.findAll();
    }

    public Dispositivo encontrarDispositivo(UUID id) {
        return dispositivoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dispositivo não encontrado com id: " + id));
    }

    public Dispositivo salvarDispositivo(Dispositivo dispositivo) {
        return dispositivoRepository.save(dispositivo);
    }

    public Dispositivo editarDispositivo(UUID id, Dispositivo dispositivo) {
        Dispositivo dispositivoExistente = encontrarDispositivo(id);
        
        // Atualizar apenas o campo ativo
        dispositivoExistente.setAtivo(dispositivo.isAtivo());
        
        return dispositivoRepository.save(dispositivoExistente);
    }

    public void deletarDispositivo(UUID id) {
        dispositivoRepository.deleteById(id);
    }
    
    public Dispositivo ativarDispositivo(UUID id) {
        Dispositivo dispositivo = encontrarDispositivo(id);
        dispositivo.setAtivo(true);
        return dispositivoRepository.save(dispositivo);
    }
    
    public Dispositivo desativarDispositivo(UUID id) {
        Dispositivo dispositivo = encontrarDispositivo(id);
        dispositivo.setAtivo(false);
        return dispositivoRepository.save(dispositivo);
    }
}
