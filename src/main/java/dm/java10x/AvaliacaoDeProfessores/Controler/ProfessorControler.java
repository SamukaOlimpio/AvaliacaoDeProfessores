package dm.java10x.AvaliacaoDeProfessores.Controler;

import dm.java10x.AvaliacaoDeProfessores.model.AlunoModel;
import dm.java10x.AvaliacaoDeProfessores.model.ProfessorModel;
import dm.java10x.AvaliacaoDeProfessores.service.AlunoService;
import dm.java10x.AvaliacaoDeProfessores.service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/professor")
public class ProfessorControler {
    @Autowired
    private AlunoService alunoService;

    @Autowired
    private ProfessorService professorService;

    @GetMapping
    public ResponseEntity<List<ProfessorModel>> listarTodos() {
        List<ProfessorModel> professor = professorService.findAll();
        return ResponseEntity.ok(professor);
    }

    @GetMapping("/media/{id}")
    public ResponseEntity<Integer> buscarMediaPorId(@PathVariable long id){
        Integer media = professorService.mediaDoProfessorPorId(id);
        return ResponseEntity.ok(media);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfessorModel> buscarPorId(@PathVariable Long id) {
        ProfessorModel professor = professorService.findById(id);
        return ResponseEntity.ok(professor);
    }

    @PostMapping("/criarProfessor")
    public ResponseEntity<ProfessorModel> criar(@RequestBody ProfessorModel professor) {
        ProfessorModel professorCriado = professorService.create(professor);
        return ResponseEntity.status(HttpStatus.CREATED).body(professorCriado);
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
