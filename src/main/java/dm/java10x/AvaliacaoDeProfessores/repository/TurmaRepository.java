package dm.java10x.AvaliacaoDeProfessores.repository;

import dm.java10x.AvaliacaoDeProfessores.enumeradores.Turma;
import dm.java10x.AvaliacaoDeProfessores.model.ProfessorModel;
import dm.java10x.AvaliacaoDeProfessores.model.TurmaModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TurmaRepository extends JpaRepository<TurmaModel, Long> {

    List<TurmaModel> findTurmaModelByTurma(Turma turma);

    List<ProfessorModel> findProfessorModelByTurma(Turma turma);

    void deleteByProfessorModel(ProfessorModel professor);
}
