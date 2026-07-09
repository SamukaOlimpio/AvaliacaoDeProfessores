package dm.java10x.AvaliacaoDeProfessores.Controler;

import dm.java10x.AvaliacaoDeProfessores.enumeradores.Adjetivo;
import dm.java10x.AvaliacaoDeProfessores.model.AlunoModel;
import dm.java10x.AvaliacaoDeProfessores.model.ProfessorModel;
import dm.java10x.AvaliacaoDeProfessores.service.AlunoService;
import dm.java10x.AvaliacaoDeProfessores.service.AulaService;
import dm.java10x.AvaliacaoDeProfessores.service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    @PostMapping("/criarAluno")
    public ResponseEntity<AlunoModel> criar(@RequestBody AlunoModel aluno) {
        AlunoModel alunoCriado = alunoService.create(aluno);
        return ResponseEntity.status(HttpStatus.CREATED).body(alunoCriado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfessorModel> atualizar(@PathVariable Long id, @RequestBody ProfessorModel professor) {
        professor.setId(id);
        ProfessorModel professorAtualizado = professorService.update(professor);
        return ResponseEntity.ok(professorAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        professorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
