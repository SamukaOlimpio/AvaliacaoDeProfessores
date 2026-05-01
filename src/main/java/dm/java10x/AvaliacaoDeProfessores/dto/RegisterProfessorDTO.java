package dm.java10x.AvaliacaoDeProfessores.dto;

import dm.java10x.AvaliacaoDeProfessores.enumeradores.Materia;
import java.util.List;

public record RegisterProfessorDTO(
        String nome,
        Materia materia,
        String senha,
        String email,
        List<String> turmas
) {
}