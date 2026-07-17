package dm.java10x.AvaliacaoDeProfessores.Controler;

import dm.java10x.AvaliacaoDeProfessores.dto.ProfessorUpdateDTO;
import dm.java10x.AvaliacaoDeProfessores.enumeradores.Adjetivo;
import dm.java10x.AvaliacaoDeProfessores.model.AlunoModel;
import dm.java10x.AvaliacaoDeProfessores.model.Image;
import dm.java10x.AvaliacaoDeProfessores.model.ProfessorModel;
import dm.java10x.AvaliacaoDeProfessores.service.AlunoService;
import dm.java10x.AvaliacaoDeProfessores.service.AulaService;
import dm.java10x.AvaliacaoDeProfessores.service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/professor")
public class ProfessorControler {

    @Autowired
    private AulaService aulaService;

    @Autowired
    private AlunoService alunoService;

    @Autowired
    private ProfessorService professorService;


    @GetMapping("/buscar/{email}")
    public ResponseEntity<ProfessorModel> buscarPorEmail(@PathVariable String email) {
        ProfessorModel professor = professorService.findProfessorModelByEmail(email);
        if (professor != null) {
            return ResponseEntity.ok(professor);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<ProfessorModel>> listarTodos() {
        List<ProfessorModel> professor = professorService.findAll();
        return ResponseEntity.ok(professor);
    }

    @GetMapping("/media/{id}")
    public ResponseEntity<Integer> buscarMediaPorId(@PathVariable Long id){
        aulaService.deletarAulasVencidas();
        Integer media = professorService.mediaDoProfessorPorId(id);
        return ResponseEntity.ok(media);
    }

    @GetMapping("/adjetivo/{id}")
    public ResponseEntity<Adjetivo> buscarAdjetivoPorId(@PathVariable Long id){
        aulaService.deletarAulasVencidas();
        Adjetivo adjetivo = professorService.modaDosAdjetivos(id);
        return ResponseEntity.ok(adjetivo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfessorModel> buscarPorId(@PathVariable Long id) {
        ProfessorModel professor = professorService.findById(id);
        return ResponseEntity.ok(professor);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> atualizar(
            @PathVariable Long id,
            @ModelAttribute ProfessorUpdateDTO obj) {
        if (Objects.isNull(professorService.findById(id))){
            return ResponseEntity.notFound().build();
        }
        ProfessorModel professorAtualizado = professorService.update(obj, id);
        return ResponseEntity.ok(professorAtualizado);
    }

    @PostMapping("/upload/{id}")
    public ResponseEntity<?> uploadImage(@PathVariable Long id, @RequestParam("image") MultipartFile file) {
        try {

            if (Objects.isNull(professorService.findById(id))){
                return ResponseEntity.notFound().build();
            }

            Image savedImage = professorService.uploadImage(file, professorService.findById(id));

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Imagem enviada com sucesso!");
            response.put("id", savedImage.getId());
            response.put("name", savedImage.getName());
            response.put("type", savedImage.getType());
            response.put("size", savedImage.getImageData().length + " bytes");
            response.put("url", "/api/images/download/" + savedImage.getId());

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", e.getMessage()
            ));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "success", false,
                            "error", "Erro ao processar a imagem: " + e.getMessage()
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "success", false,
                            "error", "Erro inesperado: " + e.getMessage()
                    ));
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        professorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
