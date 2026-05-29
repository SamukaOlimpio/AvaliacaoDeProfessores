package dm.java10x.AvaliacaoDeProfessores.service;

import dm.java10x.AvaliacaoDeProfessores.enumeradores.Turma;
import dm.java10x.AvaliacaoDeProfessores.model.AulaModel;
import dm.java10x.AvaliacaoDeProfessores.model.AvaliacaoModel;
import dm.java10x.AvaliacaoDeProfessores.model.ProfessorModel;
import dm.java10x.AvaliacaoDeProfessores.repository.AvaliacaoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AvaliacaoService {

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private AlunoService alunoService;

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private AulaService aulaService;


    @Transactional
    public AvaliacaoModel creat(Long id_aluno, Long id_professor, Long id_aula){


        if (id_aluno == null) {
            throw new IllegalArgumentException("ID do aluno não pode ser nulo");
        }
        if (id_professor == null) {
            throw new IllegalArgumentException("ID do professor não pode ser nulo");
        }
        if (id_aula == null) {
            throw new IllegalArgumentException("ID da aula não pode ser nulo");
        }

        AvaliacaoModel obj = new AvaliacaoModel();
        obj.setAlunoModel(alunoService.findById(id_aluno));
        obj.setAulaModel(aulaService.findById(id_aula));
        obj.setProfessorModel(professorService.findById(id_professor));
        obj = this.avaliacaoRepository.save(obj);
        return obj;
    }

    public Boolean foiAvaliadoNessaSemana(List<AvaliacaoModel> avaliacaoModels){
        for(AvaliacaoModel avaliacao: avaliacaoModels){
            if(avaliacao.getAulaModel().getDataDeCriacao().isAfter(LocalDateTime.now().minusDays(6))){
                return true;
            }
        } return false;
    }

}
