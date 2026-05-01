package dm.java10x.AvaliacaoDeProfessores.repository;





import dm.java10x.AvaliacaoDeProfessores.model.ProfessorModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessorRepository extends JpaRepository<ProfessorModel, Long> {
    UserDetails findByEmail(String email);
}


