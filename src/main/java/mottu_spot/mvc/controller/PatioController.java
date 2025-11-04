package mottu_spot.mvc.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import mottu_spot.mvc.model.Endereco;
import mottu_spot.mvc.model.Moto;
import mottu_spot.mvc.model.Patio;
import mottu_spot.mvc.service.MotoService;
import mottu_spot.mvc.service.PatioService;

@Controller
@RequestMapping("/")
public class PatioController {

    private final PatioService patioService;
    private final MotoService motoService;
    
    public PatioController(PatioService patioService, MotoService motoService) {
        this.patioService = patioService;
        this.motoService = motoService;
    }

    @GetMapping
    @Transactional(readOnly = true)
    public String index(Model model) {
        List<Patio> patios = patioService.listarPatios();
        // Força o carregamento das motos para evitar LazyInitializationException
        patios.forEach(patio -> {
            if (patio.getMotos() != null) {
                patio.getMotos().size(); // Força a inicialização da coleção
            }
        });
        model.addAttribute("patios", patios);
        return "index";
    }

    @GetMapping("/adicionarPatio")
    public String adicionarPatio(Model model) {
        Patio patio = new Patio();
        patio.setEndereco(new Endereco());
        model.addAttribute("patio", patio);
        return "adicionarPatio";
    }

    @GetMapping("/patios/{id}")
    public String motosPorPatio(Model model, @PathVariable Long id){
        Patio patio = patioService.encontrarPatio(id);
        List<Moto> motos = motoService.encontrarMotoPorPatio(id);
        patio.setMotos(motos);
        Long patioId = patio.getId();
        model.addAttribute("patio", patio);
        model.addAttribute("patioId", patioId);
        model.addAttribute("motos", motos);
        model.addAttribute("moto", new Moto());
        model.addAttribute("headerAction", "moto");
        return "motosPorPatio";
    }

    @PostMapping("/patios")
    public String salvarPatio(@Valid @ModelAttribute("patio") Patio patio, BindingResult result, RedirectAttributes redirect, Model model) {

        if (result.hasErrors()) {
            // Log dos erros para debug
            result.getAllErrors().forEach(error -> {
                System.out.println("Erro de validação: " + error.getDefaultMessage());
            });
            
            // Garantir que o endereço não seja nulo
            if (patio.getEndereco() == null) {
                patio.setEndereco(new Endereco());
            }
            
            model.addAttribute("patio", patio);
            return "adicionarPatio";
        }

        try {
            // Garantir que o endereço existe
            if (patio.getEndereco() == null) {
                patio.setEndereco(new Endereco());
            }
            
            patioService.salvarPatio(patio);
            redirect.addFlashAttribute("message", "Pátio criado com sucesso");
            return "redirect:/";
        } catch (Exception e) {
            System.out.println("Erro ao salvar pátio: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Erro ao salvar pátio: " + e.getMessage());
            model.addAttribute("patio", patio);
            return "adicionarPatio";
        }
    }

    @GetMapping("/patios/edit/{id}")
    public String editarPatioForm(@PathVariable Long id, Model model) {
        Patio patio = patioService.encontrarPatio(id);
        model.addAttribute("patio", patio);
        return "editarPatio";
    }

    @PostMapping("/patios/edit/{id}")
    public String editarPatio(@PathVariable Long id, @Valid @ModelAttribute Patio patio, BindingResult result, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return "editarPatio";
        }
        patio.setId(id);
        patioService.editarPatio(id, patio);
        redirect.addFlashAttribute("message", "Pátio editado com sucesso");
        return "redirect:/";
    }

    @GetMapping("/patios/delete/{id}")
    public String deletarPatio(@PathVariable Long id, RedirectAttributes redirect) {
        patioService.deletePatio(id);
        redirect.addFlashAttribute("message", "Pátio excluído com sucesso");
        return "redirect:/";
    }

}
