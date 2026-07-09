package dm.java10x.AvaliacaoDeProfessores.repository;

import dm.java10x.AvaliacaoDeProfessores.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
