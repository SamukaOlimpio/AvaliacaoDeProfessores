package dm.java10x.AvaliacaoDeProfessores.repository;






import dm.java10x.AvaliacaoDeProfessores.model.ProfessorModel;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface ProfessorRepository extends JpaRepository<ProfessorModel, Long> {
    UserDetails findByEmail(String email);

    ProfessorModel findProfessorModelById(Long id);

    ProfessorModel findProfessorModelByEmail(String email);
}


