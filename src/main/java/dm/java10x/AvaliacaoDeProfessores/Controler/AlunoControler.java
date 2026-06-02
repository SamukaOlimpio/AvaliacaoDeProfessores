
package dm.java10x.AvaliacaoDeProfessores.Controler;



import dm.java10x.AvaliacaoDeProfessores.dto.AvaliacaoDaAulaDTO;
import dm.java10x.AvaliacaoDeProfessores.model.AlunoModel;
import dm.java10x.AvaliacaoDeProfessores.model.AulaModel;
import dm.java10x.AvaliacaoDeProfessores.model.AvaliacaoModel;
import dm.java10x.AvaliacaoDeProfessores.model.ProfessorModel;
import dm.java10x.AvaliacaoDeProfessores.service.AlunoService;
import dm.java10x.AvaliacaoDeProfessores.service.AulaService;
import dm.java10x.AvaliacaoDeProfessores.service.AvaliacaoService;
import dm.java10x.AvaliacaoDeProfessores.service.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/aluno")
public class AlunoControler {

    @Autowired
    private AulaService aulaService;

    @Autowired
    private AlunoService alunoService;

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private AvaliacaoService avaliacaoService;

    @GetMapping
    public ResponseEntity<List<AlunoModel>> listarTodos() {
        List<AlunoModel> alunos = alunoService.findAll();
        return ResponseEntity.ok(alunos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlunoModel> buscarPorId(@PathVariable Long id) {
        AlunoModel aluno = alunoService.findById(id);
        return ResponseEntity.ok(aluno);
    }

    @GetMapping("/aulas/{id}")
    public ResponseEntity<List<ProfessorModel>> avaliarAulas(@PathVariable Long id){
        AlunoModel aluno = alunoService.findById(id);
        List<ProfessorModel> professoresQueEnsinamMesmaTurma = professorService.filtrarPorTurma(aluno.getTurma());
        List<ProfessorModel> professoresFiltrados = professorService.filtrarProfessoresNaoAvaliadosEstaSemana(professoresQueEnsinamMesmaTurma, aluno);
        return ResponseEntity.ok(professoresFiltrados);
    }
    @PostMapping("/aula")
    public ResponseEntity<AulaModel> avaliarAula(@RequestBody AvaliacaoDaAulaDTO aulaDTO){
        AlunoModel aluno = alunoService.findById(aulaDTO.id_aluno());
        AulaModel aula = new AulaModel();
        aula.setAdjetivo(aulaDTO.adjetivo());
        aula.setNota(aulaDTO.nota());
        AulaModel obj = this.aulaService.create(aula);
        AvaliacaoModel avaliacao = this.avaliacaoService.creat(aulaDTO.id_aluno(), aulaDTO.id_professor(), obj.getId());
        return ResponseEntity.ok(obj);
    }


    @PutMapping("/{id}")
    public ResponseEntity<AlunoModel> atualizar(@PathVariable Long id, @RequestBody AlunoModel aluno) {
        aluno.setId(id);
        AlunoModel alunoAtualizado = alunoService.update(aluno);
        return ResponseEntity.ok(alunoAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        alunoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}