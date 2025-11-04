package mottu_spot.mvc.service;

import mottu_spot.mvc.model.Dispositivo;
import mottu_spot.mvc.model.Moto;
import mottu_spot.mvc.repository.MotoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MotoService {

    private final MotoRepository motoRepository;

    public MotoService(MotoRepository motoRepository) {
        this.motoRepository = motoRepository;
    }

    public List<Moto> listarMotos() {
        return motoRepository.findAll();
    }

    public List<Moto> encontrarMotoPorPatio(Long patioId) {
        return motoRepository.findByPatioId(patioId);
    }

    public Moto criarMoto(Moto moto){
        // Criar dispositivo associado (sem referência à moto)
        Dispositivo dispositivo = Dispositivo.builder()
                .ativo(false)
                .build();
        
        moto.setDispositivo(dispositivo);
        
        // O cascade salvará o dispositivo automaticamente
        return motoRepository.save(moto);
    }

    public Moto encontrarMoto(Long id) {
        return motoRepository.findById(id).orElseThrow(() ->
            new RuntimeException("Moto não encontrada com id: " + id));
    }

    public Moto atualizarMoto(Moto moto) {
        return motoRepository.save(moto);
    }

    public void deletarMoto(Long id) {
        motoRepository.deleteById(id);
    }

    public Moto editarMoto(Long id, Moto moto) {
        Moto motoExistente = encontrarMoto(id);
        
        // Atualizar apenas os campos editáveis, preservando os dispositivos
        if (moto.getDescricao() != null) {
            motoExistente.setDescricao(moto.getDescricao());
        }
        if (moto.getPlaca() != null) {
            motoExistente.setPlaca(moto.getPlaca());
        }
        if (moto.getDataAdicao() != null) {
            motoExistente.setDataAdicao(moto.getDataAdicao());
        }
        if (moto.getStatus() != null) {
            motoExistente.setStatus(moto.getStatus());
        }
        
        // Se o pátio foi alterado, atualize
        if (moto.getPatio() != null) {
            motoExistente.setPatio(moto.getPatio());
        }
        
        // Só atualiza o dispositivo se for fornecido explicitamente
        if (moto.getDispositivo() != null) {
            motoExistente.setDispositivo(moto.getDispositivo());
        }
        
        return motoRepository.save(motoExistente);
    }

}
