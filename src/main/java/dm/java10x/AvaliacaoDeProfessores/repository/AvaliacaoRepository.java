package dm.java10x.AvaliacaoDeProfessores.repository;

import dm.java10x.AvaliacaoDeProfessores.model.AlunoModel;
import dm.java10x.AvaliacaoDeProfessores.model.AulaModel;
import dm.java10x.AvaliacaoDeProfessores.model.AvaliacaoModel;
import dm.java10x.AvaliacaoDeProfessores.model.ProfessorModel;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AvaliacaoRepository extends JpaRepository<AvaliacaoModel, Long> {

    void deleteByAulaModel(AulaModel aulaModel);

    void deleteByProfessorModel(ProfessorModel professorModel);

    void deleteByAlunoModel(AlunoModel alunoModel);

    List<AvaliacaoModel> findByProfessorModel(ProfessorModel professorModel);

    List<AvaliacaoModel> findByProfessorModelAndAlunoModel(ProfessorModel professorModel, AlunoModel alunoModel);
}

