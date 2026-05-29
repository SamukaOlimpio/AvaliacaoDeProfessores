package dm.java10x.AvaliacaoDeProfessores.service;

import dm.java10x.AvaliacaoDeProfessores.enumeradores.Turma;
import dm.java10x.AvaliacaoDeProfessores.model.AlunoModel;
import dm.java10x.AvaliacaoDeProfessores.model.AvaliacaoModel;
import dm.java10x.AvaliacaoDeProfessores.model.ProfessorModel;
import dm.java10x.AvaliacaoDeProfessores.model.TurmaModel;
import dm.java10x.AvaliacaoDeProfessores.repository.AulaRepository;
import dm.java10x.AvaliacaoDeProfessores.repository.AvaliacaoRepository;
import dm.java10x.AvaliacaoDeProfessores.repository.ProfessorRepository;
import dm.java10x.AvaliacaoDeProfessores.repository.TurmaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProfessorService {

    @Autowired
    private AvaliacaoService avaliacaoService;

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private TurmaRepository turmaRepository;

    public List<ProfessorModel> findAll(){
        return professorRepository.findAll();
    }

    public ProfessorModel findById(long id){
        Optional<ProfessorModel> professorModel = this.professorRepository.findById(id);
        return professorModel.orElseThrow(() -> new RuntimeException(
                "Professor não encontrado"
        ));
    }

    public UserDetails findByEmail(String email){
        return professorRepository.findByEmail(email);
    }

    public ProfessorModel findProfessorModelByEmail(String email){
        return professorRepository.findProfessorModelByEmail(email);
    }

    @Transactional
    public ProfessorModel create(ProfessorModel obj, List<Turma> turmas){
        obj = this.professorRepository.save(obj);
        for (Turma t: turmas){
            TurmaModel novaTurma = new TurmaModel(t, obj);
            turmaRepository.save(novaTurma);
        }
        return obj;
    }

    @Transactional
    public ProfessorModel update(ProfessorModel obj){
        ProfessorModel newProfessor = findById(obj.getId());
        if(Objects.nonNull(obj.getSenha())){newProfessor.setSenha(obj.getSenha());}
        if (Objects.nonNull(obj.getEmail())){newProfessor.setEmail(obj.getEmail());}
        if (Objects.nonNull(obj.getMateria())){newProfessor.setMateria(obj.getMateria());}
        if (Objects.nonNull(obj.getNome())){newProfessor.setNome(obj.getNome());}
        return this.professorRepository.save(newProfessor);
    }

    @Transactional
    public void delete(long id){
        ProfessorModel professor = findById(id);
        try {
            this.avaliacaoRepository.deleteByProfessorModel(professor);
            this.professorRepository.deleteById(id);
        } catch (Exception e){
            throw new RuntimeException("Não é possivel excluir pois há entidades relacionadas");
        }
    }

    public Integer mediaDoProfessorPorId(long id){
        ProfessorModel professor = findById(id);
        int cont = 0;
        int soma = 0;
        List<AvaliacaoModel> avaliacoes = avaliacaoRepository.findByProfessorModel(professor);
        for (AvaliacaoModel avaliacao: avaliacoes){
            soma += avaliacao.getAulaModel().getNota();
            cont ++;
        }
        if (cont > 0){return soma/cont;}
        else{ return 0;}
    }

    public List<ProfessorModel> filtrarPorTurma(Turma turma){
        List<Long> idsDosProfessores = turmaRepository.findId_professorByTurma(turma);
        List<ProfessorModel> listaDeProfessores = List.of();
        for (Long id: idsDosProfessores){
            listaDeProfessores.add(professorRepository.findProfessorModelById(id));
        }
        return listaDeProfessores;
    }

    public List<ProfessorModel> filtrarProfessoresNaoAvaliadosEstaSemana(List<ProfessorModel> professores, AlunoModel aluno){
        List<ProfessorModel> professoresFiltrados = List.of();
        for (ProfessorModel professor: professores){
            List<AvaliacaoModel> avaliacoes = avaliacaoRepository.findByProfessorModelAndAlunoModel(professor, aluno);
            if (!avaliacaoService.foiAvaliadoNessaSemana(avaliacoes)){
                professoresFiltrados.add(professor);
            }
        }
        return  professoresFiltrados;
    }

}
